package business.NewsSources;

import Application.Main;
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
import java.util.*;


public class ZingNews extends NewsOutlet{
    // main category

    private static final String ZING_COVID = "https://zingnews.vn/tieu-diem/covid-19.html";
    private static final String ZING_POLITICS = "https://zingnews.vn/chinh-tri.html";
    private static final String ZING_BUSINESS = "https://zingnews.vn/kinh-doanh-tai-chinh.html";
    private static final String ZING_TECHNOLOGY = "https://zingnews.vn/cong-nghe.html";
    private static final String ZING_HEALTH = "https://zingnews.vn/suc-khoe.html";
    private static final String ZING_SPORTS = "https://zingnews.vn/the-thao.html";
    private static final String ZING_ENTERTAINMENT = "https://zingnews.vn/giai-tri.html";
    private static final String ZING_WORLD = "https://zingnews.vn/the-gioi.html";

    private static final Category COVID = new SubCategory(CATEGORY.COVID, ZING_COVID, CSS.ZING_TITLE_LINK);

    private static final Category POLITICS = new SubCategory(CATEGORY.POLITICS, ZING_POLITICS, CSS.ZING_TITLE_LINK);


    private static final Category BUSINESS = new MainCategory(CATEGORY.BUSINESS, ZING_BUSINESS, CSS.ZING_TITLE_LINK);
    static {
        BUSINESS.add(new SubCategory("https://zingnews.vn/bat-dong-san.html", CSS.ZING_TITLE_LINK));
        BUSINESS.add(new SubCategory("https://zingnews.vn/tieu-dung.html", CSS.ZING_TITLE_LINK));
        BUSINESS.add(new SubCategory("https://zingnews.vn/kinh-te-so.html", CSS.ZING_TITLE_LINK));
        BUSINESS.add(new SubCategory("https://zingnews.vn/hang-khong.html", CSS.ZING_TITLE_LINK));
        BUSINESS.add(new SubCategory("https://zingnews.vn/ttdn.html", CSS.ZING_TITLE_LINK));
    }
    private static final Category TECHNOLOGY = new MainCategory(CATEGORY.TECHNOLOGY, ZING_TECHNOLOGY, CSS.ZING_TITLE_LINK);


    // others
    private static final HashMap<String, String> ZING_OTHERS = new HashMap<>();
    private static final String ZING_SOCIETY = "https://zingnews.vn/thoi-su.html";
    private static final String ZING_LIFESTYLE = "https://zingnews.vn/doi-song.html";
    private static final String ZING_EDUCATION = "https://zingnews.vn/giao-duc.html";
    private static final String ZING_CAR = "https://zingnews.vn/oto-xe-may.html";
    private static final String ZING_TOURISM = "https://zingnews.vn/du-lich.html";

    public static NewsOutlet init(){
        Categories ZingCategories = new Categories(ZING_COVID, ZING_POLITICS,
                                                    ZING_BUSINESS, ZING_TECHNOLOGY,
                                                    ZING_HEALTH, ZING_SPORTS,
                                                    ZING_ENTERTAINMENT, ZING_WORLD);


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
    public Set<String> getCategoryNames(Document doc) {
        Element tag = doc.selectFirst(".the-article-category");
        Set<String> categoryList = new HashSet<>();
        if (tag != null){
            Elements categoryTags = tag.getElementsByClass("parent_cate");
            for (Element e: categoryTags){
                String category = e.attr("title")
                // map category
                categoryList.add(category);
            }

        }

        if (categoryList.isEmpty()){
            categoryList.add(CATEGORY.OTHERS);
        }

        return categoryList;
    }
}
