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
    private static final int MAX_WAIT_TIME = 5000; // ms

    public static ArrayList<Article> getArticles(NewsOutletInfo newsOutletInfo){
        // TODO: get articles from database
        return extractArticlesFromCategories(newsOutletInfo);
    }

    private static ArrayList<Article> extractArticlesFromCategories(NewsOutletInfo newsOutletInfo){
        ArrayList<Article> articles = new ArrayList<>();
        HashMap<String, ArrayList<URL>> categories = extractLinksFromCategories(newsOutletInfo);

        for (String category: categories.keySet()){
            for(URL articleUrl: categories.get(category)){
                Article article = createArticle(articleUrl, category, newsOutletInfo);
                // TODO: check if article already exists, if yes add current category to it
                if (article != null)
                    articles.add(article);
            }
        }
        return articles;
    }


    private static Article createArticle(URL url, String category, NewsOutletInfo newsOutletInfo){
        try{
            Document articleDoc = Jsoup.connect(url.toString()).timeout(MAX_WAIT_TIME).get();

            // scrape all needed tags of the article
            Element titleTag = Scraper.scrapeElementByClass(articleDoc, newsOutletInfo.titleCssClass);
            Element descriptionTag = Scraper.scrapeElementByClass(articleDoc, newsOutletInfo.descriptionCssClass);
            Element mainContentTag = Scraper.scrapeElementByClass(articleDoc, newsOutletInfo.contentBodyCssClass);
            String thumbNailUrl = Scraper.scrapeFirstImgUrlByClass(articleDoc, newsOutletInfo.pictureCssClass);
            if (thumbNailUrl.isEmpty()){
                thumbNailUrl = newsOutletInfo.defaultThumbNailUrl;
            }
            LocalDateTime dateTime = newsOutletInfo.scrapingDateTimeBehavior.getLocalDateTime(articleDoc, newsOutletInfo.dateTimeCssClass);


            // sanitize all scraped tags and customize them
            titleTag = newsOutletInfo.sanitizer.sanitize(titleTag, HtmlSanitizer.TITLE_CSS_CLASS);
            descriptionTag = newsOutletInfo.sanitizer.sanitize(descriptionTag, HtmlSanitizer.DESCRIPTION_CSS_CLASS);
            mainContentTag = newsOutletInfo.sanitizer.sanitize(mainContentTag, HtmlSanitizer.MAIN_CONTENT_CSS_CLASS);

            // no need to sanitize thumbnail url or date time as default values will be assigned
            // to such values if they are null

            // TODO: Check valid tags
            if (titleTag == null || descriptionTag == null || mainContentTag == null){
                return null;
            }

            Article article = new Article();

            article.setUrl(url);
            article.addCategory(category);
            article.setTitle(titleTag);
            article.setDescription(descriptionTag);
            article.setMainContent(mainContentTag);
            article.setThumbNailUrl(thumbNailUrl);
            article.setDateTime(dateTime);

            return article;

        }
        catch (IOException | NullPointerException e){
            return null;
        }

    }

    private static HashMap<String, ArrayList<URL>> extractLinksFromCategories(NewsOutletInfo newsOutletInfo){

        HashMap<String, ArrayList<URL>> categories = new HashMap<>();

        // TODO: check if link already exists in database?

        for (String category: newsOutletInfo.categories.keySet()){
            ArrayList<URL> links;
            URL categoryUrl;
            try {
                categoryUrl = new URL(newsOutletInfo.categories.get(category));
            }
            catch (MalformedURLException e){
                continue; // skip if category url cannot be formed;
            }

            links = Scraper.scrapeLinksByClass(categoryUrl, newsOutletInfo.titleLinkCssClass);

            categories.put(category, links);
        }


        return categories;


    }
}
