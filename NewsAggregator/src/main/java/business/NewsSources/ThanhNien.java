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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ThanhNien extends NewsOutlet {
    private static final Category NEW = new Category(CATEGORY.NEW, "https://thanhnien.vn/", CSS.THANHNIEN_TITLE_LINK);
    private static final Category COVID = new Category(CATEGORY.COVID, "https://thanhnien.vn/covid-19/", CSS.THANHNIEN_TITLE_LINK);
    private static final Category POLITICS = new Category(CATEGORY.POLITICS, "https://thanhnien.vn/thoi-su/chinh-tri/", CSS.THANHNIEN_TITLE_LINK);
    private static final Category BUSINESS = new Category(CATEGORY.BUSINESS, "https://thanhnien.vn/tai-chinh-kinh-doanh", CSS.THANHNIEN_TITLE_LINK);
    static {
        BUSINESS.add("https://thanhnien.vn/tai-chinh-kinh-doanh/kinh-te-xanh/");
        BUSINESS.add("https://thanhnien.vn/kinh-doanh/chinh-sach-phat-trien/");
        BUSINESS.add("https://thanhnien.vn/tai-chinh-kinh-doanh/ngan-hang/");
        BUSINESS.add("https://thanhnien.vn/tai-chinh-kinh-doanh/chung-khoan/");
        BUSINESS.add("https://thanhnien.vn/tai-chinh-kinh-doanh/doanh-nghiep/");
        BUSINESS.add("https://thanhnien.vn/tai-chinh-kinh-doanh/doanh-nhan/");
        BUSINESS.add("https://thanhnien.vn/tai-chinh-kinh-doanh/tieu-dung/");
        BUSINESS.add("https://thanhnien.vn/tai-chinh-kinh-doanh/lam-giau/");
        BUSINESS.add("https://thanhnien.vn/tai-chinh-kinh-doanh/dia-oc/");
    }

    private static final Category TECHNOLOGY = new Category(CATEGORY.TECHNOLOGY, "https://thanhnien.vn/cong-nghe/", CSS.THANHNIEN_TITLE_LINK);
    static {
        TECHNOLOGY.add("https://thanhnien.vn/cong-nghe/xu-huong/");
        TECHNOLOGY.add("https://thanhnien.vn/cong-nghe/san-pham-moi/");
        TECHNOLOGY.add("https://thanhnien.vn/cong-nghe/kinh-nghiem/");
        TECHNOLOGY.add("https://thanhnien.vn/cong-nghe/y-tuong/");
        TECHNOLOGY.add("https://thanhnien.vn/cong-nghe/chuyen-doi-so/");
    }

    private static final Category HEALTH = new Category(CATEGORY.HEALTH, "https://thanhnien.vn/suc-khoe/", CSS.THANHNIEN_TITLE_LINK);
    static {
        HEALTH.add("https://thanhnien.vn/suc-khoe/lam-dep/");
        HEALTH.add("https://thanhnien.vn/suc-khoe/khoe-dep-moi-ngay/");
        HEALTH.add("https://thanhnien.vn/suc-khoe/gioi-tinh/");
        HEALTH.add("https://thanhnien.vn/suc-khoe/song-vui-khoe/");
    }

    private static final Category SPORTS = new Category(CATEGORY.SPORTS, "https://thanhnien.vn/the-thao/", CSS.THANHNIEN_TITLE_LINK);
    static {
        SPORTS.add("https://thanhnien.vn/the-thao/bong-da-viet-nam/");
        SPORTS.add("https://thanhnien.vn/the-thao/bong-da-quoc-te/");
        SPORTS.add("https://thanhnien.vn/the-thao/tin-chuyen-nhuong/");
        SPORTS.add("https://thanhnien.vn/the-thao/bong-ro/");
        SPORTS.add("https://thanhnien.vn/the-thao/the-thao-cong-dong/");
        SPORTS.add("https://thanhnien.vn/the-thao/toan-canh-the-thao/");
    }

    private static final Category ENTERTAINMENT = new Category(CATEGORY.ENTERTAINMENT, "https://thanhnien.vn/giai-tri/", CSS.THANHNIEN_TITLE_LINK);
    static {
        ENTERTAINMENT.add("https://thanhnien.vn/giai-tri/phim/");
        ENTERTAINMENT.add("https://thanhnien.vn/giai-tri/truyen-hinh/");
        ENTERTAINMENT.add("https://thanhnien.vn/giai-tri/doi-nghe-si/");

    }

    private static final Category WORLD = new Category(CATEGORY.WORLD, "https://thanhnien.vn/the-gioi/", CSS.THANHNIEN_TITLE_LINK);
    static {
        WORLD.add("https://thanhnien.vn/the-gioi/kinh-te-the-gioi/");
        WORLD.add("https://thanhnien.vn/the-gioi/quan-su/");
        WORLD.add("https://thanhnien.vn/the-gioi/goc-nhin/");
        WORLD.add("https://thanhnien.vn/the-gioi/ho-so/");
        WORLD.add("https://thanhnien.vn/the-gioi/nguoi-viet-nam-chau/");
        WORLD.add("https://thanhnien.vn/the-gioi/chuyen-la/");
    }

    private static final Category OTHERS = new Category(CATEGORY.OTHERS, "", CSS.THANHNIEN_TITLE_LINK);

    static {
        OTHERS.add("https://thanhnien.vn/thoi-su/");
        OTHERS.add("https://thanhnien.vn/toi-viet/");
        OTHERS.add("https://thanhnien.vn/van-hoa/");
        OTHERS.add("https://thanhnien.vn/doi-song/");
        OTHERS.add("https://thanhnien.vn/gioi-tre/");
        OTHERS.add("https://thanhnien.vn/giao-duc/");
        OTHERS.add("https://thanhnien.vn/game/");
        OTHERS.add("https://thanhnien.vn/du-lich/");
        OTHERS.add("https://thanhnien.vn/xe/");
        OTHERS.add("https://thanhnien.vn/ban-can-biet/");
    }


    public static NewsOutlet init() {
        HashMap<String, Category> categories = new HashMap<>();
        categories.put(CATEGORY.NEW, NEW);
        categories.put(CATEGORY.COVID, COVID);
        categories.put(CATEGORY.POLITICS, POLITICS);
        categories.put(CATEGORY.BUSINESS, BUSINESS);
        categories.put(CATEGORY.TECHNOLOGY, TECHNOLOGY);
        categories.put(CATEGORY.HEALTH, HEALTH);
        categories.put(CATEGORY.SPORTS, SPORTS);
        categories.put(CATEGORY.ENTERTAINMENT, ENTERTAINMENT);
        categories.put(CATEGORY.WORLD, WORLD);
        categories.put(CATEGORY.OTHERS, OTHERS);


        CssConfiguration ThanhNienCssConfig = new CssConfiguration(
                "https://thanhnien.vn/",
                CSS.THANHNIEN_TITLE,
                CSS.THANHNIEN_DESCRIPTION,
                CSS.THANHNIEN_BODY,
                CSS.THANHNIEN_TIME,
                CSS.THANHNIEN_PIC);
        return new ThanhNien("Thanh Nien",
                "https://static.thanhnien.vn/v2/App_Themes/images/logo-tn-2.png",
                categories,
                ThanhNienCssConfig,
                new ThanhNienSanitizer());
    }

    public ThanhNien(String name, String defaultThumbnail, HashMap<String, Category> categories, CssConfiguration cssConfiguration, HtmlSanitizer sanitizer) {
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
        List<String> categoryList = new ArrayList<>();

        // get parent category
        Element tag = doc.getElementsByAttributeValue("property", "article:section").first();

        if (tag != null) {
            String parentCategory = tag.attr("content");
            parentCategory = CATEGORY.convert(parentCategory);
            if (!StringUtils.isEmpty(parentCategory))
                categoryList.add(parentCategory);
        }

        // get child category
        Element childrenCategoryTag = doc.selectFirst(".breadcrumbs");
        if (childrenCategoryTag != null) {
            Elements children = childrenCategoryTag.getElementsByTag("a");
            for (Element e : children) {
                String category = e.attr("title");
                category = CATEGORY.convert(category);

                if (StringUtils.isEmpty(category))
                    continue;

                if (!categoryList.contains(category)) {
                    categoryList.add(category);
                }
            }
        }

        if (categoryList.isEmpty())
            categoryList.add(CATEGORY.OTHERS);

        return categoryList;
    }
}
