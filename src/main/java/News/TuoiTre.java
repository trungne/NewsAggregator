package News;

import News.Sanitizer.HtmlSanitizer;
import News.Sanitizer.TuoiTreSanitizer;
import News.Sanitizer.ZingNewsSanitizer;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.time.LocalDateTime;
import java.util.HashMap;

public class TuoiTre extends NewsOutlet{
    public static final String TUOITRE_COVID = "https://tuoitre.vn/covid-19.html";
    public static final String TUOITRE_POLITICS = "https://tuoitre.vn/thoi-su.htm";
    public static final String TUOITRE_BUSINESS = "https://tuoitre.vn/kinh-doanh.htm";
    public static final String TUOITRE_TECHNOLOGY = "https://tuoitre.vn/khoa-hoc.htm";
    public static final String TUOITRE_HEALTH = "https://tuoitre.vn/suc-khoe.htm";
    public static final String TUOITRE_SPORTS = "https://tuoitre.vn/the-thao.htm";
    public static final String TUOITRE_ENTERTAINMENT = "https://tuoitre.vn/giai-tri.htm";
    public static final String TUOITRE_WORLD = "https://tuoitre.vn/the-gioi.htm";
    public static NewsOutlet init(){
        HashMap<String, String> categories = new HashMap<>();
        categories.put(CATEGORY.COVID, TUOITRE_COVID);
        categories.put(CATEGORY.POLITICS, TUOITRE_POLITICS);
        categories.put(CATEGORY.BUSINESS, TUOITRE_BUSINESS);
        categories.put(CATEGORY.TECHNOLOGY, TUOITRE_TECHNOLOGY);
        categories.put(CATEGORY.HEALTH, TUOITRE_HEALTH);
        categories.put(CATEGORY.SPORTS, TUOITRE_SPORTS);
        categories.put(CATEGORY.ENTERTAINMENT, TUOITRE_ENTERTAINMENT);
        categories.put(CATEGORY.WORLD, TUOITRE_WORLD);
        Categories TuoiTreCategories = new Categories(categories);
        CssConfiguration TuoiTreCssConfig = new CssConfiguration(
                "https://tuoitre.vn/",
                CSS.TUOITRE_TITLE_LINK,
                CSS.TUOITRE_TITLE,
                CSS.TUOITRE_DESCRIPTION,
                CSS.TUOITRE_BODY,
                CSS.TUOITRE_TIME,
                CSS.TUOITRE_PIC);
        return new TuoiTre("Tuoi Tre",
                "https://dangkyxettuyennghe.tuoitre.vn/img/logo-tt.png",
                TuoiTreCategories,
                TuoiTreCssConfig,
                new TuoiTreSanitizer());
    }

    public TuoiTre(String name, String defaultThumbnail, Categories categories, CssConfiguration cssConfiguration, HtmlSanitizer sanitizer) {
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
