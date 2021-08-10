package business.NewsSources;

import business.Helper.CATEGORY;
import business.Helper.CSS;
import business.Sanitizer.HtmlSanitizer;
import business.Sanitizer.NhanDanSanitizer;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static business.Helper.Scraper.createCleanImgTag;
import static business.Helper.Scraper.scrapeFirstElementByClass;

public class NhanDan extends NewsOutlet {
    private static final Category COVID = new Category(CATEGORY.COVID, "https://nhandan.vn/tieu-diem", CSS.NHANDAN_TITLE_LINK);
    private static final Category POLITICS = new Category(CATEGORY.POLITICS, "https://nhandan.vn/chinhtri", CSS.NHANDAN_TITLE_LINK);
    static {
        POLITICS.add("https://nhandan.vn/tin-tuc-su-kien");
        POLITICS.add("https://nhandan.vn/xa-luan");
        POLITICS.add("https://nhandan.vn/cung-suy-ngam");
        POLITICS.add("https://nhandan.vn/binh-luan-phe-phan");
        POLITICS.add("https://nhandan.vn/nguoi-viet-xa-xu");
        POLITICS.add("https://nhandan.vn/dang-va-cuoc-song");
        POLITICS.add("https://nhandan.vn/dan-toc-mien-nui");
    }

    private static final Category BUSINESS = new Category(CATEGORY.BUSINESS, "https://nhandan.vn/kinhte", CSS.NHANDAN_TITLE_LINK);
    static {
        BUSINESS.add("https://nhandan.vn/tin-tuc-kinh-te");
        BUSINESS.add("https://nhandan.vn/nhan-dinh");
        BUSINESS.add("https://nhandan.vn/chuyen-lam-an");
        BUSINESS.add("https://nhandan.vn/chungkhoan");
        BUSINESS.add("https://nhandan.vn/hanggiahangthat");
    }

    private static final Category TECHNOLOGY = new Category(CATEGORY.TECHNOLOGY, "https://nhandan.vn/khoahoc-congnghe", CSS.NHANDAN_TITLE_LINK);
    static {
        TECHNOLOGY.add("https://nhandan.vn/khoa-hoc");
        TECHNOLOGY.add("https://nhandan.vn/vi-moi-truong-xanh");
        TECHNOLOGY.add("https://nhandan.vn/thong-tin-so");
    }

    private static final Category HEALTH = new Category(CATEGORY.HEALTH, "https://nhandan.vn/y-te", CSS.NHANDAN_TITLE_LINK);
    static {
        HEALTH.add("https://nhandan.vn/benh-thuong-gap");
        HEALTH.add("https://nhandan.vn/goc-tu-van");
        HEALTH.add("https://nhandan.vn/tin-tuc-y-te");
    }

    private static final Category SPORTS = new Category(CATEGORY.SPORTS, "https://nhandan.vn/thethao", CSS.NHANDAN_TITLE_LINK);
    static {
        SPORTS.add("https://nhandan.vn/nhip-song-the-thao");
        SPORTS.add("https://nhandan.vn/guong-mat");
        SPORTS.add("https://nhandan.vn/bong-da-viet-nam");
        SPORTS.add("https://nhandan.vn/bong-da-quoc-te");
    }

    private static final Category ENTERTAINMENT = new Category(CATEGORY.ENTERTAINMENT, "https://nhandan.vn/vanhoa", CSS.NHANDAN_TITLE_LINK);
    static {
        ENTERTAINMENT.add("https://nhandan.vn/dong-chay");
        ENTERTAINMENT.add("https://nhandan.vn/dien-dan");
        ENTERTAINMENT.add("https://nhandan.vn/nghe-doc-xem");
        ENTERTAINMENT.add("https://nhandan.vn/di-san");
        ENTERTAINMENT.add("https://nhandan.vn/chan-dung");
    }

    private static final Category WORLD = new Category(CATEGORY.WORLD, "https://nhandan.vn/thegioi", CSS.NHANDAN_TITLE_LINK);
    static {
        WORLD.add("https://nhandan.vn/cua-so-the-gioi");
        WORLD.add("https://nhandan.vn/cong-dong-asean");
        WORLD.add("https://nhandan.vn/binh-luan-quoc-te");
        WORLD.add("https://nhandan.vn/ho-so-tu-lieu");
        WORLD.add("https://nhandan.vn/chuyen-thoi-su");
        WORLD.add("https://nhandan.vn/tin-tuc-the-gioi");
    }

