package News;

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

    public static ArrayList<Article> getArticlesInCategory(NewsOutletInfo newsOutletInfo, String category){
        return extractArticlesFromCategory(newsOutletInfo, category);
    }

    private static ArrayList<Article> extractArticlesFromCategory(NewsOutletInfo newsOutletInfo, String category){
        ArrayList<Article> articles = new ArrayList<>();
        ArrayList<URL> articleUrls = extractLinksFromCategory(newsOutletInfo, category);
        if (articleUrls != null){
            for (URL url: articleUrls){
                Article article = createArticle(url, category, newsOutletInfo);
                if (article != null)
                    articles.add(article);
            }
        }
        System.out.println(newsOutletInfo.name + "=>" + articles);
        return articles;
    }

    private static ArrayList<Article> extractArticlesFromCategories(NewsOutletInfo newsOutletInfo){
        ArrayList<Article> articles = new ArrayList<>();
        HashMap<String, ArrayList<URL>> categories = extractLinksFromCategories(newsOutletInfo);

        for (String category: categories.keySet()){
            for(URL articleUrl: categories.get(category)){
                Article article = createArticle(articleUrl, category, newsOutletInfo);
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
            Element thumbNail = Scraper.scrapeCleanedFirstImgTagByClass(articleDoc, newsOutletInfo.pictureCssClass);
            LocalDateTime dateTime = newsOutletInfo.scrapingDateTimeBehavior.getLocalDateTime(articleDoc, newsOutletInfo.dateTimeCssClass);
            if (!thumbNail.hasAttr("src")){
                thumbNail.attr("src", newsOutletInfo.defaultThumbNailUrl);
            }

            if (!thumbNail.hasAttr("alt")){
                // set alt to title tag text if there is one.
                // else, set it to default value, which is "thumbnail"
                thumbNail.attr("alt", !titleTag.text().isEmpty() ? titleTag.text() : "thumbnail");
            }

            // sanitize all scraped tags and customize them
            titleTag = newsOutletInfo.sanitizer.sanitize(titleTag, CSS.TITLE);
            descriptionTag = newsOutletInfo.sanitizer.sanitize(descriptionTag, CSS.DESCRIPTION);
            mainContentTag = newsOutletInfo.sanitizer.sanitize(mainContentTag, CSS.MAIN_CONTENT);
            thumbNail = newsOutletInfo.sanitizer.sanitize(thumbNail, CSS.THUMBNAIL);

            // no need to sanitize date time as a default value will be assigned if it is null

            // TODO: Check valid tags
            if (titleTag == null || descriptionTag == null || mainContentTag == null || thumbNail == null){
                return null;
            }

            Article article = new Article();

            article.setUrl(url);
            article.addCategory(category);
            article.setTitle(titleTag);
            article.setDescription(descriptionTag);
            article.setMainContent(mainContentTag);
            article.setThumbNailUrl(thumbNail);
            article.setDateTime(dateTime);
            article.setNewsSource(newsOutletInfo.name);

            return article;

        }
        catch (IOException | NullPointerException e){
            return null;
        }

    }

    private static ArrayList<URL> extractLinksFromCategory(NewsOutletInfo newsOutletInfo, String category){
        if (newsOutletInfo.categories.containsKey(category)){
            URL categoryUrl;
            try {
                categoryUrl = new URL(newsOutletInfo.categories.get(category));
                return new ArrayList<>(Scraper.scrapeLinksByClass(categoryUrl, newsOutletInfo.titleLinkCssClass));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static HashMap<String, ArrayList<URL>> extractLinksFromCategories(NewsOutletInfo newsOutletInfo){

        HashMap<String, ArrayList<URL>> categories = new HashMap<>();

        // TODO: check if link already exists in database?

        // loop through each category and scrape links in it
        for (String category: newsOutletInfo.categories.keySet()){
            ArrayList<URL> urls =  extractLinksFromCategory(newsOutletInfo, category);
            if (urls != null)
                categories.put(category, urls);
        }
        return categories;


    }
}
