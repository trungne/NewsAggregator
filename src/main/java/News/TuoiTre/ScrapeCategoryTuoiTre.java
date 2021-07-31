package News.TuoiTre;

import Scraper.CategoryScrapable;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class ScrapeCategoryTuoiTre implements CategoryScrapable {
    @Override
    public String getCategory(Document doc) {
        Element tag = doc.getElementsByAttributeValue("property", "article:section").first();
        if (tag == null) return "";
        if (tag.attr("content").isEmpty()) return "";
        return tag.attr("content");
    }
}
