package business.Scraper;

import business.Helper.CSS;
import business.Sanitizer.MainContentFilter;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.safety.Safelist;
import org.jsoup.select.NodeFilter;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class TuoiTreScraper extends Scraper {
    private static final Category NEW = new Category(Category.NEW, "https://tuoitre.vn/", CSS.TUOITRE_TITLE_LINK);
    private static final Category COVID = new Category(Category.COVID, "https://tuoitre.vn/covid-19.html", CSS.TUOITRE_TITLE_LINK);
    private static final Category POLITICS = new Category(Category.POLITICS, "https://tuoitre.vn/thoi-su.htm", CSS.TUOITRE_TITLE_LINK);

    static {
        POLITICS.add("https://tuoitre.vn/thoi-su/but-bi.htm");
        POLITICS.add("https://tuoitre.vn/thoi-su/xa-hoi.htm");
        POLITICS.add("https://tuoitre.vn/thoi-su/phong-su.htm");
        POLITICS.add("https://tuoitre.vn/thoi-su/binh-luan.htm");
    }


    private static final Category BUSINESS = new Category(Category.BUSINESS, "https://tuoitre.vn/kinh-doanh.htm", CSS.TUOITRE_TITLE_LINK);

    static {
        BUSINESS.add("https://tuoitre.vn/kinh-doanh/tai-chinh.htm");
        BUSINESS.add("https://tuoitre.vn/kinh-doanh/doanh-nghiep.htm");
        BUSINESS.add("https://tuoitre.vn/kinh-doanh/mua-sam.htm");
        BUSINESS.add("https://tuoitre.vn/kinh-doanh/dau-tu.htm");
    }

    private static final Category TECHNOLOGY = new Category(Category.TECHNOLOGY, "https://tuoitre.vn/khoa-hoc.htm", CSS.TUOITRE_TITLE_LINK);

    static {
        TECHNOLOGY.add("https://tuoitre.vn/khoa-hoc/thuong-thuc.htm");
        TECHNOLOGY.add("https://tuoitre.vn/khoa-hoc/phat-minh.htm");
    }

    private static final Category HEALTH = new Category(Category.HEALTH, "https://tuoitre.vn/suc-khoe.htm", CSS.TUOITRE_TITLE_LINK);

    static {
        HEALTH.add("https://tuoitre.vn/suc-khoe/dinh-duong.htm");
        HEALTH.add("https://tuoitre.vn/suc-khoe/me-va-be.htm");
        HEALTH.add("https://tuoitre.vn/suc-khoe/gioi-tinh.htm");
        HEALTH.add("https://tuoitre.vn/suc-khoe/phong-mach.htm");
        HEALTH.add("https://tuoitre.vn/suc-khoe/biet-de-khoe.htm");
    }

    private static final Category SPORTS = new Category(Category.SPORTS, "https://tuoitre.vn/the-thao.htm", CSS.TUOITRE_TITLE_LINK);

    static {
        SPORTS.add("https://tuoitre.vn/the-thao/bong-da.htm");
        SPORTS.add("https://tuoitre.vn/the-thao/bong-ro.htm");
        SPORTS.add("https://tuoitre.vn/the-thao/vo-thuat.htm");
        SPORTS.add("https://tuoitre.vn/the-thao/cac-mon-khac.htm");
        SPORTS.add("https://tuoitre.vn/the-thao/khoe-360.htm");
        SPORTS.add("https://tuoitre.vn/the-thao/nguoi-ham-mo.htm");
    }

    private static final Category ENTERTAINMENT = new Category(Category.ENTERTAINMENT, "https://tuoitre.vn/giai-tri.htm", CSS.TUOITRE_TITLE_LINK);

    static {
        ENTERTAINMENT.add("https://tuoitre.vn/giai-tri/nghe-gi-hom-nay.htm");
        ENTERTAINMENT.add("https://tuoitre.vn/giai-tri/am-nhac.htm");
        ENTERTAINMENT.add("https://tuoitre.vn/giai-tri/dien-anh.htm");
        ENTERTAINMENT.add("https://tuoitre.vn/giai-tri/tv-show.htm");
        ENTERTAINMENT.add("https://tuoitre.vn/giai-tri/thoi-trang.htm");
        ENTERTAINMENT.add("https://tuoitre.vn/giai-tri/hau-truong.htm");
    }

    private static final Category WORLD = new Category(Category.WORLD, "https://tuoitre.vn/the-gioi.htm", CSS.TUOITRE_TITLE_LINK);

    static {
        WORLD.add("https://tuoitre.vn/the-gioi/binh-luan.htm");
        WORLD.add("https://tuoitre.vn/the-gioi/kieu-bao.htm");
        WORLD.add("https://tuoitre.vn/the-gioi/muon-mau.htm");
        WORLD.add("https://tuoitre.vn/the-gioi/ho-so.htm");
    }

    private static final Category OTHERS = new Category(Category.OTHERS, "", CSS.TUOITRE_TITLE_LINK);

    static {
        OTHERS.add("https://tuoitre.vn/phap-luat.htm");
        OTHERS.add("https://tuoitre.vn/xe.htm");
        OTHERS.add("https://dulich.tuoitre.vn/");
        OTHERS.add("https://tuoitre.vn/nhip-song-tre.htm");
        OTHERS.add("https://tuoitre.vn/van-hoa.htm");
        OTHERS.add("https://tuoitre.vn/giao-duc.htm");
        OTHERS.add("https://tuoitre.vn/gia-that.htm");
        OTHERS.add("https://tuoitre.vn/ban-doc-lam-bao.htm");
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

        CssConfiguration TuoiTreCssConfig = new CssConfiguration(
                "https://tuoitre.vn/",
                CSS.TUOITRE_TITLE,
                CSS.TUOITRE_DESCRIPTION,
                CSS.TUOITRE_BODY,
                CSS.TUOITRE_TIME,
                CSS.TUOITRE_PIC);
        return new TuoiTreScraper("Tuoi Tre",
                "https://dangkyxettuyennghe.tuoitre.vn/img/logo-tt.png",
                categories,
                TuoiTreCssConfig);
    }

    public TuoiTreScraper(String name,
                          String defaultThumbnail,
                          HashMap<String, Category> categories,
                          CssConfiguration cssConfiguration) {
        super(name, defaultThumbnail, categories, cssConfiguration);
    }

    @Override
    public Element scrapeMainContent(Document doc) throws ElementNotFound {
        Element authorTag = scrapeAuthor(doc, "author");
        if(authorTag != null){
            return super.scrapeMainContent(doc).append(authorTag.outerHtml());
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

        // Only scrape category in meta tag as categories cannot be scraped in breadcrumb
        String category = scrapeCategoryNamesInMeta(doc, "property", "article:section", "content");
        if (!StringUtils.isEmpty(category)){
            categoryList.add(category);
        }
        return categoryList;
    }

    @Override
    public NodeFilter getNodeFilter(Element root) {
        return new TuoiTreFilter(root);
    }

    static class TuoiTreFilter extends MainContentFilter {

        public TuoiTreFilter(Element root) {
            super(root);
        }

        @Override
        protected boolean isFigure(Element node) {
            return node.hasClass("VCSortableInPreviewMode")
                    && node.attr("type").equals("Photo");
        }

        @Override
        protected boolean isVideo(Element node) {
            return node.hasClass("VCSortableInPreviewMode")
                    && node.attr("type").equals("VideoStream");
        }

        @Override
        protected boolean isQuote(Element node) {
            if (node.hasClass("VCSortableInPreviewMode")){
                String type = node.attr("type");
                return type.equals("wrapnote")
                        || type.equals("SimpleQuote");
            }
            else {
                return false;
            }
        }

        @Override
        protected boolean isAuthor(Element node) {
            return false;
        }

        @Override
        protected Element getFilteredFigure(Element node) {
            Element figure = new Element("figure");

            // pull out img tag
            for (Element imgTag: node.getElementsByTag("img")){
                String src = imgTag.attr("src");

                if (StringUtils.isEmpty(src)){
                    continue;
                }

                String alt = imgTag.attr("alt");
                Element img = new Element("img")
                        .attr("src", src)
                        .attr("alt", alt);
                figure.append(img.outerHtml());
            }

            // pull out caption
            for (Element caption: node.getElementsByTag("p")){
                Element figcaption = new Element("figcaption")
                        .text(caption.text());
                figure.append(figcaption.outerHtml());
            }
            return figure;
        }

        @Override
        protected Element getFilteredVideo(Element node) {
            String rawUrl = node.attr("data-src");

            // extract the link from rawUrl by regex
            String regrex = "vid=(.*)mp4";
            Pattern pattern = Pattern.compile(regrex);
            Matcher matcher = pattern.matcher(rawUrl);
            if (matcher.find()) {
                String src = "https://hls.tuoitre.vn/" + matcher.group(1) + "mp4";
                Element source = new Element("source")
                        .attr("src", src)
                        .attr("controls", true);

                return new Element("video").appendChild(source);
            }
            else{
                return null;
            }
        }

        @Override
        protected Element getFilteredQuote(Element node) {
            node.clearAttributes();
            Safelist safelist = Safelist.basic();
            safelist.removeTags("a"); // no need to keep link in quote

            node.html(Jsoup.clean(node.html(), safelist));
            // add css class to the paragraphs in quote
            for (Element p: node.getElementsByTag("p")) {
                p.clearAttributes();
                // style paragraph containing author's name
                if (p.hasClass("StarNameCaption")){
                    p.tagName("span");
                    p.attr("style", "text-align:right;");
                }
            }
            return node;
        }

        @Override
        protected Element getFilteredAuthor(Element node) {
            return null;
        }

        @Override
        protected boolean skip(Element node) {
            return (node.attr("type").equals("RelatedOneNews"));
        }

    }
}
