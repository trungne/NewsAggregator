package business.Scraper.LinksCrawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class JSoupGenerator implements DocGenerator{

    @Override
    public Document getDocument(String url) throws IOException {
        return Jsoup.connect(url).get();
    }
}
