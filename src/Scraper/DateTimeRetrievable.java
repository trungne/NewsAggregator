package Scraper;

import org.jsoup.nodes.Document;

import java.time.LocalDateTime;

public interface DateTimeRetrievable {
    String getDateTimeString(Document doc, String propertyContainsDateTimeInfo);
    LocalDateTime getLocalDateTime(String dateTimeStr);
}