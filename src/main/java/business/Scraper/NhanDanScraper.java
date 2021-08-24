package business.Scraper;

import business.Helper.CSS;
import business.Sanitizer.MainContentFilter;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Safelist;
import org.jsoup.select.Elements;
import org.jsoup.select.NodeFilter;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


public final class NhanDanScraper extends Scraper {
    private static final Category NEW = new Category(Category.NEW, "https://nhandan.vn/", CSS.NHANDAN_TITLE_LINK);
    private static final Category COVID = new Category(Category.COVID, "https://nhandan.vn/tieu-diem", CSS.NHANDAN_TITLE_LINK);
    private static final Category POLITICS = new Category(Category.POLITICS, "https://nhandan.vn/chinhtri", CSS.NHANDAN_TITLE_LINK);

    static {
        POLITICS.add("https://nhandan.vn/tin-tuc-su-kien");
        POLITICS.add("https://nhandan.vn/xa-luan");
        POLITICS.add("https://nhandan.vn/cung-suy-ngam");
        POLITICS.add("https://nhandan.vn/binh-luan-phe-phan");
        POLITICS.add("https://nhandan.vn/nguoi-viet-xa-xu");
        POLITICS.add("https://nhandan.vn/dang-va-cuoc-song");
        POLITICS.add("https://nhandan.vn/dan-toc-mien-nui");
    }

    private static final Category BUSINESS = new Category(Category.BUSINESS, "https://nhandan.vn/kinhte", CSS.NHANDAN_TITLE_LINK);

    static {
        BUSINESS.add("https://nhandan.vn/tin-tuc-kinh-te");
        BUSINESS.add("https://nhandan.vn/nhan-dinh");
        BUSINESS.add("https://nhandan.vn/chuyen-lam-an");
        BUSINESS.add("https://nhandan.vn/chungkhoan");
        BUSINESS.add("https://nhandan.vn/hanggiahangthat");
    }

    private static final Category TECHNOLOGY = new Category(Category.TECHNOLOGY, "https://nhandan.vn/khoahoc-congnghe", CSS.NHANDAN_TITLE_LINK);

    static {
        TECHNOLOGY.add("https://nhandan.vn/khoa-hoc");
        TECHNOLOGY.add("https://nhandan.vn/vi-moi-truong-xanh");
        TECHNOLOGY.add("https://nhandan.vn/thong-tin-so");
    }

    private static final Category HEALTH = new Category(Category.HEALTH, "https://nhandan.vn/y-te", CSS.NHANDAN_TITLE_LINK);

    static {
        HEALTH.add("https://nhandan.vn/benh-thuong-gap");
        HEALTH.add("https://nhandan.vn/goc-tu-van");
        HEALTH.add("https://nhandan.vn/tin-tuc-y-te");
    }

    private static final Category SPORTS = new Category(Category.SPORTS, "https://nhandan.vn/thethao", CSS.NHANDAN_TITLE_LINK);

    static {
        SPORTS.add("https://nhandan.vn/nhip-song-the-thao");
        SPORTS.add("https://nhandan.vn/guong-mat");
        SPORTS.add("https://nhandan.vn/bong-da-viet-nam");
        SPORTS.add("https://nhandan.vn/bong-da-quoc-te");
    }

    private static final Category ENTERTAINMENT = new Category(Category.ENTERTAINMENT, "https://nhandan.vn/vanhoa", CSS.NHANDAN_TITLE_LINK);

    static {
        ENTERTAINMENT.add("https://nhandan.vn/dong-chay");
        ENTERTAINMENT.add("https://nhandan.vn/dien-dan");
        ENTERTAINMENT.add("https://nhandan.vn/nghe-doc-xem");
        ENTERTAINMENT.add("https://nhandan.vn/di-san");
        ENTERTAINMENT.add("https://nhandan.vn/chan-dung");
    }

    private static final Category WORLD = new Category(Category.WORLD, "https://nhandan.vn/thegioi", CSS.NHANDAN_TITLE_LINK);

