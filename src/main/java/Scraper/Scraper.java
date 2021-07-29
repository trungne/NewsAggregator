package Scraper;

import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.*;
import java.net.*;

import java.util.*;

public class Scraper {
    private static final int MAX_WAIT_TIME = 5000; // ms
    static final int MAX_LINKS_SCRAPED = 12;

    // TODO this shit crashes my computer
    public static String getBodyHtml(URL url){
        String chromeDriverPath = "E:\\Coding\\Java\\chromedriver_win32\\chromedriver.exe";
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless", "--disable-gpu", "--window-size=1920,1200","--ignore-certificate-errors");
        WebDriver driver = new ChromeDriver(options);
        driver.get(url.toString());

        WebElement element = driver.findElement(By.tagName("body"));
        System.out.println(element.getAttribute("innerHtml"));
        return element.getAttribute("innerHtml");
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


    /* Create a new img with the src and alt of an img tag
     * Return null if the parameter is not an img tag
     * Return null if the no src is found
     * */
    public static Element createCleanImgTag(Element imgTag){
        if (!imgTag.tagName().equals("img")) return null;

        Element cleanedFirstImgTag = new Element("img");

        // assign src for the img tag
        // TODO: maybe check valid src? end with .jpg png??
        if (!imgTag.attr("data-src").isEmpty())
            cleanedFirstImgTag.attr("src",imgTag.attr("data-src"));
        else if (!imgTag.attr("src").isEmpty())
            cleanedFirstImgTag.attr("src",imgTag.attr("src"));
        else
            return null;

        // assign alt for the img tag
        if (!imgTag.attr("alt").isEmpty()){
            cleanedFirstImgTag.attr("alt", imgTag.attr("alt"));
        }

        // only return img tag that has src
        return cleanedFirstImgTag;
    }

    public static Element scrapeCaption(Element caption){
        return null;
    }
}



