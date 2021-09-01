package Business.Scraper.Sanitizer;

import Business.Scraper.Helper.CSS;
import Business.Scraper.Sanitizer.MainContentFilter;
import Business.Scraper.LinksCrawler.LinksCrawler;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Safelist;
import org.jsoup.select.NodeFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class TuoiTreFilter extends MainContentFilter{
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
        String regex = "(?<=vid=)(.*?)(?=&)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(rawUrl);
        if (matcher.find()) {
            String src = "https://hls.tuoitre.vn/" + matcher.group(1);

            Element source = new Element("source").attr("src", src);
            return new Element("video").attr("controls", true).appendChild(source);
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
