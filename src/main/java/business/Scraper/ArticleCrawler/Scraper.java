package business.Scraper.ArticleCrawler;

import business.Sanitizer.Sanitizable;
import business.Scraper.LinksCrawler.LinksCrawler;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.net.URL;
import java.util.Set;

public abstract class Scraper implements Scrapable, Sanitizable {
    // TODO: Thai comments this
    public static String toName(URL url) {
        String host = url.getHost();
        switch (host) {
            case "vnexpress.net": {
                return "VNExpress";
            }
            case "zingnews.vn": {
                return "ZingNews";
            }
            case "tuoitre.vn": {
                return "Tuoi Tre";
            }
            case "nhandan.vn": {
                return "Nhan Dan";
            }
            case "thanhnien.vn": {
                return "Thanh Nien";
            }
            default:
                return "";
        }
    }

    protected final String name;
    protected final String defaultThumbnail;
    protected final CssConfiguration cssConfiguration;
    protected final LinksCrawler linksCrawler;

    public Scraper(String name,
                   String defaultThumbnail,
                   CssConfiguration cssConfiguration,
                   LinksCrawler linksCrawler) {
        this.name = name;
        this.defaultThumbnail = defaultThumbnail;
        this.cssConfiguration = cssConfiguration;
        this.linksCrawler = linksCrawler;
    }

    // by default, scrape the first element that matches provided css
    public Element scrapeTitle(Document doc) throws ElementNotFound {
        Element title = scrapeFirstElementByClass(doc, cssConfiguration.title);
        if (title == null) {
            throw new ElementNotFound();
        }
        return sanitizeTitle(title);
    }

    // by default, scrape the first element that matches provided css
    public Element scrapeDescription(Document doc) throws ElementNotFound {
        Element desp = scrapeFirstElementByClass(doc, cssConfiguration.description);
        if (desp == null) {
            throw new ElementNotFound();
        }
        return sanitizeDescription(desp);
    }

    // by default, scrape the first element that matches provided css
    public Element scrapeMainContent(Document doc) throws ElementNotFound {
        Element content = scrapeFirstElementByClass(doc, cssConfiguration.mainContent);
        if (content == null) {
            throw new ElementNotFound();
        }
        return sanitizeMainContent(content);
    }

    // by default, set the first img as the thumbnail
    public String scrapeThumbnail(Document doc) {
        String url = scrapeFirstImgUrl(doc, cssConfiguration.picture);
        if (StringUtils.isEmpty(url)){
            return getDefaultThumbnail();
        }
        else{
            return url;
        }
    }

    protected String getDefaultThumbnail() {
        return defaultThumbnail;
    }

    public Set<URL> getLinksFromCategory(String categoryName) {
        return linksCrawler.getArticleLinks(categoryName);
    }

    static class CssConfiguration {
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
}

