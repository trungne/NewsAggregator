package business.Scraper.LinksCrawler;

import org.jsoup.nodes.Document;

import java.io.IOException;

public interface DocGenerator {
    // TODO: Trung comments this
    Document getDocument(String url) throws IOException;
}
