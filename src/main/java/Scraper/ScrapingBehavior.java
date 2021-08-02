package Scraper;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.time.LocalDateTime;

import static Scraper.Scraper.createCleanImgTag;
import static Scraper.Scraper.scrapeFirstElementByClass;

public interface ScrapingBehavior {
    default Element getTitle(Document doc, String css){
        return scrapeFirstElementByClass(doc, css);
    }
    default Element getDescription(Document doc, String css){
        return scrapeFirstElementByClass(doc, css);
    }
    default Element getMainContent(Document doc, String css){
        return scrapeFirstElementByClass(doc, css);
    }

    default Element getThumbnail(Document doc, String css){
        Element elementContainsImgs = scrapeFirstElementByClass(doc, css);
        if (elementContainsImgs == null)
            return null;

        Element firstImgTag = elementContainsImgs.getElementsByTag("img").first();
        if (firstImgTag == null)
            return null;

        return createCleanImgTag(firstImgTag);
    }
    LocalDateTime getPublishedTime(Document doc, String css);
    String getCategory(Document doc, String css);


}
