package business.Helper;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

public class ScrapingUtils {
    public final static int MAX_LINKS_SCRAPED_IN_A_PAGE = 15;
    public final static int MAX_WAIT_TIME_WHEN_ACCESS_URL = 5000; // ms
    public final static int MAX_TERMINATION_TIME = 15000; // ms
    public final static int MAX_ARTICLES_PER_SOURCE = 10;
    public final static int MAX_ARTICLES_DISPLAYED = 50;

    /**  Target all elements with provided css class, pull out all URLs in a tag.
     * @param baseUrl: URL to parse and also provide the base in case of relative URLs are scraped
     * @param cssClass: target Element that has this class and pull out URL from tag
     * */
    public static Set<URL> scrapeLinksByClass(URL baseUrl, String cssClass) {
        Document doc;
        Set<URL> links = new HashSet<>();
        try {
            doc = Jsoup
                    .connect(baseUrl.toString())
                    .timeout(MAX_WAIT_TIME_WHEN_ACCESS_URL)
                    .get();
            Elements titleTags = doc.getElementsByClass(cssClass);
            // target all title tags and pull out links for articles
            for (Element tag : titleTags) {
                if (links.size() > MAX_LINKS_SCRAPED_IN_A_PAGE) {
                    return links;
                }
                // link is stored in href attribute of <a> tag
                URL link = new URL(baseUrl, tag.getElementsByTag("a").attr("href"));
                links.add(link);
            }
        } catch (IOException e) {
            e.printStackTrace();
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
