package News.VNExpress;

import Scraper.CategoryScrapable;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class ScrapeCategoryVNExpress implements CategoryScrapable {
    @Override
    public String getCategory(Document doc) {
        Element tag = doc.getElementsByAttributeValue("name", "tt_site_id_detail").first();
        if (tag == null) return "";
        if (tag.attr("catename").isEmpty()) return "";
        return tag.attr("catename");
    }
}
