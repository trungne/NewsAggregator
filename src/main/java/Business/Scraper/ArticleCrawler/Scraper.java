package Business.Scraper.ArticleCrawler;

import Business.News.Article;
import Business.News.ArticleFactory;
import Business.Scraper.Helper.LocalDateTimeParser;
import Business.Scraper.Helper.ScrapingUtils;
import Business.Scraper.LinksCrawler.Category;
import Business.Scraper.Sanitizer.MainContentFilter;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Safelist;
import org.jsoup.select.Elements;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static Business.Scraper.Helper.ScrapingUtils.getFirstElementByClass;

public class Scraper {
    private final MainContentFilter sanitizer;
    private final String DEFAULT_THUMBNAIL;
    private final String SOURCE;

    // css class to target and pull out need elements
    private final String TITLE;
    private final String AUTHOR;
    private final String DESCRIPTION;
    private final String MAIN_CONTENT;
    private final String CATEGORY;
    private final String PICTURE;
    private final String THUMBNAIL;
    private final String PUBLISHED_TIME;

    /** Scraper Constructor
     * @param source article's url domain
     * @param sanitizer article's sanitizer
     * @param defaultThumbnail thumbnail default url
     * @param title article title class
     * @param author article author class
     * @param category article category class
     * @param description article description class
     * @param mainContent article mainContent class
     * @param picture article picture class
     * @param thumbnail article thumbnail class
     * @param publishedTime article publishedTime class
     */
    public Scraper( String source,
                    MainContentFilter sanitizer,
                    String defaultThumbnail,
                    String title,
                    String author,
                    String category,
                    String description,
                    String mainContent,
                    String picture,
                    String thumbnail,
                    String publishedTime) {
        this.SOURCE = source;
        this.sanitizer = sanitizer;
        this.DEFAULT_THUMBNAIL = defaultThumbnail;
        this.TITLE = title;
        this.AUTHOR = author;
        this.CATEGORY = category;
        this.DESCRIPTION = description;
        this.MAIN_CONTENT = mainContent;
        this.PICTURE = picture;
        this.THUMBNAIL = thumbnail;
        this.PUBLISHED_TIME = publishedTime;
    }

    /** Get Article all info (title, description, mainContent, thumbnail, categories, ...)
     * @param url article url
     * @return article all related information
     */
    public Article getArticle(URL url){
        Document doc = ScrapingUtils.getDocumentAndDeleteCookies(url);
        if (doc == null){
            return null;
        }

        String title = scrapeTitle(doc);
        String description = scrapeDescription(doc);
        Element mainContent = scrapeMainContent(doc);
        String thumbNail = scrapeThumbnail(doc);
        Set<String> categories = scrapeCategoryNames(doc);
        LocalDateTime publishedTime = scrapePublishedTime(doc);

        if (StringUtils.isEmpty(title)
                || StringUtils.isEmpty(description)
                || mainContent == null
                || publishedTime == null){
            return null;
        }

        return ArticleFactory.createArticle(SOURCE,
                url.toString(),
                title,
                description,
                mainContent,
                thumbNail,
                categories, publishedTime);

    }

    /** Get author of an article
     * @param doc article document
     * @return author element
     */
    private Element scrapeAuthor(Document doc){
        Element author = getFirstElementByClass(doc, this.AUTHOR);
        if (author == null){
            return null;
        }

        for (Element a: author.getElementsByTag("a")){
            if (!StringUtils.isEmpty(a.ownText())){
                return new Element("p")
                        .attr("style", "text-align:right;")
                        .addClass("author")
                        .appendChild(new Element("strong").text(a.ownText()));
            }
        }

        for (Element p: author.getElementsByTag("p")){
            if (!StringUtils.isEmpty(p.ownText())){
                return new Element("p")
                        .attr("style", "text-align:right;")
                        .addClass("author")
                        .appendChild(new Element("strong").text(p.ownText()));
            }
        }

        return null;
    }

    /** Scrape Article Title Element that matches provided css
     * @param doc article document
     * @return sanitized title element
     */
    public String scrapeTitle(Document doc) {
        Element title = getFirstElementByClass(doc, TITLE);
        if (title == null){
            return "";
        }
        return title.text();
    }

