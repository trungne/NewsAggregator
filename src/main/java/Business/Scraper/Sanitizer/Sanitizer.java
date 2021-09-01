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
        return e.clearAttributes();
    }

    public Element sanitizeDescription(Element e){
        Safelist safelist;
        String cleanHtml;
        safelist = Safelist.simpleText();
        cleanHtml = Jsoup.clean(e.html(), safelist);
        return new Element("p").html(cleanHtml);
    }

    public Element sanitizeMainContent(Element e){
        return FILTER.sanitizeMainContent(e);
    }
}
