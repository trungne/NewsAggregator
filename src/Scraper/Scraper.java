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
    static final int MAX_ARTICLES_PER_CATEGORY = 5;

    public HashSet<Article> scrape(NewsOutlet newsOutlet){
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
            e.printStackTrace();
        }
        return links;
    }

    private Article getArticle(URL articleUrl, String category, NewsOutlet newsOutlet){
        Document doc;

        Article article;

        Element titleTag = null;
        Element descriptionTag = null;
        Elements contentTag = null;
        Element thumbNailTag = null;
        String dateTimeStr = "";

        try {
            // scrape all html tags that contain properties of the article
            doc = Jsoup.connect(articleUrl.toString()).get();

            // TODO: videos sometimes do not have title tag!
            titleTag = doc.getElementsByClass(newsOutlet.titleCssClass).first();
            descriptionTag = doc.getElementsByClass(newsOutlet.descriptionCssClass).first();
            contentTag = doc.getElementsByClass(newsOutlet.contentBodyCssClass);
            dateTimeStr = newsOutlet.dateTimeRetrievable.getDateTimeString(doc, newsOutlet.dateTimeCssClass);

            // first pic is always the article thumbnail !
            thumbNailTag = doc.getElementsByClass(newsOutlet.pictureCssClass).first();
        }
        catch (IOException e){
            e.printStackTrace();
        }

        // TODO: create a separate function to check if scraped html/text are valid to create an article object
        if (titleTag == null || descriptionTag == null || contentTag == null){
            return null;
        }

        // create the article by all scraped html tags and the provided category
        String title = titleTag.text();
        String description = descriptionTag.text();
        ArrayList<Detail> details = newsOutlet.detailFactory.createDetailList(contentTag);

        String thumbNailUrl = newsOutlet.defaultThumbNailUrl;
        if (thumbNailTag != null){
            // look for img in the data-src first, if not found, look in src
            thumbNailUrl = thumbNailTag.getElementsByTag("img").attr("data-src");
            if (thumbNailUrl.isEmpty()){
                thumbNailUrl = thumbNailTag.getElementsByTag("img").attr("src");
            }
        }

        LocalDateTime localDateTime = newsOutlet.dateTimeRetrievable.getLocalDateTime(dateTimeStr);
        if (title.isEmpty() || description.isEmpty() || details.isEmpty() || thumbNailUrl.isEmpty()){
            return null;
        }

        return new Article(articleUrl, category, title, description, details, thumbNailUrl, localDateTime);
    }
}
