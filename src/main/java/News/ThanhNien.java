package News;

import News.Sanitizer.HtmlSanitizer;
import News.Sanitizer.TuoiTreSanitizer;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.time.LocalDateTime;
import java.util.HashMap;

public class ThanhNien extends NewsOutlet{
    public static NewsOutlet init(){
        HashMap<String, String> categories = new HashMap<>();
        categories.put(CATEGORY.COVID, CATEGORY.THANHNIEN_COVID);
        categories.put(CATEGORY.POLITICS, CATEGORY.THANHNIEN_POLITICS);
        categories.put(CATEGORY.BUSINESS, CATEGORY.THANHNIEN_BUSINESS);
        categories.put(CATEGORY.TECHNOLOGY, CATEGORY.THANHNIEN_TECHNOLOGY);
        categories.put(CATEGORY.HEALTH, CATEGORY.THANHNIEN_HEALTH);
        categories.put(CATEGORY.SPORTS, CATEGORY.THANHNIEN_SPORTS);
        categories.put(CATEGORY.ENTERTAINMENT, CATEGORY.THANHNIEN_ENTERTAINMENT);
        categories.put(CATEGORY.WORLD, CATEGORY.THANHNIEN_WORLD);
        Categories ThanhNienCategories = new Categories(categories);
        CssConfiguration ThanhNienCssConfig = new CssConfiguration(
                "https://thanhnien.vn/",
                CSS.THANHNIEN_TITLE_LINK,
                CSS.THANHNIEN_TITLE,
                CSS.THANHNIEN_DESCRIPTION,
                CSS.THANHNIEN_BODY,
                CSS.THANHNIEN_TIME,
                CSS.THANHNIEN_PIC);
        return new ThanhNien("Thanh Nien",
                "https://static.thanhnien.vn/v2/App_Themes/images/logo-tn-2.png",
                ThanhNienCategories,
                ThanhNienCssConfig,
                new TuoiTreSanitizer());
    }

    public ThanhNien(String name, String defaultThumbnail, Categories categories, CssConfiguration cssConfiguration, HtmlSanitizer sanitizer) {
        super(name, defaultThumbnail, categories, cssConfiguration, sanitizer);
    }

    @Override
    public LocalDateTime getPublishedTime(Document doc) {
        Elements dateTimeTag = doc.getElementsByAttributeValue("property", cssConfiguration.publishedTime);

        String dateTimeStr = dateTimeTag.attr("content");
        if (dateTimeStr.isEmpty() || dateTimeStr.isBlank()){
            return LocalDateTime.now();
        }
        return Helper.parseLocalDateTime.parse(dateTimeStr);
    }

    @Override
    public String getCategory(Document doc) {
        Element tag = doc.getElementsByAttributeValue("property", "article:section").first();
        if (tag == null) return "";
        if (tag.attr("content").isEmpty()) return "";
        return tag.attr("content");
    }
}
