package Scraper;

import org.jsoup.nodes.Document;

import java.time.LocalDateTime;

public interface ScrapingDateTimeBehavior {
    LocalDateTime getLocalDateTime(Document doc, String propertyContainsDateTimeInfo);
}
