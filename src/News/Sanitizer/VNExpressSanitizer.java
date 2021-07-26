package News.Sanitizer;

import News.CSS;
import Scraper.Scraper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.safety.Safelist;
import org.jsoup.select.Elements;
import org.jsoup.select.NodeFilter;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class VNExpressSanitizer extends HtmlSanitizer {

    @Override
    protected Element sanitizeNonTitleTag(Element e, String type) {
        Safelist safelist; // modify this safe list according to the type
        String cleanHtml;
        Element newHtmlElement;

        switch (type) {
            case CSS.DESCRIPTION:
                safelist = Safelist.basic();
                cleanHtml = Jsoup.clean(e.html(), safelist);
                newHtmlElement = new Element("p").html(cleanHtml);

                // deal with span tag (for location)
                Elements spanTags = newHtmlElement.getElementsByTag("span");
                spanTags.tagName("strong");
                for (Element span : spanTags) {
                    span.addClass(CSS.LOCATION);
                    span.text(span.text() + " - ");
                }

                return newHtmlElement;

            case CSS.MAIN_CONTENT:
                Element newRoot = new Element("div"); //<div></div>

                NodeFilter VNExpressFilter = new VNExpressFilter(newRoot);
                NodeTraversor.filter(VNExpressFilter, e);

//                for (Element child: e.children()){
//                    if (child.tagName().equals("p") && child.hasClass("Normal")){
//                        Element para = child.clearAttributes();
//                        para.addClass(CSS.PARAGRAPH);
//                        System.out.println(para);
//                        newRoot.appendChild(para);
//                    }
//                    else if(child.tagName().equals("figure") && child.hasClass("tplCaption")){
//                        Element figure = VNExpressSanitizer.sanitizeFigureTag(child);
//                        if (figure == null) continue;
//                        figure.addClass(CSS.FIGURE);
//                        newRoot.appendChild(figure);
//                    }
//                    else if (child.tagName().equals("ul") && child.hasClass("list-news")){
//                        Element relevantNews = VNExpressSanitizer.sanitizeRelevantNewsTag(child);
//                        if (relevantNews == null) continue;
//                        relevantNews.addClass(CSS.RELEVANT_NEWS);
//                        newRoot.appendChild(relevantNews);
//                    }
//                    else if (child.tagName().equals("div")){
//                        Element videoTag;
//                        try {
//                            videoTag = child.getElementsByClass("videoContainter").first();
//                            videoTag = videoTag.getElementsByTag("video").first();
//                        } catch (NullPointerException err){
//                            continue;
//                        }
//
//
//                        videoTag = VNExpressSanitizer.sanitizeVideoTag(videoTag);
//                        if (videoTag == null) continue;
//
//                        videoTag.addClass(CSS.VIDEO);
//                        newRoot.appendChild(videoTag);
//                    }
//                }

                return newRoot;
            default:
                return e;
        }
    }
    protected static Element sanitizeRelevantNewsTag(Element tag){
        Element relevantNews = new Element("ul");

        // keep the href and text of <a> tag.
        // append the cleaned <a> tag to a new li tag
        // append the new li tag to the ul tag
        for (Element child: tag.getElementsByTag("li")){
            Element aTag = child.getElementsByTag("a").first();

            if(aTag == null || aTag.attr("href").isEmpty() || aTag.text().isEmpty())
                continue;

            // keep the href and text of <a> tag.
            String href = aTag.attr("href");
            aTag.clearAttributes();
            aTag.attr("href", href);

            // append the cleaned <a> tag to a new li tag
            Element list = new Element("li");
            list.addClass(CSS.RELEVANT_NEWS_ITEM);
            list.appendChild(aTag);

            // append the new li tag to the ul tag
            relevantNews.appendChild(list);
        }

        if (relevantNews.childNodeSize() == 0){
            return null;
        }

        return relevantNews;
    }

    // create a figure tag with img AND figcaption tagS
    protected static Element sanitizeFigureTag(Element tag){
        // tag = figure tag
        // get the first img tag
        // first figcaption tag
        // append them to a new figure tag
        try {
            // get the first img tag
            Element imgTag = tag.getElementsByTag("img").first();
            imgTag = Scraper.createCleanImgTag(imgTag);

            // get the first figcaption tag
            Element caption = tag.getElementsByTag("figcaption").first().clearAttributes();

            // clear the attribute of <p> tag in side figcaption
            for (Element e: caption.children()){
                e.clearAttributes();
            }

            Element figure = new Element("figure");

            // append them to a new figure tag
            figure.appendChild(imgTag);
            figure.appendChild(caption);
            return figure;
        }catch (NullPointerException e){
            return null;
        }
    }

    protected static Element sanitizeVideoTag(Element tag){
        URL src;
        try {
            src = new URL(tag.attr("src"));
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

        URL newSrc;
        try{
            newSrc = new URL(protocol, host, file);
        } catch (MalformedURLException e){
            return null;
        }

        Element newVideo = new Element("video");
        newVideo.attr("controls", true);

        Element newVideoSrc = new Element("source");
        newVideoSrc.attr("src", newSrc.toString());

        newVideo.appendChild(newVideoSrc);
        return newVideo;
    }

}

final class VNExpressFilter implements NodeFilter {
    final Element newRoot;
    public VNExpressFilter(Element newRoot) {
        this.newRoot = newRoot;
    }

    @Override
    public FilterResult head(Node node, int i) {
        if (!(node instanceof Element)) return FilterResult.SKIP_ENTIRELY;

        Element child = (Element) node;

        // get paragraph (contain text)
        if(child.tagName().equals("p") && child.hasClass(CSS.VNEXPRESS_PARAGRAPH)){

            // clear default CSS class and other attributes
            child = child.clearAttributes();

            // insert css for paragraph
            child.addClass(CSS.PARAGRAPH);

            newRoot.append(child.outerHtml());
//            this.newRoot.appendChild(child); // TODO WHY DOES THIS CAUSE NULL POINTER EXCEPTION???????????
            return FilterResult.SKIP_ENTIRELY;
        }

        // get picture
        else if(child.tagName().equals("figure")){
            Element figure = VNExpressSanitizer.sanitizeFigureTag(child);
            if (figure == null) return FilterResult.SKIP_ENTIRELY;

            figure.addClass(CSS.FIGURE);
            newRoot.append(figure.outerHtml());
            return FilterResult.SKIP_ENTIRELY;

        }

        // get video
        else if (child.tagName().equals("video")){
            Element videoTag = VNExpressSanitizer.sanitizeVideoTag(child);
            if (videoTag == null) return FilterResult.SKIP_ENTIRELY;

            videoTag.addClass(CSS.VIDEO);
            newRoot.append(videoTag.outerHtml());
            return FilterResult.SKIP_ENTIRELY;
        }

        // get relevant news
        else if (child.tagName().equals("ul") && child.hasClass("list-news")){
            Element relevantNews = VNExpressSanitizer.sanitizeRelevantNewsTag(child);
            if(relevantNews == null) return FilterResult.SKIP_ENTIRELY;

            relevantNews.addClass(CSS.RELEVANT_NEWS);
            newRoot.append(relevantNews.outerHtml());
            return FilterResult.SKIP_ENTIRELY;
        }
        else{
            return FilterResult.CONTINUE;
        }
    }

    @Override
    public FilterResult tail(Node node, int i) {

        return null;
    }
}