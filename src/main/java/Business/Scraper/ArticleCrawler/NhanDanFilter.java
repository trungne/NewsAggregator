package Business.Scraper.ArticleCrawler;

import Business.Scraper.Helper.CSS;
import Business.Scraper.Sanitizer.MainContentFilter;
import Business.Scraper.LinksCrawler.Category;
import Business.Scraper.LinksCrawler.LinksCrawler;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Safelist;
import org.jsoup.select.Elements;
import org.jsoup.select.NodeFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


public final class NhanDanFilter extends MainContentFilter{
    @Override
    protected boolean isFigure(Element node) {
        return node.tagName().equals("figure");
    }

    @Override
    protected boolean isVideo(Element node) {
        return false;
    } // NhanDan doesn't have video in their articles

    @Override
    protected boolean isQuote(Element node) {
        return node.hasClass("blockquote");
    }

    @Override
    protected boolean isAuthor(Element node) {
        return node.hasClass("box-author");
    }

    @Override
    protected Element getFilteredFigure(Element node) {
        Safelist safelist = Safelist.basicWithImages();
        safelist.addTags("figcaption");
        node.html(Jsoup.clean(node.html(), safelist));
        return node;
    }

    @Override
    protected Element getFilteredVideo(Element node) {
        return null;
    }

    @Override
    protected Element getFilteredQuote(Element node) {
        return getFilteredParagraph(node);
    }

    @Override
    protected Element getFilteredAuthor(Element node) {
        return new Element("p")
                .html(Jsoup.clean(node.html(), Safelist.simpleText()));
    }

    @Override
    protected boolean skip(Element node) {
        return node.hasClass("box-des-detail");
    }


    // "https://www.nhandan-printing.vn/datafiles_D_D/setmulti/nhandan_copy.jpg"

}
