package business.Sanitizer;

import business.Helper.CSS;
import org.jsoup.nodes.Element;

public abstract class HtmlSanitizer {
    public Element sanitizeTitle(Element e) {
        e.clearAttributes();
        e.addClass(CSS.TITLE);
        return e;
    }

    abstract public Element sanitizeDescription(Element e);

    abstract public Element sanitizeMainContent(Element e);
}
