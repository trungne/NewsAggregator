package News;

import News.Sanitizer.HtmlSanitizer;
import Scraper.Scraper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

public class ArticleListGenerator {
    NewsOutlet newsOutlet;

    ArrayList<Article> articles = new ArrayList<>();



    public ArticleListGenerator(NewsOutlet newsOutlet){
        this.newsOutlet = newsOutlet;
    }

    public ArrayList<Article> getArticles(){
        if (articles.isEmpty()){
            extractArticlesFromNewsOutlet();
        }
        return articles;
    }

    public void extractArticlesFromNewsOutlet(){
        HashMap<String, ArrayList<URL>> categories = extractLinksFromCategories();

        for (String category: categories.keySet()){
            for(URL articleUrl: categories.get(category)){
                Article article = createArticle(articleUrl, category);
                // TODO: check if article already exists, if yes add current category to it
                if (article != null)
                    articles.add(article);
            }
        }
    }


    private Article createArticle(URL url, String category){
        try{
            Document articleDoc = Jsoup.connect(url.toString()).timeout(5000).get();
            Scraper scraper = new Scraper(articleDoc);

            // scrape all needed tags of the article
            Element titleTag = scraper.scrapeElementByClass(newsOutlet.titleCssClass);
            Element descriptionTag = scraper.scrapeElementByClass(newsOutlet.descriptionCssClass);
            Element mainContentTag = scraper.scrapeElementByClass(newsOutlet.contentBodyCssClass);
            String thumbNailUrl = scraper.scrapeFirstImgUrlByClass(newsOutlet.pictureCssClass);
            if (thumbNailUrl.isEmpty()){
                thumbNailUrl = newsOutlet.defaultThumbNailUrl;
            }
            LocalDateTime dateTime = scraper.scrapeDateTime(newsOutlet.dateTimeCssClass, newsOutlet.scrapingDateTimeBehavior);


            // sanitize all scraped tags and customize them
            titleTag = newsOutlet.sanitizer.sanitize(titleTag, HtmlSanitizer.TITLE_CSS_CLASS);
            descriptionTag = newsOutlet.sanitizer.sanitize(descriptionTag, HtmlSanitizer.DESCRIPTION_CSS_CLASS);
            mainContentTag = newsOutlet.sanitizer.sanitize(mainContentTag, HtmlSanitizer.MAIN_CONTENT_CSS_CLASS);

            // no need to sanitize thumbnail url or date time as default values will be assigned
            // to such values if they are null

            // TODO: Check valid tags
            if (titleTag == null || descriptionTag == null || mainContentTag == null){
                return null;
            }

            return new Article(url, category, titleTag, descriptionTag, mainContentTag, thumbNailUrl, dateTime);

        }
        catch (IOException | NullPointerException e){
            return null;
        }

    }

    private HashMap<String, ArrayList<URL>> extractLinksFromCategories(){

        HashMap<String, ArrayList<URL>> categories = new HashMap<>();

        // TODO: check if link already exists in database?

        //
        for (String category: newsOutlet.categories.keySet()){
            ArrayList<URL> links;
            URL categoryUrl;
            try {
                categoryUrl = new URL(newsOutlet.categories.get(category));
            }
            catch (MalformedURLException e){
                continue; // skip if category url cannot be formed;
            }

            links = Scraper.scrapeLinksByClass(categoryUrl, newsOutlet.titleLinkCssClass);

            categories.put(category, links);
        }


        return categories;


    }
}
