package Scraper;
import org.jsoup.nodes.Document;

public interface CategoryScrapable {
    String getCategory(Document doc);
}
