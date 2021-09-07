package Business.Scraper.Sanitizer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Safelist;

public class Sanitizer {
    private final MainContentFilter FILTER;

    public Sanitizer(MainContentFilter mainContentFilter){
        this.FILTER = mainContentFilter;
    }

    /** Sanitize the title of an article using Jsoup Node
     * @param e title element
     * @return title element with all attributes are cleared
     */
    public Element sanitizeTitle(Element e) {
        if (e == null) return null;
        return e.clearAttributes();
    }

    /** Sanitize the description of an article using Jsoup Safelist
     * @param e description element
     * @return cleaned description element
     */
    public Element sanitizeDescription(Element e){
        if (e == null) return null;
        Safelist safelist;
        String cleanHtml;
        safelist = Safelist.simpleText();
        cleanHtml = Jsoup.clean(e.html(), safelist);
        return new Element("p").html(cleanHtml);
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
