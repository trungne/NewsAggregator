package business.Scraper;

import business.Helper.CSS;
import business.Helper.ScrapingUtils;
import business.Sanitizer.MainContentFilter;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.safety.Safelist;
import org.jsoup.select.Elements;
import org.jsoup.select.NodeFilter;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


public final class ZingNewsScraper extends Scraper {
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

        CssConfiguration ZingCssConfig = new CssConfiguration(
                "https://zingnews.vn/",
                CSS.ZING_TITLE,
                CSS.ZING_DESCRIPTION,
                CSS.ZING_BODY,
                CSS.ZING_TIME,
                CSS.ZING_PIC);
        return new ZingNewsScraper("ZingNews",
                "https://brandcom.vn/wp-content/uploads/2016/02/zingnews-logo.png",
                categories,
                ZingCssConfig);
    }


    public ZingNewsScraper(String name,
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
    public Element scrapeDescription(Document doc) throws ElementNotFound {
        return super.scrapeDescription(doc);
    }

    public Element scrapeMainContent(Document doc) throws ElementNotFound {
        Element authorTag = scrapeAuthor(doc, "the-article-credit");
        return super.scrapeMainContent(doc).append(authorTag.outerHtml());
    }

    @Override
    public Set<String> scrapeCategoryNames(Document doc) {
        // only scrape category in body as there is no category info in meta
        Element tag = doc.selectFirst(".the-article-category");
        Set<String> categoryList = new HashSet<>();
        if (tag != null) {
            Elements categoryTags = tag.getElementsByClass("parent_cate");
            for (Element e : categoryTags) {
                String category = e.attr("title");
                category = Category.convert(category);
                categoryList.add(category);
            }
        }
        return categoryList;
    }

    @Override
    public NodeFilter getNodeFilter(Element root) {
        return new ZingNewsFilter(root);
    }

    static class ZingNewsFilter extends MainContentFilter {
        public ZingNewsFilter(Element root) {
            super(root);
        }

        @Override
        protected boolean isFigure(Element node) {
            return node.hasClass("picture");
        }

        @Override
        protected boolean isVideo(Element node) {
            return node.hasClass("video");
        }

        @Override
        protected boolean isQuote(Element node) {
            return node.hasClass("notebook")
                    || node.tagName().equals("blockquote");
        }

        @Override
        protected Element getFilteredFigure(Element node) {
            // assign data-src attr to src for img tag
            for(Element img: node.getElementsByTag("img")){
                String src = img.attr("data-src");
                if (!StringUtils.isEmpty(src)){
                    img.attr("src", src);
                }
            }

            // change caption to figcaption to follow the convention
            for (Element e: node.getElementsByClass("caption")){
                e.clearAttributes();
                e.tagName("figcaption");
                // remove p tag but keep the text node
                for (Element p: e.getElementsByTag("p")){
                    p.unwrap();
                }
            }

            // clean the tag so that only img and figcaption tag remain
            Safelist safelist = Safelist.basicWithImages();
            safelist.addTags("figcaption");

            // create a figure tag to put img and figcaption in
            return new Element("figure")
                    .html(Jsoup.clean(node.html(), safelist));
        }

        @Override
        protected Element getFilteredVideo(Element node) {
            Element videoTag = getVideoTag(node);
            if (videoTag != null){
                // get caption of video
                Element caption = new Element("figcaption");
                for (Element figCaptionTag: node.getElementsByTag("figcaption")){
                    String clean = Jsoup.clean(figCaptionTag.html(), Safelist.none());
                    caption.appendText(clean);
                }

                videoTag.append(caption.outerHtml());
                return videoTag;
            }

            return null;

        }

        @Override
        protected Element getFilteredQuote(Element node) {
            Element quote = new Element("blockquote");
            for (Element p: node.getElementsByTag("p")){
                quote.appendChild(p.clearAttributes());
            }
            return quote;
        }

        @Override
        protected boolean skip(Element node) {
            return node.hasClass("inner-article")
                    || node.hasClass("covid-chart-widget")
                    || node.hasClass("z-widget-corona");
        }

        private static Element getVideoTag(Element tag) {
            // get the URL of video source
            String src = tag.attr("data-video-src");
            if (StringUtils.isEmpty(src)){
                return null;
            }
            Element video = new Element("video");
            video.attr("controls", "true");

            Element source = new Element("source");
            source.attr("src", src); // add attribute with value is url of video
            video.appendChild(source); // append <source> in to <video>

            return video;
        }

        @Override
        public FilterResult tail(Node node, int i) {
            return null;
        }
    }

}
