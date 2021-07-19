package Scraper;

import News.*;
import News.Content.*;

import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

import java.io.*;
import java.net.*;
import java.time.LocalDateTime;

import java.util.*;

public class Scraper {
    static final int MAX_ARTICLES_PER_CATEGORY = 2;
    static final int TIME_OUT_SECOND = 10 * 1000;

    public void scrapeWebAndFillCollection(NewsOutlet newsOutlet, Collection<Article> collection){
        /*
        step 1: get url to each category
        step 2: For each category, get all article links belong to that category
        step 3: For each article, create an article object
        */
        // step 1: get url to a specific category (value) by assessing the category name (key)
        for (String category: newsOutlet.categories.keySet()){
            URL urlToCategory;
            try{
                urlToCategory = new URL(newsOutlet.categories.get(category));
            }
            catch (MalformedURLException e) {
                e.printStackTrace();
                continue; // skip if the link cannot be reached
            }

            // step 2
            HashSet<URL> urlsInCategory = getAllArticleLinksInCategory(urlToCategory, newsOutlet.titleLinkCssClass);

            // step 3
            for (URL url: urlsInCategory){
                // TODO: check if url is already scraped/in the database

                Article article = getArticle(url, category, newsOutlet);
                if (article != null)
                    collection.add(article);
            }
        }

    }


    public Collection<Article> scrape(NewsOutlet newsOutlet){
        HashSet<Article> articles = new HashSet<>();
        /*
        step 1: get url to each category
        step 2: For each category, get all article links belong to that category
        step 3: For each article, create an article object
        */

        // step 1: get url to a specific category (value) by assessing the category name (key)
        for (String category: newsOutlet.categories.keySet()){
            URL urlToCategory;
            try{
                urlToCategory = new URL(newsOutlet.categories.get(category));
            }
            catch (MalformedURLException e) {
                e.printStackTrace();
                continue; // skip if the link cannot be reached
            }

            // step 2
            HashSet<URL> urlsInCategory = getAllArticleLinksInCategory(urlToCategory, newsOutlet.titleLinkCssClass);

            // step 3
            for (URL url: urlsInCategory){
                Article article = getArticle(url, category, newsOutlet);
                if (article != null)
                    articles.add(article);
            }
        }
        return articles;
    }

    // get all article links in a category
    private HashSet<URL> getAllArticleLinksInCategory(URL baseUrl, String titleLinkClass){
        Document doc;

        // Do not store duplicates
        HashSet<URL> links = new HashSet<>();
        try{
            doc = Jsoup.connect(baseUrl.toString()).get();
            Elements titleTags = doc.getElementsByClass(titleLinkClass);
            // target all title tags and pull out links for articles
            for (Element e: titleTags){

                if (links.size() > MAX_ARTICLES_PER_CATEGORY) return links;
                // link is stored in href attribute of <a> tag
                URL link = new URL(baseUrl, e.getElementsByTag("a").attr("href"));
                links.add(link);
            }

        } catch (IOException e){
            // TODO: disable this in production
            e.printStackTrace();
        }
        return links;
    }

    private Article getArticle(URL articleUrl, String category, NewsOutlet newsOutlet){
        try {
            Document doc;
            // scrape all html tags that contain properties of the article
            doc = Jsoup.connect(articleUrl.toString()).timeout(TIME_OUT_SECOND).get();

            // TODO: videos sometimes do not have title tag!
            Element titleTag = doc.getElementsByClass(newsOutlet.titleCssClass).first();
            Element descriptionTag = doc.getElementsByClass(newsOutlet.descriptionCssClass).first();
            Element mainContentTag = doc.getElementsByClass(newsOutlet.contentBodyCssClass).first();
            // first pic is always the article thumbnail !
            Element thumbNailTag = doc.getElementsByClass(newsOutlet.pictureCssClass).first();

            String dateTimeStr = newsOutlet.dateTimeRetriever.getDateTimeString(doc, newsOutlet.dateTimeCssClass);

            // TODO: create a separate function to check if scraped html/text are valid to create an article object

            // sanitize and customize 3 scraped tags
            titleTag = newsOutlet.detailFactory.createHtmlTag(titleTag, DetailFactory.TITLE_CSS_CLASS);
            descriptionTag = newsOutlet.detailFactory.createHtmlTag(descriptionTag, DetailFactory.DESCRIPTION_CSS_CLASS);
            mainContentTag = newsOutlet.detailFactory.createHtmlTag(mainContentTag, DetailFactory.MAIN_CONTENT_CSS_CLASS);

            String thumbNailUrl = newsOutlet.defaultThumbNailUrl;
            if (thumbNailTag != null){
                // look for img in the data-src first, if not found, look in src
                Element firstImgTag = thumbNailTag.getElementsByTag("img").first();

                if (firstImgTag != null){
                    if (firstImgTag.hasAttr("data-src")){
                        thumbNailUrl = firstImgTag.attr("abs:data-src");
                    }
                    else if (firstImgTag.hasAttr("src")){
                        thumbNailUrl = firstImgTag.attr("src");
                    }
                }
            }

            LocalDateTime localDateTime = newsOutlet.dateTimeRetriever.getLocalDateTime(dateTimeStr);

            if (titleTag == null || descriptionTag == null || mainContentTag == null){
                return null;
            }

            return new Article(articleUrl, category, titleTag, descriptionTag, mainContentTag, thumbNailUrl, localDateTime);
        }
        catch (IOException e){
            // TODO: disable this in production!
            e.printStackTrace();
            return null;
        }
        catch (NullPointerException e){
            return null;
        }
    }
}
