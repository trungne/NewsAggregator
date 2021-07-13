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

    NewsOutlet newsOutlet;

    // factory
    DetailFactory detailFactory;

    // datetime retrievable methods
    DateTimeRetrievable dateTimeRetrievable;

    public Scraper(NewsOutlet newsOutlet){
        this.newsOutlet = newsOutlet;

        this.detailFactory = newsOutlet.detailFactory;
        this.dateTimeRetrievable = newsOutlet.dateTimeRetrievable;
    }

    public HashSet<Article> getArticlesFromCategories(){
        HashSet<Article> articles = new HashSet<>();

        // step 1: get url to each category
        // step 2: For each category, get all article links belong to that category
        // step 3: For each article, create an article object

        for(String categoryName: this.newsOutlet.categories.keySet()){

            // step 1: get url to a specific category (value) by assessing the category name (key)
            URL urlToCategory;
            try{
                urlToCategory = new URL(this.newsOutlet.categories.get(categoryName));
            }
            catch (MalformedURLException e) {
                e.printStackTrace();
                continue; // skip if the link cannot be reached
            }

            // step 2
            HashSet<URL> urlsInCategory = getAllArticleLinks(urlToCategory);

            // step 3
            for (URL url: urlsInCategory){
                Article article = getArticle(url, categoryName);
                if (article != null)
                    articles.add(article);
            }
        }

        return articles;
    }


    // get all article links in a category
    private HashSet<URL> getAllArticleLinks(URL baseUrl){
        Document doc;

        // Do not store duplicates
        HashSet<URL> links = new HashSet<>();
        try{
            doc = Jsoup.connect(baseUrl.toString()).get();
            Elements titleTags = doc.getElementsByClass(this.newsOutlet.titleLinkClass);
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

    private Article getArticle(URL articleUrl, String category){
        Document doc;

        Article article = null;
        Element title;
        Element description;
        Elements content;
        Element thumbNail;
        String dateTimeStr;

        try {
            // scrape all html tags that contain properties of the article
            doc = Jsoup.connect(articleUrl.toString()).get();

            // TODO: videos sometimes do not have title tag!
            title = doc.getElementsByClass(this.newsOutlet.titleClass).first();
            description = doc.getElementsByClass(this.newsOutlet.descriptionClass).first();
            content = doc.getElementsByClass(this.newsOutlet.contentBodyClass);
            dateTimeStr = this.newsOutlet.dateTimeRetrievable.getDateTimeString(doc, this.newsOutlet.dateTimeClass);

            // first pic is always the article thumbnail !
            thumbNail = doc.getElementsByClass(this.newsOutlet.pictureClass).first();

            if (title == null || description == null || content == null || dateTimeStr == null){
                return null;
            }

            // create the article by all scraped html tags and the provided category
            article = createArticleFromHtmlElements(title,
                                                    description,
                                                    content,
                                                    thumbNail,
                                                    dateTimeStr,
                                                    category);
        }
        catch (IOException e){
            e.printStackTrace();
        }

        return article;
    }

    private Article createArticleFromHtmlElements(Element titleTag,
                                                  Element descriptionTag,
                                                  Elements contentTag,
                                                  Element thumbNailTag,
                                                  String dateTime,
                                                  String mainCategory){
        String title = titleTag.text();
        String description = descriptionTag.text();
        ArrayList<Detail> details = this.detailFactory.createDetailList(contentTag);

        String thumbNailUrl = this.newsOutlet.defaultThumbNailUrl;
        if (thumbNailTag != null){
            // look for img in the data-src first, if not found, look in src
            thumbNailUrl = thumbNailTag.getElementsByTag("img").attr("data-src");
            if (thumbNailUrl.isEmpty()){
                thumbNailUrl = thumbNailTag.getElementsByTag("img").attr("src");
            }
        }

        LocalDateTime localDateTime = this.dateTimeRetrievable.getLocalDateTime(dateTime);
        if (title.isEmpty() || description.isEmpty() || details.isEmpty() || thumbNailUrl.isEmpty()){
            return null;
        }

        return new Article(title, description, details, thumbNailUrl, localDateTime, mainCategory);
    }
}
