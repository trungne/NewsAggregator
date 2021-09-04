package Business.Scraper.Sanitizer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Safelist;

public class Sanitizer {
    private final MainContentFilter FILTER;

    public Sanitizer(MainContentFilter mainContentFilter){
        this.FILTER = mainContentFilter;
    }

    public Element sanitizeTitle(Element e) {
        if (e == null) return null;
        return e.clearAttributes();
    }

    public Element sanitizeDescription(Element e){
        if (e == null) return null;
        Safelist safelist;
        String cleanHtml;
        safelist = Safelist.simpleText();
        cleanHtml = Jsoup.clean(e.html(), safelist);
        return new Element("p").html(cleanHtml);
    }

    public Element sanitizeMainContent(Element e){
        if (e == null) return null;
        return FILTER.sanitizeMainContent(e);
    }
}
