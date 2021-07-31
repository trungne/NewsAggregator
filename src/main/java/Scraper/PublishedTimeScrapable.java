package Scraper;
import org.jsoup.nodes.Document;
import java.time.LocalDateTime;

public interface PublishedTimeScrapable {
    LocalDateTime getLocalDateTime(Document doc, String property);
}