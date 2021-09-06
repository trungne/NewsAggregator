package Business.Scraper.Helper;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.*;
import java.util.HashSet;
import java.util.Set;

public class ScrapingUtils {
    public final static int MAX_WAIT_TIME_WHEN_ACCESS_URL = 5000; // ms
    public final static int MAX_TERMINATION_TIME = 15000; // ms
    public final static int MAX_ARTICLES_DISPLAYED = 50;

    public static Document getDocumentAndDeleteCookies(String url){
        Connection connection = Jsoup.connect(url).timeout(MAX_WAIT_TIME_WHEN_ACCESS_URL);
        try {
            Document doc = connection.get();
            connection.cookieStore().removeAll();
            return doc;
        } catch (MalformedURLException err){
            System.out.println("MalformedURLException:" +  url);
        }
        catch (IOException e) {
            System.out.println("IOException:" +  url);
        }
        return null;
    }

    /** Get the first image url of a tag with a specific css class
     * @param doc document to parse
     * @param cls css class to target
     * @return the url of the first image found, return empty string if no image found.
     * */
    public static String scrapeFirstImgUrlFromClass(Document doc, String cls){
        Element firstElementOfClass = scrapeFirstElementByClass(doc, cls);
        if (firstElementOfClass != null){
            // return the first url link found
            for (Element imgTag: firstElementOfClass.getElementsByTag("img")){
                // some news outlets store url of img in data-src
                // if the img tag doesn't have data-src attr, check its src attr
                String urlInDataSrc = imgTag.attr("data-src");
                String urlInSrc = imgTag.attr("src");

                if (!StringUtils.isEmpty(urlInDataSrc)){
                    return urlInDataSrc;
                }
                else if (!StringUtils.isEmpty(urlInSrc)){
                    return urlInSrc;
                }
            }
        }
        return "";
    }


    public static String scrapeFirstImgUrl(Document doc, String cls){
        String url = scrapeFirstImgUrlFromClass(doc, cls);
        if (StringUtils.isEmpty(url)){

        }

        return url;
    }
    public static Element scrapeFirstElementByClass(Document doc, String cls){
        if (StringUtils.isEmpty(cls)){
            return null;
        }

        String query = cls;
        if (!query.startsWith(".")) {
            query = "." + query;
        }
        Element temp = doc.selectFirst(query);
        if (temp != null){
            return new Element("div").html(temp.outerHtml());
        }
        else{
            return null;
        }

    }
    /**  Target all elements with provided css class, pull out all URLs in a tag.
     * @param baseUrl: URL to parse and also provide the base in case of relative URLs are scraped
     * @param cssClass: target Element that has URL
     * */
    public static Set<URL> scrapeLinksByClass(URL baseUrl, String cssClass) {
        Document doc = getDocumentAndDeleteCookies(baseUrl.toString());
        if (doc == null){
            return new HashSet<>();
        }
        Set<URL> links = new HashSet<>();
        Elements titleTags = doc.getElementsByClass(cssClass);
        // target all title tags and pull out links for articles
        for (Element tag : titleTags) {
            // link is stored in href attribute of <a> tag
            try {
                URL link = new URL(baseUrl, tag.getElementsByTag("a").attr("href"));
                links.add(link);
            } catch (MalformedURLException ignored) {
//                System.out.println("Cookies error");
            }
        }
        return links;
    }

    /** From a provided img tag, create a new img with the src and alt of it.
     * @return null if the parameter is not an img tag or no src is found, otherwise return an image tag with src and alt (if there is any)
     * */
    public static Element createCleanImgTag(Element imgTag) {
        if (!imgTag.tagName().equals("img")) return null;

        Element cleanedFirstImgTag = new Element("img");

        // assign src for the img tag
        // TODO: maybe check valid src? end with .jpg png??
        if (!StringUtils.isEmpty(imgTag.attr("data-src")))
            cleanedFirstImgTag.attr("src", imgTag.attr("data-src"));
        else if (!StringUtils.isEmpty(imgTag.attr("src")))
            cleanedFirstImgTag.attr("src", imgTag.attr("src"));
        else
            return null;

        // assign alt for the img tag
        if (!StringUtils.isEmpty(imgTag.attr("alt"))) {
            cleanedFirstImgTag.attr("alt", imgTag.attr("alt"));
        }

        // only return img tag that has src
        return cleanedFirstImgTag;
    }
}

