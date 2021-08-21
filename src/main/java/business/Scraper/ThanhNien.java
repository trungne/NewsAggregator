package business.Scraper;

import business.Helper.CSS;
import business.Sanitizer.ThanhNienFilter;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.NodeFilter;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public final class ThanhNien extends NewsOutlet {
    private static final Category NEW = new Category(Category.NEW, "https://thanhnien.vn/", CSS.THANHNIEN_TITLE_LINK);
    private static final Category COVID = new Category(Category.COVID, "https://thanhnien.vn/covid-19/", CSS.THANHNIEN_TITLE_LINK);
    private static final Category POLITICS = new Category(Category.POLITICS, "https://thanhnien.vn/thoi-su/chinh-tri/", CSS.THANHNIEN_TITLE_LINK);
    private static final Category BUSINESS = new Category(Category.BUSINESS, "https://thanhnien.vn/tai-chinh-kinh-doanh", CSS.THANHNIEN_TITLE_LINK);

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

    private static final Category TECHNOLOGY = new Category(Category.TECHNOLOGY, "https://thanhnien.vn/cong-nghe/", CSS.THANHNIEN_TITLE_LINK);

    static {
        TECHNOLOGY.add("https://thanhnien.vn/cong-nghe/xu-huong/");
        TECHNOLOGY.add("https://thanhnien.vn/cong-nghe/san-pham-moi/");
        TECHNOLOGY.add("https://thanhnien.vn/cong-nghe/kinh-nghiem/");
        TECHNOLOGY.add("https://thanhnien.vn/cong-nghe/y-tuong/");
        TECHNOLOGY.add("https://thanhnien.vn/cong-nghe/chuyen-doi-so/");
    }

    private static final Category HEALTH = new Category(Category.HEALTH, "https://thanhnien.vn/suc-khoe/", CSS.THANHNIEN_TITLE_LINK);

    static {
        HEALTH.add("https://thanhnien.vn/suc-khoe/lam-dep/");
        HEALTH.add("https://thanhnien.vn/suc-khoe/khoe-dep-moi-ngay/");
        HEALTH.add("https://thanhnien.vn/suc-khoe/gioi-tinh/");
        HEALTH.add("https://thanhnien.vn/suc-khoe/song-vui-khoe/");
    }

    private static final Category SPORTS = new Category(Category.SPORTS, "https://thanhnien.vn/the-thao/", CSS.THANHNIEN_TITLE_LINK);

    static {
        SPORTS.add("https://thanhnien.vn/the-thao/bong-da-viet-nam/");
        SPORTS.add("https://thanhnien.vn/the-thao/bong-da-quoc-te/");
        SPORTS.add("https://thanhnien.vn/the-thao/tin-chuyen-nhuong/");
        SPORTS.add("https://thanhnien.vn/the-thao/bong-ro/");
        SPORTS.add("https://thanhnien.vn/the-thao/the-thao-cong-dong/");
        SPORTS.add("https://thanhnien.vn/the-thao/toan-canh-the-thao/");
    }

    private static final Category ENTERTAINMENT = new Category(Category.ENTERTAINMENT, "https://thanhnien.vn/giai-tri/", CSS.THANHNIEN_TITLE_LINK);

    static {
        ENTERTAINMENT.add("https://thanhnien.vn/giai-tri/phim/");
        ENTERTAINMENT.add("https://thanhnien.vn/giai-tri/truyen-hinh/");
        ENTERTAINMENT.add("https://thanhnien.vn/giai-tri/doi-nghe-si/");

    }

    private static final Category WORLD = new Category(Category.WORLD, "https://thanhnien.vn/the-gioi/", CSS.THANHNIEN_TITLE_LINK);

    static {
        WORLD.add("https://thanhnien.vn/the-gioi/kinh-te-the-gioi/");
        WORLD.add("https://thanhnien.vn/the-gioi/quan-su/");
        WORLD.add("https://thanhnien.vn/the-gioi/goc-nhin/");
        WORLD.add("https://thanhnien.vn/the-gioi/ho-so/");
        WORLD.add("https://thanhnien.vn/the-gioi/nguoi-viet-nam-chau/");
        WORLD.add("https://thanhnien.vn/the-gioi/chuyen-la/");
    }

    private static final Category OTHERS = new Category(Category.OTHERS, "", CSS.THANHNIEN_TITLE_LINK);

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
                ThanhNienCssConfig);
    }

    public ThanhNien(String name,
                     String defaultThumbnail,
                     HashMap<String, Category> categories,
                     CssConfiguration cssConfiguration) {
        super(name, defaultThumbnail, categories, cssConfiguration);
    }

    @Override
    public LocalDateTime scrapePublishedTime(Document doc) {
        return scrapePublishedTimeFromMeta(doc, "property", cssConfiguration.publishedTime, "content");
    }

    @Override
    public Set<String> scrapeCategoryNames(Document doc) {
        Set<String> categoryList = new HashSet<>();

        // scrape category in meta tag
        String category = scrapeCategoryNamesInMeta(doc, "property", "article:section", "content");
        if (!StringUtils.isEmpty(category)){
            categoryList.add(category);
        }

        // scrape category in breadcrumb
        categoryList.addAll(scrapeCategoryNamesInBreadcrumb(doc, "breadcrumbs"));
        return categoryList;

//        List<String> categoryList = new ArrayList<>();
//
//        // get parent category
//        Element tag = doc.getElementsByAttributeValue("property", "article:section").first();
//
//        if (tag != null) {
//            String parentCategory = tag.attr("content");
//            parentCategory = Category.convert(parentCategory);
//            if (!StringUtils.isEmpty(parentCategory))
//                categoryList.add(parentCategory);
//        }
//
//        // get child category
//        Element childrenCategoryTag = doc.selectFirst(".breadcrumbs");
//        if (childrenCategoryTag != null) {
//            Elements children = childrenCategoryTag.getElementsByTag("a");
//            for (Element e : children) {
//                String category = e.attr("title");
//                category = Category.convert(category);
//
//                if (StringUtils.isEmpty(category))
//                    continue;
//
//                if (!categoryList.contains(category)) {
//                    categoryList.add(category);
//                }
//            }
//        }
//
//        if (categoryList.isEmpty())
//            categoryList.add(Category.OTHERS);
//
//        return categoryList;
    }

    @Override
    public NodeFilter getNodeFilter(Element root) {
        return new ThanhNienFilter(root);
    }
}
