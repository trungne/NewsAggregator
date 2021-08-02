package BusinessLayer.NewsSources;

import BusinessLayer.Helper.CATEGORY;
import BusinessLayer.Helper.CSS;
import BusinessLayer.Helper.ParseLocalDateTime;
import BusinessLayer.Sanitizer.HtmlSanitizer;
import BusinessLayer.Sanitizer.ThanhNienSanitizer;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.time.LocalDateTime;
import java.util.HashMap;

public class ThanhNien extends NewsOutlet{
    public static final String THANHNIEN_COVID = "https://thanhnien.vn/covid-19/";
    public static final String THANHNIEN_POLITICS = "https://thanhnien.vn/thoi-su/";
    public static final String THANHNIEN_BUSINESS = "https://thanhnien.vn/tai-chinh-kinh-doanh/";
    public static final String THANHNIEN_TECHNOLOGY = "https://thanhnien.vn/cong-nghe/";
    public static final String THANHNIEN_HEALTH = "https://thanhnien.vn/suc-khoe/";
    public static final String THANHNIEN_SPORTS = "https://thanhnien.vn/the-thao/";
    public static final String THANHNIEN_ENTERTAINMENT = "https://thanhnien.vn/giai-tri/";
    public static final String THANHNIEN_WORLD = "https://thanhnien.vn/the-gioi/";
    public static NewsOutlet init(){
        HashMap<String, String> categories = new HashMap<>();
        categories.put(CATEGORY.COVID, THANHNIEN_COVID);
        categories.put(CATEGORY.POLITICS, THANHNIEN_POLITICS);
        categories.put(CATEGORY.BUSINESS, THANHNIEN_BUSINESS);
        categories.put(CATEGORY.TECHNOLOGY, THANHNIEN_TECHNOLOGY);
        categories.put(CATEGORY.HEALTH, THANHNIEN_HEALTH);
        categories.put(CATEGORY.SPORTS, THANHNIEN_SPORTS);
        categories.put(CATEGORY.ENTERTAINMENT, THANHNIEN_ENTERTAINMENT);
        categories.put(CATEGORY.WORLD, THANHNIEN_WORLD);
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
                new ThanhNienSanitizer());
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
        return ParseLocalDateTime.parse(dateTimeStr);
    }

    @Override
    public String getCategory(Document doc) {
        Element tag = doc.getElementsByAttributeValue("property", "article:section").first();
        if (tag == null) return "";
        if (tag.attr("content").isEmpty()) return "";
        return tag.attr("content");
    }
}
