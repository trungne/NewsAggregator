package business.NewsSources;

import business.Helper.CATEGORY;
import business.Sanitizer.*;
import org.jsoup.nodes.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;

import static business.Helper.Scraper.*;

public abstract class NewsOutlet {
    protected final String name;
    protected final String defaultThumbnail;
    protected final HashMap<String, Category> categories;
    protected final CssConfiguration cssConfiguration;
    protected final HtmlSanitizer sanitizer;

    public NewsOutlet(String name,
                      String defaultThumbnail,
                      HashMap<String, Category> categories,
                      CssConfiguration cssConfiguration,
                      HtmlSanitizer sanitizer) {
        this.name = name;
        this.defaultThumbnail = defaultThumbnail;
        this.categories = categories;
        this.cssConfiguration = cssConfiguration;
        this.sanitizer = sanitizer;
    }

    // by default, scrape the first element that matches provided css
    public Element getTitle(Document doc) {
        Element title = scrapeFirstElementByClass(doc, cssConfiguration.title);
        if(title != null) title = sanitizer.sanitizeTitle(title);
        return title;
    }

    // by default, scrape the first element that matches provided css
    public Element getDescription(Document doc) {
        Element desp = scrapeFirstElementByClass(doc, cssConfiguration.description);
        if(desp != null) desp = sanitizer.sanitizeDescription(desp);
        return desp;
    }

    // by default, scrape the first element that matches provided css
    public Element getMainContent(Document doc) {
        Element content = scrapeFirstElementByClass(doc, cssConfiguration.mainContent);
        if(content != null) content = sanitizer.sanitizeMainContent(content);
        return content;
    }

    // by default, set the first img as the thumbnail
    public Element getThumbnail(Document doc){
        try{
            Element elementContainsImgs = scrapeFirstElementByClass(doc, cssConfiguration.picture);
            Element thumbnail = elementContainsImgs.getElementsByTag("img").first();
            thumbnail = createCleanImgTag(thumbnail);
            thumbnail = sanitizer.sanitizeThumbNail(thumbnail);
            return thumbnail;
        } catch (NullPointerException e){
            return getDefaultThumbnail();
        }
    }

    protected Element getDefaultThumbnail(){
        Element thumbnail = new Element("img");
        thumbnail.attr("src", defaultThumbnail);
        thumbnail = sanitizer.sanitizeThumbNail(thumbnail);
        return thumbnail;
    }

    public Collection<URL> getLinksFromCategory(String categoryName){
        if (categories.containsKey(categoryName)){
            Category category = categories.get(categoryName);
            return category.getLinks();
        }
        else {
            return null;
        }

    }

    public abstract LocalDateTime getPublishedTime(Document doc);

    // one article can have multiple categories. The first category will be the parent category
    public abstract List<String> getCategoryNames(Document doc);

    public String toString(){
        return this.name;
    }
    public String getName() {return this.name;}
}

class CssConfiguration {
    public String baseUrl;
    public String title;
    public String description;
    public String mainContent;
    public String publishedTime;
    public String picture;

    public CssConfiguration(String baseUrl,
                            String title,
                            String description,
                            String mainContent,
                            String publishedTime,
                            String picture) {
        this.baseUrl = baseUrl;
        this.title = title;
        this.description = description;
        this.mainContent = mainContent;
        this.publishedTime = publishedTime;
        this.picture = picture;
    }
}