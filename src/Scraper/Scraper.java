package Scraper;

import News.Article;
import News.Content.ContentFactory;
import News.Content.Detail;
import News.Content.DetailFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Scraper {
    static final int MAX_ARTICLES_PER_CATEGORY = 5;

    URL newsOutletUrl;
    String titleLinkClass;
    String titleClass;
    String descriptionClass;
    String contentBodyClass;
    String dateTimeClass;
    String pictureClass;
    HashMap<String, URL> categories;

    DetailFactory detailFactory = new ContentFactory();
    DateTimeRetrievable dateTimeRetriever;

    public Scraper(String newsOutletUrl,
                   String titleLinkClass,
                   String titleClass,
                   String descriptionClass,
                   String contentBodyClass,
                   String dateTimeClass,
                   String pictureClass,
                   HashMap<String, URL> categories,
                   DateTimeRetrievable dateTimeRetriever){

        try {
            this.newsOutletUrl = new URL(newsOutletUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        this.titleLinkClass = titleLinkClass;
        this.titleClass = titleClass;
        this.descriptionClass = descriptionClass;
        this.contentBodyClass = contentBodyClass;
        this.dateTimeClass = dateTimeClass;
        this.pictureClass = pictureClass;
        this.categories = categories;
        this.dateTimeRetriever = dateTimeRetriever;
    }

    public HashSet<Article> getArticlesFromCategories(){
        HashSet<Article> articles = new HashSet<>();

        for(String categoryName: categories.keySet()){

            // retrieve all article links in each category
            HashSet<URL> urlsInCategory = getAllArticleLinks(categories.get(categoryName));

            // in each category, create articles from its links
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
            Elements titleTags = doc.getElementsByClass(this.titleLinkClass);
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
            title = doc.getElementsByClass(this.titleClass).first();
            description = doc.getElementsByClass(this.descriptionClass).first();
            content = doc.getElementsByClass(this.contentBodyClass);
            dateTimeStr = dateTimeRetriever.getDateTimeString(doc, this.dateTimeClass);

            // first pic is always the article thumbnail !
            // TODO: create DEFAULT thumbnail in case there is no pic in doc
            thumbNail = doc.getElementsByClass(this.pictureClass).first();

//            System.out.println(articleUrl.toString());
//            System.out.println(thumbNail);
//            System.out.println("==========================");

            if (title == null || description == null || content == null || dateTimeStr == null || thumbNail == null){
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
        ArrayList<Detail> details = detailFactory.createDetailList(contentTag);

        String thumbNailUrl = thumbNailTag.getElementsByTag("img").attr("data-src");
        if (thumbNailUrl.isEmpty()){
            thumbNailUrl = thumbNailTag.getElementsByTag("img").attr("src");
        }

        LocalDateTime localDateTime = dateTimeRetriever.getLocalDateTime(dateTime);

        if (title.isEmpty() || description.isEmpty() || details.isEmpty() || thumbNailUrl.isEmpty() || localDateTime == null){
            return null;
        }

        return new Article(title, description, details, thumbNailUrl, localDateTime, mainCategory);
    }
}
