package business.Helper;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

import java.io.*;
import java.net.*;

import java.util.*;

public class Scraper {
    static final int MAX_LINKS_SCRAPED = 20;
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
    /* Create a new img with the src and alt of an img tag
     * Return null if the parameter is not an img tag
     * Return null if the no src is found
     * */
    public static Element createCleanImgTag(Element imgTag){
        if (!imgTag.tagName().equals("img")) return null;

        Element cleanedFirstImgTag = new Element("img");

        // assign src for the img tag
        // TODO: maybe check valid src? end with .jpg png??
        if (!StringUtils.isEmpty(imgTag.attr("data-src")))
            cleanedFirstImgTag.attr("src",imgTag.attr("data-src"));
        else if (!StringUtils.isEmpty(imgTag.attr("src")))
            cleanedFirstImgTag.attr("src",imgTag.attr("src"));
        else
            return null;

        // assign alt for the img tag
        if (!StringUtils.isEmpty(imgTag.attr("alt"))){
            cleanedFirstImgTag.attr("alt", imgTag.attr("alt"));
        }

        // only return img tag that has src
        return cleanedFirstImgTag;
    }
}



