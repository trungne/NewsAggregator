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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VNExpress extends NewsOutlet {
    private static final Category COVID = new SubCategory(CATEGORY.COVID, "https://vnexpress.net/covid-19/tin-tuc", CSS.VNEXPRESS_TITLE_LINK);
    private static final Category POLITICS = new SubCategory(CATEGORY.POLITICS, "https://vnexpress.net/thoi-su/chinh-tri", CSS.VNEXPRESS_TITLE_LINK);
    private static final Category BUSINESS = new MainCategory(CATEGORY.BUSINESS, "https://vnexpress.net/kinh-doanh", CSS.VNEXPRESS_TITLE_LINK);

    static {
        BUSINESS.addSub("https://vnexpress.net/kinh-doanh/quoc-te");
        BUSINESS.addSub("https://vnexpress.net/kinh-doanh/doanh-nghiep");
        BUSINESS.addSub("https://vnexpress.net/kinh-doanh/chung-khoan");
        BUSINESS.addSub("https://vnexpress.net/kinh-doanh/bat-dong-san");
        BUSINESS.addSub("https://vnexpress.net/kinh-doanh/ebank");
        BUSINESS.addSub("https://vnexpress.net/kinh-doanh/vi-mo");
        BUSINESS.addSub("https://vnexpress.net/kinh-doanh/tien-cua-toi");
        BUSINESS.addSub("https://vnexpress.net/kinh-doanh/bao-hiem");
        BUSINESS.addSub("https://vnexpress.net/kinh-doanh/hang-hoa");
        BUSINESS.addSub("https://vnexpress.net/kinh-doanh/e-commerce-40");
    }

    private static final Category TECHNOLOGY = new MainCategory(CATEGORY.TECHNOLOGY, "https://vnexpress.net/khoa-hoc", CSS.VNEXPRESS_TITLE_LINK);

    static {
        TECHNOLOGY.addSub("https://vnexpress.net/khoa-hoc/tin-tuc");
        TECHNOLOGY.addSub("https://vnexpress.net/khoa-hoc/phat-minh");
        TECHNOLOGY.addSub("https://vnexpress.net/khoa-hoc/ung-dung");
        TECHNOLOGY.addSub("https://vnexpress.net/khoa-hoc/the-gioi-tu-nhien");
        TECHNOLOGY.addSub("https://vnexpress.net/khoa-hoc/thuong-thuc");
        TECHNOLOGY.addSub("https://vnexpress.net/khoa-hoc/khoa-hoc-trong-nuoc");
    }

    private static final Category HEALTH = new MainCategory(CATEGORY.HEALTH, "https://vnexpress.net/suc-khoe", CSS.VNEXPRESS_TITLE_LINK);

    static {
        HEALTH.addSub("https://vnexpress.net/suc-khoe/tin-tuc");
        HEALTH.addSub("https://vnexpress.net/suc-khoe/tu-van");
        HEALTH.addSub("https://vnexpress.net/suc-khoe/dinh-duong");
        HEALTH.addSub("https://vnexpress.net/suc-khoe/khoe-dep");
        HEALTH.addSub("https://vnexpress.net/suc-khoe/dan-ong");
        HEALTH.addSub("https://vnexpress.net/suc-khoe/cac-benh");
        HEALTH.addSub("https://vnexpress.net/suc-khoe/vaccine");
    }

    private static final Category SPORTS = new MainCategory(CATEGORY.SPORTS, "https://vnexpress.net/the-thao", CSS.VNEXPRESS_TITLE_LINK);

    static {
        SPORTS.addSub("https://vnexpress.net/the-thao/video");
        SPORTS.addSub("https://vnexpress.net/bong-da");
        SPORTS.addSub("https://vnexpress.net/the-thao/v-league");
        SPORTS.addSub("https://vnexpress.net/the-thao/cac-mon-khac");
    }

    private static final Category ENTERTAINMENT = new MainCategory(CATEGORY.ENTERTAINMENT, "https://vnexpress.net/giai-tri", CSS.VNEXPRESS_TITLE_LINK);

    static {
        ENTERTAINMENT.addSub("https://vnexpress.net/giai-tri/gioi-sao");
        ENTERTAINMENT.addSub("https://vnexpress.net/giai-tri/phim");
        ENTERTAINMENT.addSub("https://vnexpress.net/giai-tri/nhac");
        ENTERTAINMENT.addSub("https://vnexpress.net/giai-tri/thoi-trang");
        ENTERTAINMENT.addSub("https://vnexpress.net/giai-tri/lam-dep");
        ENTERTAINMENT.addSub("https://vnexpress.net/giai-tri/sach");
        ENTERTAINMENT.addSub("https://vnexpress.net/giai-tri/san-khau-my-thuat");
    }

    private static final Category WORLD = new MainCategory(CATEGORY.WORLD, "https://vnexpress.net/the-gioi", CSS.VNEXPRESS_TITLE_LINK);

    static {
        WORLD.addSub("https://vnexpress.net/the-gioi/tu-lieu");
        WORLD.addSub("https://vnexpress.net/the-gioi/phan-tich");
        WORLD.addSub("https://vnexpress.net/the-gioi/nguoi-viet-5-chau");
        WORLD.addSub("https://vnexpress.net/the-gioi/cuoc-song-do-day");
        WORLD.addSub("https://vnexpress.net/the-gioi/quan-su");
    }

    private static final Category OTHERS = new MainCategory(CATEGORY.OTHERS, "", CSS.VNEXPRESS_TITLE_LINK);

    static {
        OTHERS.addSub("https://vnexpress.net/giao-duc");
        OTHERS.addSub("https://vnexpress.net/thoi-su");
        OTHERS.addSub("https://vnexpress.net/goc-nhin");
        OTHERS.addSub("https://vnexpress.net/phap-luat");
        OTHERS.addSub("https://vnexpress.net/doi-song");
        OTHERS.addSub("https://vnexpress.net/du-lich");
        OTHERS.addSub("https://vnexpress.net/so-hoa");
        OTHERS.addSub("https://vnexpress.net/oto-xe-may");
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

        CssConfiguration VNExpressConfig = new CssConfiguration(
                "https://vnexpress.net/",
                CSS.VNEXPRESS_TITLE,
                CSS.VNEXPRESS_DESCRIPTION,
                CSS.VNEXPRESS_BODY,
                CSS.VNEXPRESS_TIME,
                CSS.VNEXPRESS_PIC);
        return new VNExpress("VNExpress",
                "https://s1.vnecdn.net/vnexpress/restruct/i/v395/logo_default.jpg",
                categories,
                VNExpressConfig,
                new VNExpressSanitizer());
    }

    public VNExpress(String name, String defaultThumbnail, HashMap<String, Category> categories, CssConfiguration cssConfiguration, HtmlSanitizer sanitizer) {
        super(name, defaultThumbnail, categories, cssConfiguration, sanitizer);
    }

    @Override
    public LocalDateTime getPublishedTime(Document doc) {
        Elements dateTimeTag = doc.getElementsByAttributeValue("itemprop", cssConfiguration.publishedTime);
        String dateTimeStr = dateTimeTag.attr("content");

        if (StringUtils.isEmpty(dateTimeStr)) {
            return LocalDateTime.now();
        }

        return LocalDateTimeParser.parse(dateTimeStr);
    }

    @Override
    public List<String> getCategoryNames(Document doc) {
        // scrape the parent category in meta tag
        Element parentTag = doc.getElementsByAttributeValue("name", "tt_site_id_detail").first();
        String parentCategory;

        // if no parent tag is found, set the category to default "others"
        if (parentTag == null) {
            parentCategory = CATEGORY.OTHERS;
        } else {
            parentCategory = parentTag.attr("catename");
            parentCategory = CATEGORY.convert(parentCategory);
        }

        List<String> categoryList = new ArrayList<>();
        categoryList.add(parentCategory);

        // scape all categories in body
        Element tag = doc.selectFirst(".breadcrumb");

        if (tag != null) {
            Elements categoryTags = tag.getElementsByTag("a");
            for (Element e : categoryTags) {
                String category = e.attr("title");
                category = CATEGORY.convert(category);
                if (!categoryList.contains(category)) {
                    categoryList.add(category);
                }
            }
        }
        return categoryList;
    }
}