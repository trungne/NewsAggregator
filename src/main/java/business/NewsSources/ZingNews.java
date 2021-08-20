package business.NewsSources;

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
    private static final Category NEW = new Category(Category.NEW, "https://zingnews.vn/", CSS.ZING_TITLE_LINK);
    private static final Category COVID = new Category(Category.COVID, "https://zingnews.vn/tieu-diem/covid-19.html", CSS.ZING_TITLE_LINK);
    private static final Category POLITICS = new Category(Category.POLITICS, "https://zingnews.vn/chinh-tri.html", CSS.ZING_TITLE_LINK);
    private static final Category BUSINESS = new Category(Category.BUSINESS, "https://zingnews.vn/kinh-doanh-tai-chinh.html", CSS.ZING_TITLE_LINK);

    static {
        BUSINESS.add("https://zingnews.vn/bat-dong-san.html");
        BUSINESS.add("https://zingnews.vn/tieu-dung.html");
        BUSINESS.add("https://zingnews.vn/kinh-te-so.html");
        BUSINESS.add("https://zingnews.vn/hang-khong.html");
        BUSINESS.add("https://zingnews.vn/ttdn.html");
    }

    private static final Category TECHNOLOGY = new Category(Category.TECHNOLOGY, "https://zingnews.vn/cong-nghe.html", CSS.ZING_TITLE_LINK);

    static {
        TECHNOLOGY.add("https://zingnews.vn/mobile.html");
        TECHNOLOGY.add("https://zingnews.vn/gadget.html");
        TECHNOLOGY.add("https://zingnews.vn/internet.html");
        TECHNOLOGY.add("https://zingnews.vn/esports.html");
    }

    private static final Category HEALTH = new Category(Category.HEALTH, "https://zingnews.vn/suc-khoe.html", CSS.ZING_TITLE_LINK);

    static {
        HEALTH.add("https://zingnews.vn/khoe-dep.html");
        HEALTH.add("https://zingnews.vn/dinh-duong.html");
        HEALTH.add("https://zingnews.vn/me-va-be.html");
        HEALTH.add("https://zingnews.vn/benh-thuong-gap.html");
    }

    private static final Category SPORTS = new Category(Category.SPORTS, "https://zingnews.vn/the-thao.html", CSS.ZING_TITLE_LINK);

    static {
        SPORTS.add("https://zingnews.vn/bong-da-viet-nam.html");
        SPORTS.add("https://zingnews.vn/bong-da-anh.html");
        SPORTS.add("https://zingnews.vn/vo-thuat.html");
        SPORTS.add("https://zingnews.vn/esports-the-thao.html");
    }

    private static final Category ENTERTAINMENT = new Category(Category.ENTERTAINMENT, "https://zingnews.vn/giai-tri.html", CSS.ZING_TITLE_LINK);

    static {
        ENTERTAINMENT.add("https://zingnews.vn/sao-viet.html");
        ENTERTAINMENT.add("https://zingnews.vn/am-nhac.html");
        ENTERTAINMENT.add("https://zingnews.vn/phim-anh.html");
        ENTERTAINMENT.add("https://zingnews.vn/thoi-trang.html");
    }

    private static final Category WORLD = new Category(Category.WORLD, "https://zingnews.vn/the-gioi.html", CSS.ZING_TITLE_LINK);

    static {
        WORLD.add("https://zingnews.vn/quan-su-the-gioi.html");
        WORLD.add("https://zingnews.vn/tu-lieu-the-gioi.html");
        WORLD.add("https://zingnews.vn/phan-tich-the-gioi.html");
        WORLD.add("https://zingnews.vn/nguoi-viet-4-phuong.html");
        WORLD.add("https://zingnews.vn/chuyen-la-the-gioi.html");
    }

    // others
    private static final Category OTHERS = new Category(Category.OTHERS, "", CSS.ZING_TITLE_LINK);

    static {
        OTHERS.add("https://zingnews.vn/thoi-su.html");
        OTHERS.add("https://zingnews.vn/phap-luat.html");
        OTHERS.add("https://zingnews.vn/doi-song.html");
        OTHERS.add("https://zingnews.vn/giao-duc.html");
    }


    public static NewsOutlet init() {
        HashMap<String, Category> categories = new HashMap<>();
        categories.put(Category.NEW, NEW);
        categories.put(Category.COVID, COVID);
        categories.put(Category.POLITICS, POLITICS);
        categories.put(Category.BUSINESS, BUSINESS);
        categories.put(Category.TECHNOLOGY, TECHNOLOGY);
        categories.put(Category.HEALTH, HEALTH);
        categories.put(Category.SPORTS, SPORTS);
        categories.put(Category.ENTERTAINMENT, ENTERTAINMENT);
        categories.put(Category.WORLD, WORLD);
        categories.put(Category.OTHERS, OTHERS);

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
                category = Category.convert(category);

                if (StringUtils.isEmpty(category))
                    continue;

                if (!categoryList.contains(category)) {
                    categoryList.add(category);
                }
            }
        }


        if (categoryList.isEmpty()) {
            categoryList.add(Category.OTHERS);
        }

        return categoryList;
    }
}
