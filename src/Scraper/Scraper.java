package Scraper;

import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

import java.io.*;
import java.net.*;
import java.time.LocalDateTime;

import java.util.*;

public class Scraper {
    static final int MAX_LINKS_SCRAPED = 12;

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
    public static Element scrapeElementByClass(Document doc, String uniqueCssClass) {
        String queryString = uniqueCssClass;
        if (!queryString.startsWith(".")) {
            queryString = "." + uniqueCssClass;
        }
        return doc.selectFirst(queryString);
    }

    public static Element scrapeCleanedFirstImgTagByClass(Document doc, String uniqueCssClass) {
        Element firstImgTag = scrapeElementByClass(doc, uniqueCssClass);
        firstImgTag = firstImgTag.getElementsByTag("img").first();
        // return an empty img tag if nothing to be scraped
        Element cleanedFirstImgTag = new Element("img");

        if (firstImgTag != null) {

            // assign src for the img tag
            if (firstImgTag.hasAttr("data-src")) {
                cleanedFirstImgTag.attr("src",firstImgTag.attr("data-src"));
            } else if (firstImgTag.hasAttr("src")) {
                cleanedFirstImgTag.attr("src",firstImgTag.attr("src"));
            }

            // assign alt for the img tag
            if (firstImgTag.hasAttr("alt")){
                cleanedFirstImgTag.attr("alt", firstImgTag.attr("alt"));
            }

        }

        return cleanedFirstImgTag;
    }
}



