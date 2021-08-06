package business.NewsSources;

import business.Helper.CATEGORY;
import business.Helper.CSS;
import business.Helper.LocalDateTimeParser;
import business.Sanitizer.HtmlSanitizer;
import business.Sanitizer.VNExpressSanitizer;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class VNExpress extends NewsOutlet{
    private static final String VNEXPRESS_COVID =  "https://vnexpress.net/covid-19/tin-tuc";
    private static final String VNEXPRESS_POLITICS = "https://vnexpress.net/thoi-su/chinh-tri";
    private static final String VNEXPRESS_BUSINESS = "https://vnexpress.net/kinh-doanh";
    private static final String VNEXPRESS_TECHNOLOGY = "https://vnexpress.net/khoa-hoc";
    private static final String VNEXPRESS_HEALTH = "https://vnexpress.net/suc-khoe";
    private static final String VNEXPRESS_SPORTS = "https://vnexpress.net/the-thao";
    private static final String VNEXPRESS_ENTERTAINMENT = "https://vnexpress.net/giai-tri";
    private static final String VNEXPRESS_WORLD = "https://vnexpress.net/the-gioi";
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

        if (StringUtils.isEmpty(dateTimeStr)){
            return LocalDateTime.now();
        }

        return LocalDateTimeParser.parse(dateTimeStr);
    }

    @Override
    public Set<String> getCategoryNames(Document doc) {
        // parent category
        Element parentTag = doc.getElementsByAttributeValue("name", "tt_site_id_detail").first();
        String parentCategory;

        if (parentTag == null){
            parentCategory = CATEGORY.OTHERS;
        }
        else{
            parentCategory = parentTag.attr("catename");
            // map the category to CATEGORY.
            // TODO CREATE A FUNCTION TO MAP VIETNAMESE NAME TO ENGLISH NAME
        }
        Set<String> categoryList = new HashSet<>();
        categoryList.add(parentCategory);

        Element tag = doc.selectFirst(".breadcrumb");
        if (tag != null){
            Elements categoryTags = tag.getElementsByTag("li");
            for (Element e: categoryTags){
                String category = e.attr("title");
                // TODO:  map the category to english
                categoryList.add(category);
            }
        }

        return categoryList;
    }
}