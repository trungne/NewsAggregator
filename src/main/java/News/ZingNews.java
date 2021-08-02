package News;

import News.Sanitizer.HtmlSanitizer;
import News.Sanitizer.VNExpressSanitizer;
import News.Sanitizer.ZingNewsSanitizer;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.time.LocalDateTime;
import java.util.HashMap;

public class ZingNews extends NewsOutlet{
    public static NewsOutlet init(){
        HashMap<String, String> categories = new HashMap<>();
        categories.put(CATEGORY.COVID, CATEGORY.ZING_COVID);
        categories.put(CATEGORY.POLITICS, CATEGORY.ZING_POLITICS);
        categories.put(CATEGORY.BUSINESS, CATEGORY.ZING_BUSINESS);
        categories.put(CATEGORY.TECHNOLOGY, CATEGORY.ZING_TECHNOLOGY);
        categories.put(CATEGORY.HEALTH, CATEGORY.ZING_HEALTH);
        categories.put(CATEGORY.SPORTS, CATEGORY.ZING_SPORTS);
        categories.put(CATEGORY.ENTERTAINMENT, CATEGORY.ZING_ENTERTAINMENT);
        categories.put(CATEGORY.WORLD, CATEGORY.ZING_WORLD);
        Categories ZingCategories = new Categories(categories);
        CssConfiguration ZingCssConfig = new CssConfiguration(
                "https://zingnews.vn/",
                CSS.ZING_TITLE_LINK,
                CSS.ZING_TITLE,
                CSS.ZING_DESCRIPTION,
                CSS.ZING_BODY,
                CSS.ZING_TIME,
                CSS.ZING_PIC);
        NewsOutlet news = new ZingNews("ZingNews",
                "https://static-znews.zadn.vn/images/logo-zing-home.svg",
                ZingCategories,
                ZingCssConfig,
                new ZingNewsSanitizer());
        return news;
    }


    public ZingNews(String name, String defaultThumbnail, Categories categories, CssConfiguration cssConfiguration, HtmlSanitizer sanitizer) {
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
        Element tag = doc.selectFirst(".the-article-category");
        if (tag == null) return "";
        if (tag.text().isEmpty()) return "";
        return tag.text();
    }
}