    /** Scrape Article Description Element that matches provided css
     * @param doc article document
     * @return sanitized title element
     */
    public String scrapeDescription(Document doc) {
        Element description = getFirstElementByClass(doc, DESCRIPTION);
        if (description == null) {
            return "";
        }

        // some description has span tag for location, add whitespace to text for readability
        for (Element span : description.getElementsByTag("span")){
            span.text(span.text() + " ");
        }

        String cleanHtml = Jsoup.clean(description.html(), Safelist.simpleText());

        // only get the text inside clean html
        // this is a trick to get text node in a html string
        // create an arbitrary tag and set its html as the html string
        // call text() method on the tag to get text of the html string
        // this way tags are eliminated and only text is returned
        return new Element("p").html(cleanHtml).text();
    }

    /** Scrape Article Main content Element that matches provided css
     * @param doc article document
     * @return sanitized title element
     */
    public Element scrapeMainContent(Document doc) {
        Element content = getFirstElementByClass(doc, MAIN_CONTENT);
        if (content == null || content.html().length() < 10) {
            // if content has less than 10 character, it's probably bogus
            return null;
        }
        content.tagName("main");
        Element authorTag = scrapeAuthor(doc);
        if (authorTag != null){
            content.append(authorTag.outerHtml());
        }
        return sanitizer.sanitizeMainContent(content);
    }

    /** Scrape Article Thumbnail url
     * @param doc article document
     * @return thumbnail url string
     */
    public String scrapeThumbnail(Document doc) {
        String thumbFromCls = ScrapingUtils.scrapeFirstImgUrlFromClass(doc, THUMBNAIL);
        String thumbFromID = ScrapingUtils.scrapeFirstImgUrlFromID(doc, THUMBNAIL);
        String firstPic = ScrapingUtils.scrapeFirstImgUrlFromClass(doc, PICTURE);

        if (!StringUtils.isEmpty(thumbFromCls)){
            return thumbFromCls;
        }
        else if (!StringUtils.isEmpty(thumbFromID)){
            // sometimes provided THUMBNAIL can be of an ID
            return thumbFromID;
        }
        else if (!StringUtils.isEmpty(firstPic)){
            return firstPic;
        }
        else {
            return DEFAULT_THUMBNAIL;
        }
    }

    /** Scrape Article Published Time in meta tag
     * @param doc article document
     * @param lookupAttribute itemprop attribute in meta tag
     * @return Article Published Time
     */
    private LocalDateTime scrapePublishedTimeFromMeta(Document doc, String lookupAttribute){
        Element time = ScrapingUtils.getFirstElementByMatchingValue(doc, "meta", lookupAttribute);
        if (time == null) {
            return null;
        }

        for (Attribute a: time.attributes()){
            if(a.getValue().contains("+07")){
                return LocalDateTimeParser.parse(a.getValue());
            }
        }

        return null;
    }

    /** Scrape Article Published Time in body
     * @param doc article document
     * @param css css class
     * @return Article Published Time
     */
    private LocalDateTime scrapePublishedTimeInBody(Document doc, String css){
        Elements dateTimeTags = doc.getElementsByClass(css);
        for (Element dateTimeTag :dateTimeTags){
            LocalDateTime time = LocalDateTimeParser.parse(dateTimeTag);
            if (time != null){
                return time;
            }
        }
        return null;
    }

    /** Scrape Article Published Time
     * @param doc article document
     * @return article Published Time
     */
    public LocalDateTime scrapePublishedTime(Document doc){
        // first look for published time in meta tag
        LocalDateTime time = scrapePublishedTimeFromMeta(doc, PUBLISHED_TIME);

        // look for published time in body if no time tag is found in meta
        if (time == null){
            time = scrapePublishedTimeInBody(doc, PUBLISHED_TIME);
        }
        return time;
    }

    public Set<String> scrapeCategoryNames(Document doc) {
        Set<String> categoryList = new HashSet<>();
        Element breadcrumb = getFirstElementByClass(doc, CATEGORY);
        if (breadcrumb != null){
            // breadcrumb contains links to categories
            Elements linkTags = breadcrumb.getElementsByTag("a");
            for(Element link: linkTags) {
                // name of category is the text of its link
                String category = link.text();

                // convert from Vietnamese to English name
                category = Category.translateToEnglish(category);
                categoryList.add(category);
            }
        }
        return categoryList;
    }
}

