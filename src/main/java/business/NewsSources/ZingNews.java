package business.NewsSources;

import business.Helper.CATEGORY;
import business.Helper.CSS;
import business.Helper.LocalDateTimeParser;
import business.Sanitizer.HtmlSanitizer;
import business.Sanitizer.ZingNewsSanitizer;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.time.LocalDateTime;
import java.util.HashMap;

public class ZingNews extends NewsOutlet{
    public static final String ZING_COVID = "https://zingnews.vn/tieu-diem/covid-19.html";
    public static final String ZING_POLITICS = "https://zingnews.vn/chinh-tri.html";
    public static final String ZING_BUSINESS = "https://zingnews.vn/kinh-doanh-tai-chinh.html";
    public static final String ZING_TECHNOLOGY = "https://zingnews.vn/cong-nghe.html";
    public static final String ZING_HEALTH = "https://zingnews.vn/suc-khoe.html";
    public static final String ZING_SPORTS = "https://zingnews.vn/the-thao.html";
    public static final String ZING_ENTERTAINMENT = "https://zingnews.vn/giai-tri.html";
    public static final String ZING_WORLD = "https://zingnews.vn/the-gioi.html";

    public static NewsOutlet init(){
        HashMap<String, String> categories = new HashMap<>();
        categories.put(CATEGORY.COVID, ZING_COVID);
        categories.put(CATEGORY.POLITICS, ZING_POLITICS);
        categories.put(CATEGORY.BUSINESS, ZING_BUSINESS);
        categories.put(CATEGORY.TECHNOLOGY, ZING_TECHNOLOGY);
        categories.put(CATEGORY.HEALTH, ZING_HEALTH);
        categories.put(CATEGORY.SPORTS, ZING_SPORTS);
        categories.put(CATEGORY.ENTERTAINMENT, ZING_ENTERTAINMENT);
        categories.put(CATEGORY.WORLD, ZING_WORLD);
        Categories ZingCategories = new Categories(categories);
        CssConfiguration ZingCssConfig = new CssConfiguration(
                "https://zingnews.vn/",
                CSS.ZING_TITLE_LINK,
                CSS.ZING_TITLE,
                CSS.ZING_DESCRIPTION,
                CSS.ZING_BODY,
                CSS.ZING_TIME,
                CSS.ZING_PIC);
        return new ZingNews("ZingNews",
                "https://static-znews.zadn.vn/images/logo-zing-home.svg",
                ZingCategories,
                ZingCssConfig,
                new ZingNewsSanitizer());
    }


    public ZingNews(String name, String defaultThumbnail, Categories categories, CssConfiguration cssConfiguration, HtmlSanitizer sanitizer) {
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
        Element tag = doc.selectFirst(".the-article-category");
        if (tag == null)
            return "";

        if (StringUtils.isEmpty(tag.text()))
            return "";

        return tag.text();
    }
}
