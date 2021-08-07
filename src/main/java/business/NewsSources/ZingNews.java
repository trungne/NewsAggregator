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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ZingNews extends NewsOutlet {
    // main category
    private static final Category COVID = new SubCategory(CATEGORY.COVID, "https://zingnews.vn/tieu-diem/covid-19.html", CSS.ZING_TITLE_LINK);
    private static final Category POLITICS = new SubCategory(CATEGORY.POLITICS, "https://zingnews.vn/chinh-tri.html", CSS.ZING_TITLE_LINK);
    private static final Category BUSINESS = new MainCategory(CATEGORY.BUSINESS, "https://zingnews.vn/kinh-doanh-tai-chinh.html", CSS.ZING_TITLE_LINK);

    static {
        BUSINESS.addSub("https://zingnews.vn/bat-dong-san.html");
        BUSINESS.addSub("https://zingnews.vn/tieu-dung.html");
        BUSINESS.addSub("https://zingnews.vn/kinh-te-so.html");
        BUSINESS.addSub("https://zingnews.vn/hang-khong.html");
        BUSINESS.addSub("https://zingnews.vn/ttdn.html");
    }

    private static final Category TECHNOLOGY = new MainCategory(CATEGORY.TECHNOLOGY, "https://zingnews.vn/cong-nghe.html", CSS.ZING_TITLE_LINK);

    static {
        TECHNOLOGY.addSub("https://zingnews.vn/mobile.html");
        TECHNOLOGY.addSub("https://zingnews.vn/gadget.html");
        TECHNOLOGY.addSub("https://zingnews.vn/internet.html");
        TECHNOLOGY.addSub("https://zingnews.vn/esports.html");
    }

    private static final Category HEALTH = new MainCategory(CATEGORY.HEALTH, "https://zingnews.vn/suc-khoe.html", CSS.ZING_TITLE_LINK);

    static {
        HEALTH.addSub("https://zingnews.vn/khoe-dep.html");
        HEALTH.addSub("https://zingnews.vn/dinh-duong.html");
        HEALTH.addSub("https://zingnews.vn/me-va-be.html");
        HEALTH.addSub("https://zingnews.vn/benh-thuong-gap.html");
    }

    private static final Category SPORTS = new MainCategory(CATEGORY.SPORTS, "https://zingnews.vn/the-thao.html", CSS.ZING_TITLE_LINK);

    static {
        SPORTS.addSub("https://zingnews.vn/bong-da-viet-nam.html");
        SPORTS.addSub("https://zingnews.vn/bong-da-anh.html");
        SPORTS.addSub("https://zingnews.vn/vo-thuat.html");
        SPORTS.addSub("https://zingnews.vn/esports-the-thao.html");
    }

    private static final Category ENTERTAINMENT = new MainCategory(CATEGORY.ENTERTAINMENT, "https://zingnews.vn/giai-tri.html", CSS.ZING_TITLE_LINK);

    static {
        ENTERTAINMENT.addSub("https://zingnews.vn/sao-viet.html");
        ENTERTAINMENT.addSub("https://zingnews.vn/am-nhac.html");
        ENTERTAINMENT.addSub("https://zingnews.vn/phim-anh.html");
        ENTERTAINMENT.addSub("https://zingnews.vn/thoi-trang.html");
    }

    private static final Category WORLD = new MainCategory(CATEGORY.WORLD, "https://zingnews.vn/the-gioi.html", CSS.ZING_TITLE_LINK);

    static {
        WORLD.addSub("https://zingnews.vn/quan-su-the-gioi.html");
        WORLD.addSub("https://zingnews.vn/tu-lieu-the-gioi.html");
        WORLD.addSub("https://zingnews.vn/phan-tich-the-gioi.html");
        WORLD.addSub("https://zingnews.vn/nguoi-viet-4-phuong.html");
        WORLD.addSub("https://zingnews.vn/chuyen-la-the-gioi.html");
    }

    // others
    private static final Category OTHERS = new MainCategory(CATEGORY.OTHERS, "", CSS.ZING_TITLE_LINK);

    static {
        OTHERS.addSub("https://zingnews.vn/thoi-su.html");
        OTHERS.addSub("https://zingnews.vn/phap-luat.html");
        OTHERS.addSub("https://zingnews.vn/doi-song.html");
        OTHERS.addSub("https://zingnews.vn/giao-duc.html");
    }


    public static NewsOutlet init() {
        HashMap<String, Category> categories = new HashMap<>();
        categories.put(CATEGORY.COVID, COVID);
        categories.put(CATEGORY.POLITICS, POLITICS);
        categories.put(CATEGORY.BUSINESS, BUSINESS);
        categories.put(CATEGORY.TECHNOLOGY, TECHNOLOGY);
        categories.put(CATEGORY.HEALTH, HEALTH);
        categories.put(CATEGORY.SPORTS, SPORTS);
        categories.put(CATEGORY.ENTERTAINMENT, ENTERTAINMENT);
        categories.put(CATEGORY.WORLD, WORLD);
        categories.put(CATEGORY.OTHERS, OTHERS);

        CssConfiguration ZingCssConfig = new CssConfiguration(
                "https://zingnews.vn/",
                CSS.ZING_TITLE,
                CSS.ZING_DESCRIPTION,
                CSS.ZING_BODY,
                CSS.ZING_TIME,
                CSS.ZING_PIC);
        return new ZingNews("ZingNews",
                "https://static-znews.zadn.vn/images/logo-zing-home.svg",
                categories,
                ZingCssConfig,
                new ZingNewsSanitizer());
    }


    public ZingNews(String name, String defaultThumbnail, HashMap<String, Category> categories, CssConfiguration cssConfiguration, HtmlSanitizer sanitizer) {
        super(name, defaultThumbnail, categories, cssConfiguration, sanitizer);
    }

    @Override
    public LocalDateTime getPublishedTime(Document doc) {
        Elements dateTimeTag = doc.getElementsByAttributeValue("property", cssConfiguration.publishedTime);
        String dateTimeStr = dateTimeTag.attr("content");

        if (StringUtils.isEmpty(dateTimeStr)) {
            return LocalDateTime.now();
        }

        return LocalDateTimeParser.parse(dateTimeStr);
    }

    @Override
    public List<String> getCategoryNames(Document doc) {
        Element tag = doc.selectFirst(".the-article-category");
        List<String> categoryList = new ArrayList<>();
        if (tag != null) {
            Elements categoryTags = tag.getElementsByClass("parent_cate");
            for (Element e : categoryTags) {
                String category = e.attr("title");
                category = CATEGORY.convert(category);
                if (!categoryList.contains(category)) {
                    categoryList.add(category);
                }
            }

        }

        if (categoryList.isEmpty()) {
            categoryList.add(CATEGORY.OTHERS);
        }

        return categoryList;
    }
}
