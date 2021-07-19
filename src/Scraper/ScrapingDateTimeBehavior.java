package Scraper;

import org.jsoup.nodes.Document;

import java.time.LocalDateTime;

public interface ScrapingDateTimeBehavior {
    String getDateTimeString(Document doc, String propertyContainsDateTimeInfo);
    LocalDateTime getLocalDateTime(String dateTimeStr);
}
