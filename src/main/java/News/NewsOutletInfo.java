package News;
import News.Sanitizer.*;
import Scraper.*;
import static Scraper.Scraper.*;
import org.jsoup.nodes.*;
import java.time.LocalDateTime;
import java.util.HashMap;

public class NewsOutletInfo implements Scrapable {
    public String name = "";
    public String baseUrl = "";
    public String titleLinkCssClass = "";
    public String titleCssClass = "";
    public String descriptionCssClass = "";
    public String contentBodyCssClass = "";
    public String dateTimeCssClass = "";
    public String pictureCssClass = "";
    public String thumbNailCssClass = "";
    public String defaultThumbNailUrl = "";
    public HashMap<String, String> categories = null;

    // sanitizer
    public HtmlSanitizer sanitizer;

    // scraping behaviors
    private PublishedTimeScrapable publishedTimeScrapable;
    private CategoryScrapable categoryScrapable;

    // setters

    public void setPublishedTimeScrapable(PublishedTimeScrapable publishedTimeScrapable) {
        this.publishedTimeScrapable = publishedTimeScrapable;
    }

    public void setCategoryScrapable(CategoryScrapable categoryScrapable) {
        this.categoryScrapable = categoryScrapable;
    }

    public NewsOutletInfo(String name){
        this.name = name;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void setCategories(HashMap<String, String> categories) {
        this.categories = categories;
    }

    public void setTitleLinkCssClass(String titleLinkCssClass) {
        this.titleLinkCssClass = titleLinkCssClass;
    }

    public void setDescriptionCssClass(String descriptionCssClass) {
        this.descriptionCssClass = descriptionCssClass;
    }

    public void setContentBodyCssClass(String contentBodyCssClass) {
        this.contentBodyCssClass = contentBodyCssClass;
    }

    public void setPictureCssClass(String pictureCssClass) {
        this.pictureCssClass = pictureCssClass;
    }

    public void setDateTimeCssClass(String dateTimeCssClass) {
        this.dateTimeCssClass = dateTimeCssClass;
    }

    public void setThumbNailCssClass(String thumbNailCssClass){
        this.thumbNailCssClass = thumbNailCssClass;
    }

    public void setSanitizer(HtmlSanitizer sanitizer) {
        this.sanitizer = sanitizer;
    }

    public void setTitleCssClass(String titleCssClass) {
        this.titleCssClass = titleCssClass;
    }

    public void setDefaultThumbNailUrl(String url){
        this.defaultThumbNailUrl = url;
    }

    @Override
    public Element getTitleTag(Document doc) {
        return scrapeFirstElementByClass(doc, this.titleCssClass);
    }

    @Override
    public Element getDescriptionTag(Document doc) {
        return scrapeFirstElementByClass(doc, this.descriptionCssClass);
    }

    @Override
    public Element getMainContent(Document doc) {
        return scrapeFirstElementByClass(doc, this.contentBodyCssClass);
    }

    @Override
    public Element getThumbnail(Document doc) {
        String css;
        if (this.thumbNailCssClass.isEmpty()) css = this.pictureCssClass;
        else css = this.thumbNailCssClass;

        Element elementContainsImgs = scrapeFirstElementByClass(doc, css);
        if (elementContainsImgs == null)
            return getDefaultThumbnail();

        Element firstImgTag = elementContainsImgs.getElementsByTag("img").first();
        if (firstImgTag == null)
            return getDefaultThumbnail();

        return createCleanImgTag(firstImgTag);
    }

    @Override
    public LocalDateTime getPublishedTime(Document doc) {
        return publishedTimeScrapable.getLocalDateTime(doc, this.dateTimeCssClass);
    }

    @Override
    public String getCategory(Document doc) {
        return categoryScrapable.getCategory(doc);
    }

    Element getDefaultThumbnail(){
        Element thumbnail = new Element("img");
        thumbnail.attr("src", this.defaultThumbNailUrl);
        return thumbnail;
    }


}
