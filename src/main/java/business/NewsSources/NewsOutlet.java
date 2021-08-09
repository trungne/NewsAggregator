package business.NewsSources;

import business.Sanitizer.HtmlSanitizer;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static business.Helper.Scraper.createCleanImgTag;
import static business.Helper.Scraper.scrapeFirstElementByClass;

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
        if (title != null) title = sanitizer.sanitizeTitle(title);
        return title;
    }

    // by default, scrape the first element that matches provided css
    public Element getDescription(Document doc) {
        Element desp = scrapeFirstElementByClass(doc, cssConfiguration.description);
        if (desp != null) desp = sanitizer.sanitizeDescription(desp);
        return desp;
    }

    // by default, scrape the first element that matches provided css
    public Element getMainContent(Document doc) {
        Element content = scrapeFirstElementByClass(doc, cssConfiguration.mainContent);
        if (content != null) content = sanitizer.sanitizeMainContent(content);
        return content;
    }

    // by default, set the first img as the thumbnail
    public Element getThumbnail(Document doc) {
        try {
            Element elementContainsImgs = scrapeFirstElementByClass(doc, cssConfiguration.picture);
            Element thumbnail = elementContainsImgs.getElementsByTag("img").first();
            thumbnail = createCleanImgTag(thumbnail);
            thumbnail = sanitizer.sanitizeThumbNail(thumbnail);
            return thumbnail;
        } catch (NullPointerException e) {
            return getDefaultThumbnail();
        }
    }

    protected Element getDefaultThumbnail() {
        Element thumbnail = new Element("img");
        thumbnail.attr("src", defaultThumbnail);
        thumbnail = sanitizer.sanitizeThumbNail(thumbnail);
        return thumbnail;
    }

    protected Category getCategory(String categoryName){
        if (categories.containsKey(categoryName)){
            return categories.get(categoryName);
        }

        for (Category category: categories.values()){
            if(category.getName().equals(categoryName)){
                return category;
            }
        }
        return null;
    }
    // find category by name
    private Category find(String name){

        Category matched;
        for (Category category: categories.values()){
            matched = category.find(name);
            if (matched != null) return matched;
        }
        return null;
    }
    public Set<URL> getLinksFromCategory(String categoryName) {
        Category category = find(categoryName);
        if(category == null)
            return new HashSet<>();
        return category.getLinks();
    }

    public abstract LocalDateTime getPublishedTime(Document doc);

    // one article can have multiple categories. The first category will be the parent category
    public abstract List<String> getCategoryNames(Document doc);

    public String toString() {
        return this.name;
    }

    public String getName() {
        return this.name;
    }
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