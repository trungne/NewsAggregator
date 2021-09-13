package Business.Scraper.Helper;

import static java.util.Objects.isNull;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

public class ScrapingUtils {
    public final static int MAX_WAIT_TIME_WHEN_ACCESS_URL = 7000; // ms
    public final static int MAX_TERMINATION_TIME = 15000; // ms
    public final static int MAX_ARTICLES_DISPLAYED = 50;

    public static Document getDocumentAndDeleteCookies(URL url){
        try {
            return Jsoup.parse(url, MAX_WAIT_TIME_WHEN_ACCESS_URL);
        } catch (IOException ignored){
            return null;
        }
    }

    public static String extractImgUrlFromTag(Element e){
        for (Element imgTag: e.getElementsByTag("img")){
            // some news outlets store url of img in data-src
            // if the img tag doesn't have data-src attr, check its src attr
            String urlInDataSrc = imgTag.attr("data-src");
            String urlInSrc = imgTag.attr("src");

            if (StringUtils.isNotEmpty(urlInDataSrc)){
                return urlInDataSrc;
            }
            else if (StringUtils.isNotEmpty(urlInSrc)){
                return urlInSrc;
            }
        }
        return "";
    }

    /** Get the first image url of a tag with a specific css class
     * @param doc document to parse
     * @param cls css class to target
     * @return the url of the first image found, return empty string if no image found.
     * */
    public static String scrapeFirstImgUrlFromClass(Document doc, String cls){
        if (StringUtils.isEmpty(cls)){
            return "";
        }

        for (Element e : doc.getElementsByClass(cls)){
            String url = extractImgUrlFromTag(e);
            if (StringUtils.isNotEmpty(url)){
                return url;
            }
        }

        return "";
    }

    public static String scrapeFirstImgUrlFromID(Document doc, String id){
        if (StringUtils.isEmpty(id)){
            return "";
        }

        Element tag = doc.getElementById(id);
        if (tag != null){
            return extractImgUrlFromTag(tag);
        }

        return "";
    }

    public static Element getFirstElementByClass(Document doc, String cls){
        if (StringUtils.isEmpty(cls)){
            return null;
        }

        String query = cls;
        if (!query.startsWith(".")) {
            query = "." + query;
        }
        Element temp = doc.selectFirst(query);
        if (temp != null) {
            // return a new element with the content of the target tag to avoid
            // unwanted linking to document
            return new Element("div").html(temp.outerHtml());
        }
        else {
            return null;
        }
    }

    /**  Target all elements with provided css class, pull out all URLs in a tag.
     * @param baseUrl: URL to parse and also provide the base in case of relative URLs are scraped
     * @param cssClass: target Element that has URL
     * */
    public static Set<URL> getLinksByClass(URL baseUrl, String cssClass) {
        Document doc = getDocumentAndDeleteCookies(baseUrl);
        if (doc == null){
            return new HashSet<>();
        }
        Set<URL> links = new HashSet<>();
        Elements titleTags = doc.getElementsByClass(cssClass);
        // target all title tags and pull out links for articles
        for (Element tag : titleTags) {
            // link is stored in href attribute of <a> tag
            try {
                if (StringUtils.isEmpty(tag.getElementsByTag("a").attr("href"))){
                    continue;
                }
                URL link = new URL(baseUrl, tag.getElementsByTag("a").attr("href"));

                if (link.getPath().contains("/video")){
                    continue;
                }

                links.add(link);
            } catch (MalformedURLException ignored) {
            }
        }
        return links;
    }
    
    /**
     * From a provided img tag, create a new img with the src and alt of it.
     *
     * @return null if the parameter is not an img tag or no src is found, otherwise return an image tag with src and alt (if there is any)
     */
    public static Element createCleanImgTag(Element imgTag) {
        if (isNull(imgTag) || !StringUtils.equals("img", imgTag.tagName())) {
            return null;
        }
        
        Element cleanedFirstImgTag = new Element("img");

        // assign src for the img tag
        if (StringUtils.isNotEmpty(imgTag.attr("data-src")))
            cleanedFirstImgTag.attr("src", imgTag.attr("data-src"));
        else if (StringUtils.isNotEmpty(imgTag.attr("src")))
            cleanedFirstImgTag.attr("src", imgTag.attr("src"));
        else
            return null;

        // assign alt for the img tag
        if (StringUtils.isNotEmpty(imgTag.attr("alt"))) {
            cleanedFirstImgTag.attr("alt", imgTag.attr("alt"));
        }

        // only return img tag that has src
        return cleanedFirstImgTag;
    }

    /** Get first element of a tag that has a particular value
     * @param doc document of the site
     * @param tagName name of the tag to target
     * @param value matching value to look for
     * @return First element of the tag that has the specified value, null if not found
     * */
    public static Element getFirstElementByMatchingValue(Document doc, String tagName, String value){
        Elements tags = doc.getElementsByTag(tagName);
        for (Element tag: tags){
            for (Attribute a: tag.attributes()){
                if(a.getValue().equals(value)){
                    return tag;
                }
            }
        }
        return null;
    }
}