    static {
        WORLD.add("https://nhandan.vn/cua-so-the-gioi");
        WORLD.add("https://nhandan.vn/cong-dong-asean");
        WORLD.add("https://nhandan.vn/binh-luan-quoc-te");
        WORLD.add("https://nhandan.vn/ho-so-tu-lieu");
        WORLD.add("https://nhandan.vn/chuyen-thoi-su");
        WORLD.add("https://nhandan.vn/tin-tuc-the-gioi");
    }

    private static final Category OTHERS = new Category(Category.OTHERS, "", CSS.NHANDAN_TITLE_LINK);

    static {
        OTHERS.add("https://nhandan.vn/phapluat");
        OTHERS.add("https://nhandan.vn/du-lich");
        OTHERS.add("https://nhandan.vn/giaoduc");
        OTHERS.add("https://nhandan.vn/bandoc");
    }

    public static Scraper init() {
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

        CssConfiguration NhanDanCssConfig = new CssConfiguration(
                "https://nhandan.vn/",
                CSS.NHANDAN_TITLE,
                CSS.NHANDAN_DESCRIPTION,
                CSS.NHANDAN_BODY,
                CSS.NHANDAN_TIME,
                CSS.NHANDAN_PIC);
        return new NhanDanScraper("Nhan Dan",
                "https://www.nhandan-printing.vn/datafiles_D_D/setmulti/nhandan_copy.jpg",
                categories,
                NhanDanCssConfig,
                CSS.NHANDAN_THUMBNAIL);
    }

    private final String thumbnailCss;

    public NhanDanScraper(String name,
                          String defaultThumbnail,
                          HashMap<String, Category> categories,
                          CssConfiguration cssConfiguration,
                          String thumbnailCss) {
        super(name, defaultThumbnail, categories, cssConfiguration);
        this.thumbnailCss = thumbnailCss;
    }

    @Override
    public LocalDateTime scrapePublishedTime(Document doc) {
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
            return LocalDateTime.now();
        }

    }

    @Override
    public String scrapeThumbnail(Document doc) {
        String url = scrapeFirstImgUrl(doc, thumbnailCss);
        if (StringUtils.isEmpty(url)){
            return getDefaultThumbnail();
        }
        else{
            return url;
        }
    }

    @Override
    public Set<String> scrapeCategoryNames(Document doc) {
        Elements tags = doc.getElementsByClass("bc-item");
        Set<String> categoryList = new HashSet<>();
        if (!tags.isEmpty()) {
            for (Element e : tags) {
                String category = e.text();
                category = Category.convert(category);
                categoryList.add(category);
            }
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

    @Override
    public NodeFilter getNodeFilter(Element root) {
        return new NhanDanFilter(root);
    }

    static class NhanDanFilter extends MainContentFilter {
        public NhanDanFilter(Element root) {
            super(root);
        }

        @Override
        protected boolean isFigure(Element node) {
            return node.tagName().equals("figure");
        }

        @Override
        protected boolean isVideo(Element node) {
            return false;
        } // NhanDan doesn't have video in their articles

        @Override
        protected boolean isQuote(Element node) {
            return node.hasClass("blockquote");
        }

        @Override
        protected boolean isAuthor(Element node) {
            return node.hasClass("box-author");
        }

        @Override
        protected Element getFilteredFigure(Element node) {
            Safelist safelist = Safelist.basicWithImages();
            safelist.addTags("figcaption");
            node.html(Jsoup.clean(node.html(), safelist));
            return node;
        }

        @Override
        protected Element getFilteredVideo(Element node) {
            return null;
        }

        @Override
        protected Element getFilteredQuote(Element node) {
            return getFilteredParagraph(node);
        }

        @Override
        protected Element getFilteredAuthor(Element node) {
            return new Element("p")
                    .html(Jsoup.clean(node.html(), Safelist.simpleText()));
        }

        @Override
        protected boolean skip(Element node) {
            return node.hasClass(CSS.NHANDAN_DESCRIPTION);
        }
    }
}
