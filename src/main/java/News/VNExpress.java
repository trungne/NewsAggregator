package News;

import News.Sanitizer.HtmlSanitizer;
import News.Sanitizer.VNExpressSanitizer;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.time.LocalDateTime;
import java.util.HashMap;

public class VNExpress extends NewsOutlet{
    public static final String VNEXPRESS_COVID =  "https://vnexpress.net/covid-19/tin-tuc";
    public static final String VNEXPRESS_POLITICS = "https://vnexpress.net/thoi-su/chinh-tri";
    public static final String VNEXPRESS_BUSINESS = "https://vnexpress.net/kinh-doanh";
    public static final String VNEXPRESS_TECHNOLOGY = "https://vnexpress.net/khoa-hoc";
    public static final String VNEXPRESS_HEALTH = "https://vnexpress.net/suc-khoe";
    public static final String VNEXPRESS_SPORTS = "https://vnexpress.net/the-thao";
    public static final String VNEXPRESS_ENTERTAINMENT = "https://vnexpress.net/giai-tri";
    public static final String VNEXPRESS_WORLD = "https://vnexpress.net/the-gioi";
    public static NewsOutlet init(){
        /* VNExpress */
        HashMap<String, String> categories = new HashMap<>();
        categories.put(CATEGORY.COVID, VNEXPRESS_COVID);
        categories.put(CATEGORY.POLITICS, VNEXPRESS_POLITICS);
        categories.put(CATEGORY.BUSINESS, VNEXPRESS_BUSINESS);
        categories.put(CATEGORY.TECHNOLOGY, VNEXPRESS_TECHNOLOGY);
        categories.put(CATEGORY.HEALTH, VNEXPRESS_HEALTH);
        categories.put(CATEGORY.SPORTS, VNEXPRESS_SPORTS);
        categories.put(CATEGORY.ENTERTAINMENT, VNEXPRESS_ENTERTAINMENT);
        categories.put(CATEGORY.WORLD, VNEXPRESS_WORLD);
        Categories VNExpressCategories = new Categories(categories);
        CssConfiguration VNExpressConfig = new CssConfiguration(
                "https://vnexpress.net/",
                CSS.VNEXPRESS_TITLE_LINK,
                CSS.VNEXPRESS_TITLE,
                CSS.VNEXPRESS_DESCRIPTION,
                CSS.VNEXPRESS_BODY,
                CSS.VNEXPRESS_TIME,
                CSS.VNEXPRESS_PIC);
        return new VNExpress("VNExpress",
                "https://s1.vnecdn.net/vnexpress/restruct/i/v395/logo_default.jpg",
                VNExpressCategories,
                VNExpressConfig,
                new VNExpressSanitizer());
    }

    public VNExpress(String name, String defaultThumbnail, Categories categories, CssConfiguration cssConfiguration, HtmlSanitizer sanitizer) {
        super(name, defaultThumbnail, categories, cssConfiguration, sanitizer);
    }

    @Override
    public LocalDateTime getPublishedTime(Document doc) {
        Elements dateTimeTag = doc.getElementsByAttributeValue("itemprop", cssConfiguration.publishedTime);
        String dateTimeStr = dateTimeTag.attr("content");
        if (dateTimeStr.isEmpty() || dateTimeStr.isBlank()){
            return LocalDateTime.now();
        }
        return Helper.parseLocalDateTime.parse(dateTimeStr);
    }

    @Override
    public String getCategory(Document doc) {
        Element tag = doc.getElementsByAttributeValue("name", "tt_site_id_detail").first();
        if (tag == null) return "";
        if (tag.attr("catename").isEmpty()) return "";
        return tag.attr("catename");
    }
}