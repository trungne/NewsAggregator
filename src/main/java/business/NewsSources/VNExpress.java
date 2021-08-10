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
    private static final Category COVID = new Category(CATEGORY.COVID, "https://vnexpress.net/covid-19/tin-tuc", CSS.VNEXPRESS_TITLE_LINK);
    private static final Category POLITICS = new Category(CATEGORY.POLITICS, "https://vnexpress.net/thoi-su/chinh-tri", CSS.VNEXPRESS_TITLE_LINK);
    private static final Category BUSINESS = new Category(CATEGORY.BUSINESS, "https://vnexpress.net/kinh-doanh", CSS.VNEXPRESS_TITLE_LINK);
    static {
        BUSINESS.add("https://vnexpress.net/kinh-doanh/quoc-te");
        BUSINESS.add("https://vnexpress.net/kinh-doanh/doanh-nghiep");
        BUSINESS.add("https://vnexpress.net/kinh-doanh/chung-khoan");
        BUSINESS.add("https://vnexpress.net/kinh-doanh/bat-dong-san");
        BUSINESS.add("https://vnexpress.net/kinh-doanh/ebank");
        BUSINESS.add("https://vnexpress.net/kinh-doanh/vi-mo");
        BUSINESS.add("https://vnexpress.net/kinh-doanh/tien-cua-toi");
        BUSINESS.add("https://vnexpress.net/kinh-doanh/bao-hiem");
        BUSINESS.add("https://vnexpress.net/kinh-doanh/hang-hoa");
        BUSINESS.add("https://vnexpress.net/kinh-doanh/e-commerce-40");
    }

    private static final Category TECHNOLOGY = new Category(CATEGORY.TECHNOLOGY, "https://vnexpress.net/khoa-hoc", CSS.VNEXPRESS_TITLE_LINK);
    static {
        TECHNOLOGY.add("https://vnexpress.net/khoa-hoc/tin-tuc");
        TECHNOLOGY.add("https://vnexpress.net/khoa-hoc/phat-minh");
        TECHNOLOGY.add("https://vnexpress.net/khoa-hoc/ung-dung");
        TECHNOLOGY.add("https://vnexpress.net/khoa-hoc/the-gioi-tu-nhien");
        TECHNOLOGY.add("https://vnexpress.net/khoa-hoc/thuong-thuc");
        TECHNOLOGY.add("https://vnexpress.net/khoa-hoc/khoa-hoc-trong-nuoc");
    }

    private static final Category HEALTH = new Category(CATEGORY.HEALTH, "https://vnexpress.net/suc-khoe", CSS.VNEXPRESS_TITLE_LINK);

    static {
        HEALTH.add("https://vnexpress.net/suc-khoe/tin-tuc");
        HEALTH.add("https://vnexpress.net/suc-khoe/tu-van");
        HEALTH.add("https://vnexpress.net/suc-khoe/dinh-duong");
        HEALTH.add("https://vnexpress.net/suc-khoe/khoe-dep");
        HEALTH.add("https://vnexpress.net/suc-khoe/dan-ong");
        HEALTH.add("https://vnexpress.net/suc-khoe/cac-benh");
        HEALTH.add("https://vnexpress.net/suc-khoe/vaccine");
    }

    private static final Category SPORTS = new Category(CATEGORY.SPORTS, "https://vnexpress.net/the-thao", CSS.VNEXPRESS_TITLE_LINK);
    static {
        SPORTS.add("https://vnexpress.net/the-thao/video");
        SPORTS.add("https://vnexpress.net/bong-da");
        SPORTS.add("https://vnexpress.net/the-thao/v-league");
        SPORTS.add("https://vnexpress.net/the-thao/cac-mon-khac");
    }

    private static final Category ENTERTAINMENT = new Category(CATEGORY.ENTERTAINMENT, "https://vnexpress.net/giai-tri", CSS.VNEXPRESS_TITLE_LINK);
    static {
        ENTERTAINMENT.add("https://vnexpress.net/giai-tri/gioi-sao");
        ENTERTAINMENT.add("https://vnexpress.net/giai-tri/phim");
        ENTERTAINMENT.add("https://vnexpress.net/giai-tri/nhac");
        ENTERTAINMENT.add("https://vnexpress.net/giai-tri/thoi-trang");
        ENTERTAINMENT.add("https://vnexpress.net/giai-tri/lam-dep");
        ENTERTAINMENT.add("https://vnexpress.net/giai-tri/sach");
        ENTERTAINMENT.add("https://vnexpress.net/giai-tri/san-khau-my-thuat");
    }

    private static final Category WORLD = new Category(CATEGORY.WORLD, "https://vnexpress.net/the-gioi", CSS.VNEXPRESS_TITLE_LINK);
    static {
        WORLD.add("https://vnexpress.net/the-gioi/tu-lieu");
        WORLD.add("https://vnexpress.net/the-gioi/phan-tich");
        WORLD.add("https://vnexpress.net/the-gioi/nguoi-viet-5-chau");
        WORLD.add("https://vnexpress.net/the-gioi/cuoc-song-do-day");
        WORLD.add("https://vnexpress.net/the-gioi/quan-su");
    }

    private static final Category OTHERS = new Category(CATEGORY.OTHERS, "", CSS.VNEXPRESS_TITLE_LINK);
    static {
        OTHERS.add("https://vnexpress.net/giao-duc");
        OTHERS.add("https://vnexpress.net/thoi-su");
        OTHERS.add("https://vnexpress.net/goc-nhin");
        OTHERS.add("https://vnexpress.net/phap-luat");
        OTHERS.add("https://vnexpress.net/doi-song");
        OTHERS.add("https://vnexpress.net/du-lich");
        OTHERS.add("https://vnexpress.net/so-hoa");
        OTHERS.add("https://vnexpress.net/oto-xe-may");
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
        List<String> categoryList = new ArrayList<>();

        Element parentCategoryTag = doc.getElementsByAttributeValue("name","tt_site_id_detail").first();
        if (parentCategoryTag != null){
            String parentCategory = parentCategoryTag.attr("catename");
            parentCategory = CATEGORY.convert(parentCategory);
            if (!StringUtils.isEmpty(parentCategory))
                categoryList.add(parentCategory);
        }



        // scape all categories in body
        Element tag = doc.selectFirst(".breadcrumb");

        if (tag != null) {
            Elements categoryTags = tag.getElementsByTag("a");
            for (Element e : categoryTags) {
                String category = e.attr("title");
                category = CATEGORY.convert(category);

                if (StringUtils.isEmpty(category))
                    continue;

                if (!categoryList.contains(category)) {
                    categoryList.add(category);
                }
            }
        }

        if(categoryList.isEmpty()){
            categoryList.add(CATEGORY.OTHERS);
        }

        return categoryList;
    }
}