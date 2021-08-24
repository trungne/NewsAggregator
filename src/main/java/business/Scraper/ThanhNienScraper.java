package business.Scraper;

import business.Helper.CSS;
import business.Helper.ScrapingUtils;
import business.Sanitizer.MainContentFilter;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Safelist;
import org.jsoup.select.NodeFilter;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public final class ThanhNienScraper extends Scraper {
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


        CssConfiguration ThanhNienCssConfig = new CssConfiguration(
                "https://thanhnien.vn/",
                CSS.THANHNIEN_TITLE,
                CSS.THANHNIEN_DESCRIPTION,
                CSS.THANHNIEN_BODY,
                CSS.THANHNIEN_TIME,
                CSS.THANHNIEN_PIC);
        return new ThanhNienScraper("Thanh Nien",
                "https://static.thanhnien.vn/v2/App_Themes/images/logo-tn-2.png",
                categories,
                ThanhNienCssConfig);
    }

    public ThanhNienScraper(String name,
                            String defaultThumbnail,
                            HashMap<String, Category> categories,
                            CssConfiguration cssConfiguration) {
        super(name, defaultThumbnail, categories, cssConfiguration);
    }

    @Override
    public String scrapeThumbnail(Document doc) {
        Element thumbnailTag = doc.getElementById("contentAvatar");
        if (thumbnailTag != null){
            for (Element img: thumbnailTag.getElementsByTag("img")){
                if (!StringUtils.isEmpty(img.attr("src"))){
                    return img.attr("src");
                }
            }
        }

        return getDefaultThumbnail();
    }

    @Override
    public Element scrapeMainContent(Document doc) throws ElementNotFound {
        Element author = doc.selectFirst(".details__author__meta ");
        if (author != null){
            // author name is stored in h4 tag
            for (Element header: author.getElementsByTag("h4")){
                return super.scrapeMainContent(doc)
                        .appendChild(
                                new Element("p")
                                .attr("style", "text-align:right;")
                                .append("<strong>" + header.text() + "</strong>")
                        );
            }
        }
        return super.scrapeMainContent(doc);
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
    }

    @Override
    public NodeFilter getNodeFilter(Element root) {
        return new ThanhNienFilter(root);
    }
    static class ThanhNienFilter extends MainContentFilter {

        public ThanhNienFilter(Element root) {
            super(root);
        }

        @Override
        protected boolean isParagraph(Element node) {
            // paragraphs are contained in div tag without any classes or other div inside
            if (!node.className().isEmpty()
                    || !node.tagName().equals("div")
                    || !node.attributes().isEmpty()) {
                return false;
            }

            for (Element child : node.children()) {
                if (child.tagName().equals("div")
                        || child.tagName().equals("table")) { // table contains img
                    return false;
                }
            }
            return true;
        }

        @Override
        protected boolean isFigure(Element node) {
            // count thumbnail as a figure tag
            return node.hasClass("imagefull")
                    || node.id().equals("contentAvatar");
        }

        @Override
        protected boolean isVideo(Element node) {
            return node.hasClass("cms-video");
        }

        @Override
        protected boolean isQuote(Element node) {
            return node.hasClass("quote");
        }

        @Override
        protected boolean isAuthor(Element node) {
            return false;
        }

        @Override
        protected Element getFilteredParagraph(Element node) {
            return new Element("p")
                    .html(Jsoup.clean(node.html(), Safelist.basic()));
        }

        @Override
        protected Element getFilteredFigure(Element node) {
            Element figure = new Element("figure");
            // this loop always runs once as there is always 1 picture in this node
            for (Element img : node.getElementsByTag("img")) {
                Element imgTag = ScrapingUtils.createCleanImgTag(img);
                if (imgTag != null) {
                    figure.appendChild(imgTag);
                }

            }

            // this loop always runs once as there is always 1 caption in this node
            for (Element caption: node.getElementsByClass("imgcaption")){
                Element figcaption = new Element("figcaption");

                // first, find the source, append it and remove immediately
                for (Element src: caption.getElementsByClass("source")){
                    figcaption.appendChild(new Element("em").text(src.text()).prepend("<br>"));
                    src.remove();
                }

                // second, prepend all the remaining text, which is the caption
                figcaption.prependText(caption.text());
                figure.appendChild(figcaption);
            }

            // if no images are found, return null
            if (figure.children().isEmpty()) {
                return null;
            }

            return figure;
        }

        @Override
        protected Element getFilteredVideo(Element node) {
            String src = node.attr("data-video-src");
            if (StringUtils.isEmpty(src)) {
                return null;
            }

            Element source = new Element("source")
                    .attr("src", src);

            return new Element("video")
                    .attr("controls", true)
                    .appendChild(source);
        }

        @Override
        protected Element getFilteredQuote(Element node) {
            Element root = new Element("blockquote");
            return null;
        }

        @Override
        protected Element getFilteredAuthor(Element node) {
            return null;
        }

        @Override
        protected boolean skip(Element node) {
            return node.hasClass("sapo")
                    || node.hasClass("details__morenews");
        }
    }
}