    private static final Category OTHERS = new Category(CATEGORY.OTHERS, "", CSS.NHANDAN_TITLE_LINK);
    static {
        OTHERS.add("https://nhandan.vn/phapluat");
        OTHERS.add("https://nhandan.vn/du-lich");
        OTHERS.add("https://nhandan.vn/giaoduc");
        OTHERS.add("https://nhandan.vn/bandoc");
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

        CssConfiguration NhanDanCssConfig = new CssConfiguration(
                "https://nhandan.vn/",
                CSS.NHANDAN_TITLE,
                CSS.NHANDAN_DESCRIPTION,
                CSS.NHANDAN_BODY,
                CSS.NHANDAN_TIME,
                CSS.NHANDAN_PIC);
        return new NhanDan("Nhan Dan",
                "https://www.nhandan-printing.vn/datafiles_D_D/setmulti/nhandan_copy.jpg",
                categories,
                NhanDanCssConfig,
                CSS.NHANDAN_THUMBNAIL,
                new NhanDanSanitizer());
    }

    private final String thumbnailCss;

    public NhanDan(String name,
                   String defaultThumbnail,
                   HashMap<String, Category> categories,
                   CssConfiguration cssConfiguration,
                   String thumbnailCss,
                   HtmlSanitizer sanitizer) {
        super(name, defaultThumbnail, categories, cssConfiguration, sanitizer);
        this.thumbnailCss = thumbnailCss;
    }

    @Override
    public LocalDateTime getPublishedTime(Document doc) {
        Elements dateTimeTags = doc.select("." + cssConfiguration.publishedTime);
        Element dateTimeTag = dateTimeTags.last();
        if (dateTimeTag == null)
            return LocalDateTime.now();

        String dateTimeStr = getDateTimeSubString(dateTimeTag.text());
        if (StringUtils.isEmpty(dateTimeStr)) {
            return LocalDateTime.now();
        }
        // time first
        // 0 7 : 3 0 0 4 - 0 6 -  2  0  2  1
        // 0 1 2 3 4 5 6 7 8 9 10 11 12 13 14
        if (dateTimeStr.substring(0, 5).contains(":")) {
            try {
                int hours = Integer.parseInt(dateTimeStr.substring(0, 2));
                int minutes = Integer.parseInt(dateTimeStr.substring(3, 5));
                int day = Integer.parseInt(dateTimeStr.substring(5, 7));
                int month = Integer.parseInt(dateTimeStr.substring(8, 10));
                int year = Integer.parseInt(dateTimeStr.substring(11));
                return (LocalDateTime.of(year, month, day, hours, minutes));
            } catch (NumberFormatException e) {
                e.printStackTrace();
                System.out.println(doc);
                return LocalDateTime.now();
            }
        }


        // date first
        // 1 0 - 0 7 - 2 0 2 1 0  8  :  4  6
        // 0 1 2 3 4 5 6 7 8 9 10 11 12 13 14
        try {
            int day = Integer.parseInt(dateTimeStr.substring(0, 2));
            int month = Integer.parseInt(dateTimeStr.substring(3, 5));
            int year = Integer.parseInt(dateTimeStr.substring(6, 10));
            int hours = Integer.parseInt(dateTimeStr.substring(10, 12));
            int minutes = Integer.parseInt(dateTimeStr.substring(13));
            return (LocalDateTime.of(year, month, day, hours, minutes));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            System.out.println(doc);
            return LocalDateTime.now();
        }

    }

    @Override
    public Element getThumbnail(Document doc) {
        try {
            Element elementContainsImgs = scrapeFirstElementByClass(doc, thumbnailCss);
            Element thumbnail = elementContainsImgs.getElementsByTag("img").first();
            thumbnail = createCleanImgTag(thumbnail);
            thumbnail = sanitizer.sanitizeThumbNail(thumbnail);
            return thumbnail;
        } catch (NullPointerException e) {
            return getDefaultThumbnail();
        }
    }

    @Override
    public List<String> getCategoryNames(Document doc) {
        Elements tags = doc.getElementsByClass("bc-item");
        List<String> categoryList = new ArrayList<>();
        if (!tags.isEmpty()) {
            for (Element e : tags) {
                String category = e.text();
                category = CATEGORY.convert(category);
                if (!categoryList.contains(category)) {
                    categoryList.add(category);
                }

            }
        } else {
            categoryList.add(CATEGORY.OTHERS);
        }

        return categoryList;


    }

    // remove day of the week from the datetime string.
    // Example Chủ Nhật, 10-07-2021, 08:45 into
    // 10-07-2021, 08:45
    private String getDateTimeSubString(String str) {
        str = str.trim();
        str = str.replaceAll(",", "");
        str = str.replaceAll("\\s+", "");
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            // get the substring from the first digit onwards
            if (Character.isDigit(ch) ||
                    ch == '-' ||
                    ch == ':') {
                builder.append(ch);
            }
        }
        return builder.toString();
    }
}
