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

import java.net.PasswordAuthentication;
import java.time.LocalDateTime;
import java.util.*;

public class ThanhNien extends NewsOutlet{
    private static final Category COVID = new SubCategory(CATEGORY.COVID, "https://thanhnien.vn/covid-19/", CSS.THANHNIEN_TITLE_LINK);
    private static final Category POLITICS = new SubCategory(CATEGORY.POLITICS, "https://thanhnien.vn/thoi-su/chinh-tri/", CSS.THANHNIEN_TITLE_LINK);

    private static final Category BUSINESS = new MainCategory(CATEGORY.BUSINESS, "https://thanhnien.vn/tai-chinh-kinh-doanh", CSS.THANHNIEN_TITLE_LINK);
    static {
        BUSINESS.addSub("https://thanhnien.vn/tai-chinh-kinh-doanh/kinh-te-xanh/");
        BUSINESS.addSub("https://thanhnien.vn/kinh-doanh/chinh-sach-phat-trien/");
        BUSINESS.addSub("https://thanhnien.vn/tai-chinh-kinh-doanh/ngan-hang/");
        BUSINESS.addSub("https://thanhnien.vn/tai-chinh-kinh-doanh/chung-khoan/");
        BUSINESS.addSub("https://thanhnien.vn/tai-chinh-kinh-doanh/doanh-nghiep/");
        BUSINESS.addSub("https://thanhnien.vn/tai-chinh-kinh-doanh/doanh-nhan/");
        BUSINESS.addSub("https://thanhnien.vn/tai-chinh-kinh-doanh/tieu-dung/");
        BUSINESS.addSub("https://thanhnien.vn/tai-chinh-kinh-doanh/lam-giau/");
        BUSINESS.addSub("https://thanhnien.vn/tai-chinh-kinh-doanh/dia-oc/");
    }

    private static final Category TECHNOLOGY = new MainCategory(CATEGORY.TECHNOLOGY, "https://thanhnien.vn/cong-nghe/", CSS.THANHNIEN_TITLE_LINK);
    static {
        TECHNOLOGY.addSub("https://thanhnien.vn/cong-nghe/xu-huong/");
        TECHNOLOGY.addSub("https://thanhnien.vn/cong-nghe/san-pham-moi/");
        TECHNOLOGY.addSub("https://thanhnien.vn/cong-nghe/kinh-nghiem/");
        TECHNOLOGY.addSub("https://thanhnien.vn/cong-nghe/y-tuong/");
        TECHNOLOGY.addSub("https://thanhnien.vn/cong-nghe/chuyen-doi-so/");
    }

    private static final Category HEALTH = new MainCategory(CATEGORY.HEALTH, "https://thanhnien.vn/suc-khoe/", CSS.THANHNIEN_TITLE_LINK);
    static {
        HEALTH.addSub("https://thanhnien.vn/suc-khoe/lam-dep/");
        HEALTH.addSub("https://thanhnien.vn/suc-khoe/khoe-dep-moi-ngay/");
        HEALTH.addSub("https://thanhnien.vn/suc-khoe/gioi-tinh/");
        HEALTH.addSub("https://thanhnien.vn/suc-khoe/song-vui-khoe/");
    }

    private static final Category SPORTS = new MainCategory(CATEGORY.SPORTS, "https://thanhnien.vn/the-thao/", CSS.THANHNIEN_TITLE_LINK);
    static {
        SPORTS.addSub("https://thanhnien.vn/the-thao/bong-da-viet-nam/");
        SPORTS.addSub("https://thanhnien.vn/the-thao/bong-da-quoc-te/");
        SPORTS.addSub("https://thanhnien.vn/the-thao/tin-chuyen-nhuong/");
        SPORTS.addSub("https://thanhnien.vn/the-thao/bong-ro/");
        SPORTS.addSub("https://thanhnien.vn/the-thao/the-thao-cong-dong/");
        SPORTS.addSub("https://thanhnien.vn/the-thao/toan-canh-the-thao/");
    }

