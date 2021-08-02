package News;

import News.Sanitizer.*;
import org.jsoup.nodes.*;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Collection;
import static Scraper.Scraper.*;

public abstract class  NewsOutlet {

    String name;
    String defaultThumbnail;
    Categories categories;
    CssConfiguration cssConfiguration;
    HtmlSanitizer sanitizer;


    public NewsOutlet(String name,
                      String defaultThumbnail,
                      Categories categories,
                      CssConfiguration cssConfiguration,
                      HtmlSanitizer sanitizer){
        this.name = name;
        this.defaultThumbnail = defaultThumbnail;
        this.categories = categories;
        this.cssConfiguration = cssConfiguration;
        this.sanitizer = sanitizer;
    }

    // by default, scrape the first element that matches provided css
    public Element getTitle(Document doc){
        Element title = scrapeFirstElementByClass(doc, cssConfiguration.title);
        if(title != null) title = sanitizer.sanitizeTitle(title);
        return title;
    }

    // by default, scrape the first element that matches provided css
    public Element getDescription(Document doc){
        Element desp = scrapeFirstElementByClass(doc, cssConfiguration.description);
        if(desp != null) desp = sanitizer.sanitizeDescription(desp);
        return desp;
    }

    // by default, scrape the first element that matches provided css
    public Element getMainContent(Document doc){
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

    public Collection<URL> getLinksFromCategory(String category){
        return categories.getLinksFromCategory(category, cssConfiguration.titleInCategoryPage);
    }

    public abstract LocalDateTime getPublishedTime(Document doc);
    public abstract String getCategory(Document doc);


    // Override this in derived classes because Java is too stupid to implement
    // abstract static method.
//    public static NewsOutlet init(){
//        return null;
//    }

    public String toString(){
        return this.name;
    }
}
