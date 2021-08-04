package business.NewsSources;

import business.Helper.CATEGORY;
import business.Helper.CSS;
import business.Helper.LocalDateTimeParser;
import business.Sanitizer.HtmlSanitizer;
import business.Sanitizer.ThanhNienSanitizer;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.time.LocalDateTime;
import java.util.HashMap;

public class ThanhNien extends NewsOutlet{
    private static final String THANHNIEN_COVID = "https://thanhnien.vn/covid-19/";
    private static final String THANHNIEN_POLITICS = "https://thanhnien.vn/thoi-su/";
    private static final String THANHNIEN_BUSINESS = "https://thanhnien.vn/tai-chinh-kinh-doanh/";
    private static final String THANHNIEN_TECHNOLOGY = "https://thanhnien.vn/cong-nghe/";
    private static final String THANHNIEN_HEALTH = "https://thanhnien.vn/suc-khoe/";
    private static final String THANHNIEN_SPORTS = "https://thanhnien.vn/the-thao/";
    private static final String THANHNIEN_ENTERTAINMENT = "https://thanhnien.vn/giai-tri/";
    private static final String THANHNIEN_WORLD = "https://thanhnien.vn/the-gioi/";
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

        if (StringUtils.isEmpty(dateTimeStr)){
            return LocalDateTime.now();
        }
        return LocalDateTimeParser.parse(dateTimeStr);
    }

    @Override
    public String getCategory(Document doc) {
        Element tag = doc.getElementsByAttributeValue("property", "article:section").first();
        if (tag == null)
            return CATEGORY.OTHERS;

        String category = tag.attr("content");
        if (StringUtils.isEmpty(category))
            return CATEGORY.OTHERS;

        return category;
    }
}
