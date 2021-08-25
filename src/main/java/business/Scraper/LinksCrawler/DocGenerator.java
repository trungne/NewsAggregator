package business.Scraper.LinksCrawler;

import org.jsoup.nodes.Document;

import java.io.IOException;

public interface DocGenerator {
    Document getDocument(String url) throws IOException;
}