    private static final Category ENTERTAINMENT = new MainCategory(CATEGORY.ENTERTAINMENT, "https://thanhnien.vn/giai-tri/", CSS.THANHNIEN_TITLE_LINK);
    static {
        ENTERTAINMENT.addSub("https://thanhnien.vn/giai-tri/phim/");
        ENTERTAINMENT.addSub("https://thanhnien.vn/giai-tri/truyen-hinh/");
        ENTERTAINMENT.addSub("https://thanhnien.vn/giai-tri/doi-nghe-si/");

    }
    private static final Category WORLD = new MainCategory(CATEGORY.WORLD, "https://thanhnien.vn/the-gioi/", CSS.THANHNIEN_TITLE_LINK);
    static {
        WORLD.addSub("https://thanhnien.vn/the-gioi/kinh-te-the-gioi/");
        WORLD.addSub("https://thanhnien.vn/the-gioi/quan-su/");
        WORLD.addSub("https://thanhnien.vn/the-gioi/goc-nhin/");
        WORLD.addSub("https://thanhnien.vn/the-gioi/ho-so/");
        WORLD.addSub("https://thanhnien.vn/the-gioi/nguoi-viet-nam-chau/");
        WORLD.addSub("https://thanhnien.vn/the-gioi/chuyen-la/");
    }

    private static final Category OTHERS = new MainCategory(CATEGORY.OTHERS, "", CSS.THANHNIEN_TITLE_LINK);
    static {
        OTHERS.addSub("https://thanhnien.vn/thoi-su/");
        OTHERS.addSub("https://thanhnien.vn/toi-viet/");
        OTHERS.addSub("https://thanhnien.vn/van-hoa/");
        OTHERS.addSub("https://thanhnien.vn/doi-song/");
        OTHERS.addSub("https://thanhnien.vn/gioi-tre/");
        OTHERS.addSub("https://thanhnien.vn/giao-duc/");
        OTHERS.addSub("https://thanhnien.vn/game/");
        OTHERS.addSub("https://thanhnien.vn/du-lich/");
        OTHERS.addSub("https://thanhnien.vn/xe/");
        OTHERS.addSub("https://thanhnien.vn/ban-can-biet/");
    }


    public static NewsOutlet init(){
        HashMap<String, Category> categories = new HashMap<>();
        categories.put(CATEGORY.COVID,COVID);
        categories.put(CATEGORY.POLITICS,POLITICS);
        categories.put(CATEGORY.BUSINESS,BUSINESS);
        categories.put(CATEGORY.TECHNOLOGY,TECHNOLOGY);
        categories.put(CATEGORY.HEALTH,HEALTH);
        categories.put(CATEGORY.SPORTS,SPORTS);
        categories.put(CATEGORY.ENTERTAINMENT,ENTERTAINMENT);
        categories.put(CATEGORY.WORLD,WORLD);
        categories.put(CATEGORY.OTHERS,OTHERS);


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

        if (StringUtils.isEmpty(dateTimeStr)){
            return LocalDateTime.now();
        }
        return LocalDateTimeParser.parse(dateTimeStr);
    }

    @Override
    public List<String> getCategoryNames(Document doc) {
        // get parent category
        Element tag = doc.getElementsByAttributeValue("property", "article:section").first();
        String parentCategory;
        if (tag == null){
            parentCategory = CATEGORY.OTHERS;
        }
        else{
            parentCategory = tag.attr("content");
            parentCategory = CATEGORY.convert(parentCategory);
        }

        List<String> categoryList = new ArrayList<>();
        categoryList.add(parentCategory);

        // get child category
        Element childrenCategoryTag = doc.selectFirst(".breadcrumbs");
        if (childrenCategoryTag != null){
            Elements children = childrenCategoryTag.getElementsByTag("a");
            for (Element e: children){
                String category = e.attr("title");
                category = CATEGORY.convert(category);
                if (!categoryList.contains(category)){
                    categoryList.add(category);
                }
            }
        }
        return categoryList;
    }
}
