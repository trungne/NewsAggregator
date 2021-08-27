package business.Scraper.ArticleCrawler;

import business.Scraper.Helper.CSS;
import business.Scraper.Sanitizer.MainContentFilter;
import business.Scraper.LinksCrawler.Category;
import business.Scraper.LinksCrawler.LinksCrawler;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Safelist;
import org.jsoup.select.Elements;
import org.jsoup.select.NodeFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


public final class NhanDanScraper extends Scraper {
    // TODO: Thai comments this
    public static Scraper init() {
        LinksCrawler linksCrawler;
        try{
            linksCrawler = new LinksCrawler("https://nhandan.vn/",
                    "main-menu",
                    CSS.NHANDAN_TITLE_LINK);
        } catch (IOException err) {
            return null;
        }
        CssConfiguration NhanDanCssConfig = new CssConfiguration(
                "https://nhandan.vn/",
                CSS.NHANDAN_TITLE,
                CSS.NHANDAN_DESCRIPTION,
                CSS.NHANDAN_BODY,
                CSS.NHANDAN_TIME,
                CSS.NHANDAN_PIC);
        return new NhanDanScraper("Nhan Dan",
                "https://www.nhandan-printing.vn/datafiles_D_D/setmulti/nhandan_copy.jpg",
                NhanDanCssConfig,
                linksCrawler);
    }

    private final String thumbnailCss;

    public NhanDanScraper(String name,
                          String defaultThumbnail,
                          CssConfiguration cssConfiguration,
                          LinksCrawler linksCrawler) {
        super(name, defaultThumbnail, cssConfiguration, linksCrawler);
        this.thumbnailCss = CSS.NHANDAN_THUMBNAIL;
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
