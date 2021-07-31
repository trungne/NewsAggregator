package News.ZingNews;

import Scraper.CategoryScrapable;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class ScrapeCategoryZingNews implements CategoryScrapable {

    @Override
    public String getCategory(Document doc) {
        Element tag = doc.selectFirst(".the-article-category");
        if (tag == null) return "";
        if (tag.text().isEmpty()) return "";
        return tag.text();
    }
}
