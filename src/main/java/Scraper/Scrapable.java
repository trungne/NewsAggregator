package Scraper;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.Collection;

public interface Scrapable {
    Element getTitleTag(Document doc);
    Element getDescriptionTag(Document doc);
    Element getMainContent(Document doc);
    Element getThumbnail(Document doc);
    LocalDateTime getPublishedTime(Document doc);
    String getCategory(Document doc);
    Collection<URL> getLinksFromCategory(String category);
}