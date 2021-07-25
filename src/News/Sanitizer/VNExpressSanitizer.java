package News.Sanitizer;

import News.CSS;
import Scraper.Scraper;
import com.sun.javafx.webkit.theme.ScrollBarWidget;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Safelist;
import org.jsoup.select.Elements;

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
                Element cleanTag = new Element("div");

                for (Element child: e.children()){
                    if (child.tagName().equals("p") && child.hasClass("Normal")){
                        Element para = child.clearAttributes();
                        para.addClass(CSS.PARAGRAPH);
                        cleanTag.appendChild(child.clearAttributes());
                    }
                    else if(child.tagName().equals("figure") && child.hasClass("tplCaption")){
                        Element figure = sanitizeFigureTag(child);
                        if (figure == null) continue;
                        figure.addClass(CSS.FIGURE);
                        cleanTag.appendChild(figure);
                    }
                    else if (child.tagName().equals("ul") && child.hasClass("list-news")){
                        Element relevantNews = sanitizeRelevantNewsTag(child);
                        if (relevantNews == null) continue;
                        relevantNews.addClass(CSS.RELEVANT_NEWS);
                        cleanTag.appendChild(relevantNews);
                    }
                    else if (child.tagName().equals("div")){
                        Element videoTag = child.getElementsByClass("videoContainter").first();
                        if (videoTag == null) continue;

                        videoTag = sanitizeVideoTag(videoTag);
                        if (videoTag == null) continue;

                        videoTag.addClass(CSS.VIDEO);
                        cleanTag.appendChild(videoTag);
                    }

                }

                return cleanTag;

            default:
                return e;
        }


    }



    private Element sanitizeRelevantNewsTag(Element tag){
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

    private Element sanitizeFigureTag(Element tag){
        // get the first img tag
        // first figcaption tag
        // append them to a new figure tag
        try {
            // get the first img tag
            Element imgTag = tag.getElementsByTag("img").first();
            imgTag = Scraper.createCleanImgTag(imgTag);

            // get the first figcaption tag
            Element caption = tag.getElementsByTag("figcaption").first().clearAttributes();

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

    private Element sanitizeVideoTag(Element tag){
        Element oldVideoTag = tag.getElementsByTag("video").first();
        if (oldVideoTag == null) return null;

        URL src;
        try {
            src = new URL(oldVideoTag.attr("src"));
        } catch (MalformedURLException e) {
            return null;
        }

        String protocol = src.getProtocol();
        String host = src.getHost();
        String file = src.getFile();

        host = host.replaceFirst(Pattern.quote("d1"), Matcher.quoteReplacement("v"));

        file = file.replaceFirst(Pattern.quote("/video"), Matcher.quoteReplacement(""));

        file = file.replaceFirst(Pattern.quote("/,240p,360p,480p,,"), Matcher.quoteReplacement(""));
        file = file.replaceFirst(Pattern.quote("/,240p,360p,480p,720p,"), Matcher.quoteReplacement(""));
        file = file.replaceFirst(Pattern.quote("/,240p,360p,,"), Matcher.quoteReplacement(""));
        file = file.replaceFirst(Pattern.quote("/,240p,,"), Matcher.quoteReplacement(""));

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
