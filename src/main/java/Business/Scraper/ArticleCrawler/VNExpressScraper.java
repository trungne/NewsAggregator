package Business.Scraper.ArticleCrawler;

import Business.Scraper.Helper.CSS;
import Business.Scraper.Sanitizer.MainContentFilter;
import Business.Scraper.LinksCrawler.LinksCrawler;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Safelist;
import org.jsoup.select.Elements;
import org.jsoup.select.NodeFilter;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class VNExpressScraper extends Scraper {
    // TODO: Thai comments this
    public static Scraper init() {

        LinksCrawler linksCrawler;
        try{
            linksCrawler = new LinksCrawler("https://vnexpress.net/",
                    "main-nav",
                    CSS.VNEXPRESS_TITLE_LINK);
        } catch (IOException err) {
            return null;
        }
        CssConfiguration VNExpressConfig = new CssConfiguration(
                "https://vnexpress.net/",
                CSS.VNEXPRESS_TITLE,
                CSS.VNEXPRESS_DESCRIPTION,
                CSS.VNEXPRESS_BODY,
                CSS.VNEXPRESS_TIME,
                CSS.VNEXPRESS_PIC);
        return new VNExpressScraper("VNExpress",
                "https://s1.vnecdn.net/vnexpress/restruct/i/v420/logo_default.jpg",
                VNExpressConfig,
                linksCrawler);
    }

    public VNExpressScraper(String name,
                            String defaultThumbnail,
                            CssConfiguration cssConfiguration,
                            LinksCrawler linksCrawler) {
        super(name, defaultThumbnail, cssConfiguration, linksCrawler);
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