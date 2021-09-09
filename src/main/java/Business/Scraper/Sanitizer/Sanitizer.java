package Business.Scraper.Sanitizer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Safelist;

public class Sanitizer {
    private final MainContentFilter FILTER;

    public Sanitizer(MainContentFilter mainContentFilter){
        this.FILTER = mainContentFilter;
    }

    /** Sanitize the main content of an article using Jsoup NodeTraversor and NodeFilter.
     * @param e main content element
     * @return cleaned main content element
     */
    public Element sanitizeMainContent(Element e){
        if (e == null) return null;
        return FILTER.sanitizeMainContent(e);
    }
}
