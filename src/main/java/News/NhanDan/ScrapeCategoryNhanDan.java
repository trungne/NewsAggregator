package News.NhanDan;

import Scraper.CategoryScrapable;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class ScrapeCategoryNhanDan implements CategoryScrapable {
    @Override
    public String getCategory(Document doc) {
        Element tag = doc.selectFirst(".bc-item");
        if (tag == null) return "";
        if (tag.text().isEmpty()) return "";
        return tag.text();
    }
}
