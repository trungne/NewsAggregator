package business.Scraper;

import business.Helper.CSS;
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
        return super.scrapeMainContent(doc).append(authorTag.outerHtml());
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

    static class TuoiTreFilter implements NodeFilter {
        Element root;

        public TuoiTreFilter(Element root) {
            this.root = root;
        }

        @Override
        public FilterResult head(Node node, int i) {
            if (!(node instanceof Element)) {
                return FilterResult.SKIP_ENTIRELY;
            }

            Element child = (Element) node;
            String tagName = child.tagName();

            // skip relevant news tag
            if (child.attr("type").equals("RelatedOneNews")){
                return FilterResult.SKIP_ENTIRELY;
            }

            if (tagName.equals("p")) {
                child.clearAttributes();
                Safelist safelist = Safelist.basic();
                // clean html in the tag and add custom css class for paragraph
                child.html(Jsoup.clean(child.html(), safelist)).addClass(CSS.PARAGRAPH);
                root.append(child.outerHtml());
            }
            // simple quote
            else if (child.hasClass("VCSortableInPreviewMode")
                    && child.attr("type").equals("SimpleQuote")){
                child.clearAttributes();
                Safelist safelist = Safelist.basic();
                child.html(Jsoup.clean(child.html(), safelist)).addClass(CSS.QUOTE);

                // add css class to the paragraphs in quote
                for (Element p: child.getElementsByTag("p")) {
                    // style paragraph containing author's name
                    if (p.hasClass("StarNameCaption")){
                        p.clearAttributes();
                        p.tagName("span");
                        p.attr("style", "text-align:right;");
                    }
                    else{
                        p.clearAttributes();
                        p.addClass(CSS.PARAGRAPH);
                    }
                }
                root.append(child.outerHtml());
            }
            // wrapnote
            else if (child.hasClass("VCSortableInPreviewMode")
                    && child.attr("type").equals("wrapnote")){
                child.clearAttributes();

                Safelist safelist = Safelist.basic();
                safelist.removeTags("a");

                child.html(Jsoup.clean(child.html(), safelist)).addClass(CSS.QUOTE);
                root.append(child.outerHtml());
            }
            // img
            else if (child.hasClass("VCSortableInPreviewMode")
                    && child.attr("type").equals("Photo")){
                Element figure = new Element("figure");

                // pull out img tag
                for (Element imgTag: child.getElementsByTag("img")){
                    String src = imgTag.attr("src");
                    String alt = imgTag.attr("alt");
                    Element img = new Element("img")
                            .attr("src", src)
                            .attr("alt", alt);
                    figure.append(img.outerHtml());
                }

                // pull out caption
                for (Element caption: child.getElementsByTag("p")){
                    Element figcaption = new Element("figcaption")
                            .text(caption.text());
                    figure.append(figcaption.outerHtml());
                }

                root.append(figure.outerHtml());
            }
            else if (child.hasClass("VCSortableInPreviewMode")
                    && child.attr("type").equals("VideoStream")) {
                String rawUrl = child.attr("data-src");

                // extract the link by regex
                String regrex = "vid=(.*)mp4";
                Pattern pattern = Pattern.compile(regrex);
                Matcher matcher = pattern.matcher(rawUrl);
                if (matcher.find()) {
                    String src = "https://hls.tuoitre.vn/" + matcher.group(1) + "mp4";
                    Element video = new Element("video");
                    Element source = new Element("source");
                    source.attr("src", src);

                    video.appendChild(source)
                            .attr("controls", true)
                            .addClass(CSS.VIDEO);

                    root.append(video.outerHtml());
                }
            }
            else {
                return FilterResult.CONTINUE;
            }

            return FilterResult.SKIP_ENTIRELY;

        }

        @Override
        public FilterResult tail(Node node, int i) {
            return null;
        }
        // impossible to display the source
        private static Element filterVideoTag(Element tag) {
            String src = "=== I can not display the source because of this: &&&& ===";

            Element videoCaption = new Element("p").html(tag.getElementsByTag("p").html());
            String videoSource = "<source src='" + src + "'>";

            Element videoTag = new Element("video").html(videoSource);

            Element newWrapTag = new Element("div");
            newWrapTag.appendChild(videoTag);
            newWrapTag.appendChild(videoCaption);
            return newWrapTag;

        }
    }
}
