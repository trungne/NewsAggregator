package Business.Scraper.ArticleCrawler;

import Business.Scraper.Helper.LocalDateTimeParser;
import Business.Scraper.LinksCrawler.Category;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static Business.Scraper.Helper.ScrapingUtils.scrapeFirstElementByClass;
import static Business.Scraper.Helper.ScrapingUtils.scrapeFirstImgUrl;

public class Scraper {
    private final String TITLE;
    private final String AUTHOR;
    private final String DESCRIPTION;
    private final String MAIN_CONTENT;
    private final String CATEGORY;
    private final String PICTURE;
    private final String THUMBNAIL;
    private final String PUBLISHED_TIME;

    public Scraper( String title,
                    String author,
                    String category,
                    String description,
                    String mainContent,
                    String picture,
                    String thumbnail,
                    String publishedTime) {
        this.TITLE = title;
        this.AUTHOR = author;
        this.CATEGORY = category;
        this.DESCRIPTION = description;
        this.MAIN_CONTENT = mainContent;
        this.PICTURE = picture;
        this.THUMBNAIL = thumbnail;
        this.PUBLISHED_TIME = publishedTime;
    }

    private Element scrapeAuthor(Document doc){
        Element author = scrapeFirstElementByClass(doc, this.AUTHOR);
        if (author != null){
            return new Element("p")
                    .attr("style", "text-align:right;")
                    .appendChild(new Element("strong").text(author.text()));
        }
        else{
            return null;
        }
    }

    // by default, scrape the first element that matches provided css
    public Element scrapeTitle(Document doc) {
        return scrapeFirstElementByClass(doc, this.TITLE);
    }

    // by default, scrape the first element that matches provided css
    public Element scrapeDescription(Document doc) {
        return scrapeFirstElementByClass(doc, DESCRIPTION);
    }

    // by default, scrape the first element that matches provided css
    public Element scrapeMainContent(Document doc) {
        Element content = scrapeFirstElementByClass(doc, MAIN_CONTENT);
        if (content == null) {
            return null;
        }

        Element authorTag = scrapeAuthor(doc);
        if (authorTag != null){
            content.append(authorTag.outerHtml());
        }
        return content;
    }

    // by default, set the first img as the thumbnail
    public String scrapeThumbnail(Document doc) {
        String url = scrapeFirstImgUrl(doc, THUMBNAIL);

        if (StringUtils.isEmpty(url)){
            url = scrapeFirstImgUrl(doc, PICTURE);
        }
        return url;
    }
    private Element getTimeTagInMeta(Document doc, String key){
        Elements metaTags = doc.getElementsByTag("meta");
        for (Element meta: metaTags){
            for (Attribute a: meta.attributes()){
                if(a.getValue().equals(key)){
                    return meta;
                }
            }
        }
        return null;
    }

    private LocalDateTime scrapePublishedTimeFromMeta(Document doc, String key){
        Element time = getTimeTagInMeta(doc, key);
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

    public LocalDateTime scrapePublishedTime(Document doc){
        LocalDateTime time = scrapePublishedTimeFromMeta(doc, PUBLISHED_TIME);
        if (time == null){
            time = scrapePublishedTimeInBody(doc, PUBLISHED_TIME);
        }
        return time;
    }

    public Set<String> scrapeCategoryNames(Document doc) {
        Set<String> categoryList = new HashSet<>();
        Element breadcrumb = scrapeFirstElementByClass(doc, CATEGORY);
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

