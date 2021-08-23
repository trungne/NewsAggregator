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

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class VNExpressScraper extends Scraper {
    private static final Category NEW = new Category(Category.NEW, "https://vnexpress.net/", CSS.VNEXPRESS_TITLE_LINK);
    private static final Category COVID = new Category(Category.COVID, "https://vnexpress.net/covid-19/tin-tuc", CSS.VNEXPRESS_TITLE_LINK);
    private static final Category POLITICS = new Category(Category.POLITICS, "https://vnexpress.net/thoi-su/chinh-tri", CSS.VNEXPRESS_TITLE_LINK);
    private static final Category BUSINESS = new Category(Category.BUSINESS, "https://vnexpress.net/kinh-doanh", CSS.VNEXPRESS_TITLE_LINK);

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

    private static final Category TECHNOLOGY = new Category(Category.TECHNOLOGY, "https://vnexpress.net/khoa-hoc", CSS.VNEXPRESS_TITLE_LINK);

    static {
        TECHNOLOGY.add("https://vnexpress.net/khoa-hoc/tin-tuc");
        TECHNOLOGY.add("https://vnexpress.net/khoa-hoc/phat-minh");
        TECHNOLOGY.add("https://vnexpress.net/khoa-hoc/ung-dung");
        TECHNOLOGY.add("https://vnexpress.net/khoa-hoc/the-gioi-tu-nhien");
        TECHNOLOGY.add("https://vnexpress.net/khoa-hoc/thuong-thuc");
        TECHNOLOGY.add("https://vnexpress.net/khoa-hoc/khoa-hoc-trong-nuoc");
    }

    private static final Category HEALTH = new Category(Category.HEALTH, "https://vnexpress.net/suc-khoe", CSS.VNEXPRESS_TITLE_LINK);

    static {
        HEALTH.add("https://vnexpress.net/suc-khoe/tin-tuc");
        HEALTH.add("https://vnexpress.net/suc-khoe/tu-van");
        HEALTH.add("https://vnexpress.net/suc-khoe/dinh-duong");
        HEALTH.add("https://vnexpress.net/suc-khoe/khoe-dep");
        HEALTH.add("https://vnexpress.net/suc-khoe/dan-ong");
        HEALTH.add("https://vnexpress.net/suc-khoe/cac-benh");
        HEALTH.add("https://vnexpress.net/suc-khoe/vaccine");
    }

    private static final Category SPORTS = new Category(Category.SPORTS, "https://vnexpress.net/the-thao", CSS.VNEXPRESS_TITLE_LINK);

    static {
        SPORTS.add("https://vnexpress.net/the-thao/video");
        SPORTS.add("https://vnexpress.net/bong-da");
        SPORTS.add("https://vnexpress.net/the-thao/v-league");
        SPORTS.add("https://vnexpress.net/the-thao/cac-mon-khac");
    }

    private static final Category ENTERTAINMENT = new Category(Category.ENTERTAINMENT, "https://vnexpress.net/giai-tri", CSS.VNEXPRESS_TITLE_LINK);

    static {
        ENTERTAINMENT.add("https://vnexpress.net/giai-tri/gioi-sao");
        ENTERTAINMENT.add("https://vnexpress.net/giai-tri/phim");
        ENTERTAINMENT.add("https://vnexpress.net/giai-tri/nhac");
        ENTERTAINMENT.add("https://vnexpress.net/giai-tri/thoi-trang");
        ENTERTAINMENT.add("https://vnexpress.net/giai-tri/lam-dep");
        ENTERTAINMENT.add("https://vnexpress.net/giai-tri/sach");
        ENTERTAINMENT.add("https://vnexpress.net/giai-tri/san-khau-my-thuat");
    }

    private static final Category WORLD = new Category(Category.WORLD, "https://vnexpress.net/the-gioi", CSS.VNEXPRESS_TITLE_LINK);

    static {
        WORLD.add("https://vnexpress.net/the-gioi/tu-lieu");
        WORLD.add("https://vnexpress.net/the-gioi/phan-tich");
        WORLD.add("https://vnexpress.net/the-gioi/nguoi-viet-5-chau");
        WORLD.add("https://vnexpress.net/the-gioi/cuoc-song-do-day");
        WORLD.add("https://vnexpress.net/the-gioi/quan-su");
    }

    private static final Category OTHERS = new Category(Category.OTHERS, "", CSS.VNEXPRESS_TITLE_LINK);

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

        CssConfiguration VNExpressConfig = new CssConfiguration(
                "https://vnexpress.net/",
                CSS.VNEXPRESS_TITLE,
                CSS.VNEXPRESS_DESCRIPTION,
                CSS.VNEXPRESS_BODY,
                CSS.VNEXPRESS_TIME,
                CSS.VNEXPRESS_PIC);
        return new VNExpressScraper("VNExpress",
                "https://s1.vnecdn.net/vnexpress/restruct/i/v395/logo_default.jpg",
                categories,
                VNExpressConfig);
    }

    public VNExpressScraper(String name,
                            String defaultThumbnail,
                            HashMap<String, Category> categories,
                            CssConfiguration cssConfiguration) {
        super(name, defaultThumbnail, categories, cssConfiguration);
    }

    @Override
    public LocalDateTime scrapePublishedTime(Document doc) {
        return scrapePublishedTimeFromMeta(doc, "itemprop", cssConfiguration.publishedTime, "content");
    }

    @Override
    public Set<String> scrapeCategoryNames(Document doc) {
        Set<String> categoryList = new HashSet<>();
        // scrape category in meta tag
        String categoryInMeta = scrapeCategoryNamesInMeta(doc, "name", "tt_site_id_detail", "catename");
        if (!StringUtils.isEmpty(categoryInMeta)){
            categoryList.add(categoryInMeta);
        }

        // scrape category in breadcrumb
        categoryList.addAll(scrapeCategoryNamesInBreadcrumb(doc, "breadcrumb"));
        return categoryList;
    }

    @Override
    public Element sanitizeDescription(Element e){
        // clean the tag with basic safelist
        Element newHtmlElement = new Element("p").html(Jsoup.clean(e.html(), Safelist.basic()));

        // deal with span tag (for location)
        Elements spanTags = newHtmlElement.getElementsByTag("span");

        spanTags.tagName("strong");
        for (Element span : spanTags) {
            span.addClass(CSS.LOCATION);
            span.text(span.text() + " - ");
        }

        return newHtmlElement;
    }

    @Override
    public NodeFilter getNodeFilter(Element root) {
        return new VNExpressFilter(root);
    }

    static class VNExpressFilter extends MainContentFilter {
        public VNExpressFilter(Element root) {
            super(root);
        }

        @Override
        protected boolean isFigure(Element node) {
            return node.tagName().equals("figure") && node.attr("itemprop").contains("image");
        }

        @Override
        protected boolean isVideo(Element node) {
            return node.tagName().equals("video");
        }

        @Override
        protected boolean isQuote(Element node) {
            return false;
        }

        @Override
        protected boolean isAuthor(Element node) {
            return false;
        }

        @Override
        protected Element getFilteredFigure(Element node) {
            node.clearAttributes();
            // get img and caption in figure tag
            // assign data-src attr to src (VNExpress stores their img url in data-src for god knows why)
            // clean the figure tag by safelist
            for(Element img: node.getElementsByTag("img")){
                String src = img.attr("data-src");
                if (!StringUtils.isEmpty(src)){
                    img.attr("src", src);
                }
            }
            // unwrap all p tag in figcaption to avoid awkward newline between img and caption
            for (Element caption: node.getElementsByTag("figcaption")){
                for (Element p: caption.children()){
                    if (p.tagName().equals("p")){
                        p.unwrap();
                    }
                }
            }
            // clean the figure tag
            Safelist safelist = Safelist.basicWithImages();
            safelist.addTags("figcaption", "figure");

            // clean and add custom css class to the figure tag
            node.clearAttributes();
            node.html(Jsoup.clean(node.html(), safelist));
            return node;
        }

        @Override
        protected Element getFilteredVideo(Element node) {
            URL src;
            try {
                src = new URL(node.attr("src"));
            } catch (MalformedURLException e) {
                return null;
            }

            String protocol = src.getProtocol();
            String host = src.getHost();
            String file = src.getFile();

            host = host.replaceFirst(Pattern.quote("d1"), Matcher.quoteReplacement("v"));

            // remove an extra /video from file path
            file = file.replaceFirst(Pattern.quote("/video"), Matcher.quoteReplacement(""));

            // remove resolution from file path
            file = file.replaceFirst(Pattern.quote("/,240p,360p,480p,,"), Matcher.quoteReplacement(""));
            file = file.replaceFirst(Pattern.quote("/,240p,360p,480p,720p,"), Matcher.quoteReplacement(""));
            file = file.replaceFirst(Pattern.quote("/,240p,360p,,"), Matcher.quoteReplacement(""));
            file = file.replaceFirst(Pattern.quote("/,240p,,"), Matcher.quoteReplacement(""));

            // change extension to mp4
            file = file.replaceFirst(Pattern.quote("/vne/master.m3u8"), Matcher.quoteReplacement(".mp4"));

            try {
                src = new URL(protocol, host, file);
            } catch (MalformedURLException e) {
                return null;
            }

            Element newVideo = new Element("video");
            newVideo.attr("controls", true);
            newVideo.attributes();
            Element newVideoSrc = new Element("source");
            newVideoSrc.attr("src", src.toString());

            newVideo.appendChild(newVideoSrc);
            return newVideo;
        }

        @Override
        protected Element getFilteredQuote(Element node) {
            return null;
        }

        @Override
        protected Element getFilteredAuthor(Element node) {
            return null;
        }

        @Override
        protected boolean skip(Element node) {
            return node.hasClass("box_img_video")
                    || (node.attr("style").contains("display: none"));
        }
    }
}