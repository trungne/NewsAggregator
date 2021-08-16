package business.Sanitizer;

import business.Helper.CSS;
import org.jsoup.nodes.Element;

public abstract class HtmlSanitizer {
    public Element sanitizeTitle(Element e) {
        e.clearAttributes();
        e.addClass(CSS.TITLE);
        return e;
    }

    // TODO: make this abstract later!
    public Element sanitizeDescription(Element e) {
        return e;
    }


    // TODO: make this abstract later!
    public Element sanitizeMainContent(Element e) {
        return e;
    }

    // TODO: make this abstract later!
    public Element sanitizeThumbNail(Element e) {
        return e.addClass(CSS.THUMBNAIL);
    }

    public Element sanitize(Element e, String type) {
        Element target = e;

        switch (type) {
            case CSS.TITLE: {
                target.clearAttributes();
                break;
            }
            case CSS.THUMBNAIL: {

            }

            default:
                target = sanitizeNonTitleTag(target, type);
        }

        // Add specific css classes to each detail
        if (target != null)
            target.addClass(type);


        return target;
    }

    protected abstract Element sanitizeNonTitleTag(Element e, String tag);

}
