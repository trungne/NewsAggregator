package Business.Scraper.Sanitizer;

import Business.Scraper.Helper.CSS;
import Business.Scraper.Helper.ScrapingUtils;
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

import static Business.Scraper.Helper.ScrapingUtils.scrapeFirstImgUrl;

public final class ThanhNienFilter extends MainContentFilter {
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
        for (Element caption : node.getElementsByClass("imgcaption")) {
            Element figcaption = new Element("figcaption");

            // first, find the source, append it and remove immediately
            for (Element src : caption.getElementsByClass("source")) {
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
