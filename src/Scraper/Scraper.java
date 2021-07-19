package Scraper;

import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

import java.io.*;
import java.net.*;
import java.time.LocalDateTime;

import java.util.*;

public class Scraper {
    static final int MAX_LINKS_SCRAPED = 2;
    private final Document doc;

    public Scraper(Document doc) {
        this.doc = doc;
    }

    public Scraper(String url) throws IOException {
        this.doc = Jsoup.connect(url).get();
    }

    public static ArrayList<URL> scrapeLinksByClass(URL baseUrl, String cssClass) {
        Document doc;
        ArrayList<URL> links = new ArrayList<>();
        try {
            doc = Jsoup.connect(baseUrl.toString()).get();
            Elements titleTags = doc.getElementsByClass(cssClass);
            // target all title tags and pull out links for articles
            for (Element tag : titleTags) {
                if (links.size() > MAX_LINKS_SCRAPED) return links;
                // link is stored in href attribute of <a> tag
                URL link = new URL(baseUrl, tag.getElementsByTag("a").attr("href"));
                links.add(link);
            }

        } catch (IOException e) {
            // TODO: disable this in production
            e.printStackTrace();
        }
        return links;
    }

    // only scrape the first tag found!
    public Element scrapeElementByClass(String uniqueCssClass) {
        String queryString = uniqueCssClass;
        if (!queryString.startsWith(".")) {
            queryString = "." + uniqueCssClass;
        }
        return this.doc.selectFirst(queryString);
    }

    public String scrapeFirstImgUrlByClass(String uniqueCssClass) {
        String queryString = uniqueCssClass;
        if (!queryString.startsWith(".")) {
            queryString = "." + uniqueCssClass;
        }

        String thumbNailUrl = "";
        Element thumbNailTag = this.doc.selectFirst(queryString);

        if (thumbNailTag != null) {
            Element firstImgTag = thumbNailTag.getElementsByTag("img").first();

            if (firstImgTag != null) {
                if (firstImgTag.hasAttr("data-src")) {
                    thumbNailUrl = firstImgTag.attr("abs:data-src");
                } else if (firstImgTag.hasAttr("src")) {
                    thumbNailUrl = firstImgTag.attr("abs:src");
                }
            }
        }
        return thumbNailUrl;
    }

    public LocalDateTime scrapeDateTime(String uniqueDateTimeCssClass, ScrapingDateTimeBehavior scrapingDateTimeBehavior) {
        String dateTimeStr = scrapingDateTimeBehavior.getDateTimeString(this.doc, uniqueDateTimeCssClass);
        return scrapingDateTimeBehavior.getLocalDateTime(dateTimeStr);
    }
}



