package Scraper;

import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

import java.io.*;
import java.net.*;

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
    public static Element scrapeFirstElementByClass(Document doc, String uniqueCssClass) {
        String queryString = uniqueCssClass;

        if (!queryString.startsWith(".")) {
            queryString = "." + uniqueCssClass;
        }

        return doc.selectFirst(queryString);
    }

    public static Element scrapeFirstImgTagByClass(Document doc, String uniqueCssClass) {
        Element elementContainsImgs = scrapeFirstElementByClass(doc, uniqueCssClass);
        if (elementContainsImgs == null) return null;

        Element firstImgTag = elementContainsImgs.getElementsByTag("img").first();
        if (firstImgTag == null) return null;

        return createCleanImgTag(firstImgTag);
    }

    public static Element createCleanImgTag(Element e){
        Element cleanedFirstImgTag = new Element("img");

        // assign src for the img tag
        // TODO: maybe check valid src? end with .jpg png??
        if (e.hasAttr("data-src"))
            cleanedFirstImgTag.attr("src",e.attr("data-src"));
        else if (e.hasAttr("src"))
            cleanedFirstImgTag.attr("src",e.attr("src"));
        else
            return null;

        // assign alt for the img tag
        if (e.hasAttr("alt")){
            cleanedFirstImgTag.attr("alt", e.attr("alt"));
        }

        // only return img tag that has src
        return cleanedFirstImgTag;
    }
}



