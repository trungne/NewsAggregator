package Business.Scraper.Sanitizer;

import Business.Scraper.Helper.CSS;
import Business.Scraper.Sanitizer.MainContentFilter;
import Business.Scraper.LinksCrawler.Category;
import Business.Scraper.LinksCrawler.LinksCrawler;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.safety.Safelist;
import org.jsoup.select.Elements;
import org.jsoup.select.NodeFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


public final class ZingNewsFilter extends MainContentFilter{
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
    protected boolean isAuthor(Element node) {
        return false;
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
    protected Element getFilteredAuthor(Element node) {
        return null;
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

// "https://brandcom.vn/wp-content/uploads/2016/02/zingnews-logo.png"



//    public Element scrapeMainContent(Document doc) throws ElementNotFound {
//        Element authorTag = scrapeAuthor(doc, "the-article-credit");
//        if (authorTag != null){
//            return super.scrapeMainContent(doc).append(authorTag.outerHtml());
//        }
//        return super.scrapeMainContent(doc);
//    }



