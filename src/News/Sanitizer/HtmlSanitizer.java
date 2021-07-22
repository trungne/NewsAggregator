package News.Sanitizer;

import News.CSS;
import org.jsoup.nodes.Element;

public abstract class HtmlSanitizer {
//    public static final String TITLE_CSS_CLASS = "title";
//    public static final String DESCRIPTION_CSS_CLASS = "description";
//    public static final String MAIN_CONTENT_CSS_CLASS = "main-content";
//    public static final String LOCATION_CSS_CLASS = "location";
//    public static final String THUMBNAIL_CSS_CLASS = "thumbnail";
    public Element sanitize(Element e, String type){
        Element target = e;

        switch (type) {
            case CSS.TITLE: {
                target.clearAttributes();
                break;
            }

            case CSS.DESCRIPTION: {
                // maybe do something later??
                break;
            }

            default:
                target = sanitizeNonTitleTag(target, type);
        }

        // Add specific css classes to each detail
        target.addClass(type);


        return target;
    }

    protected abstract Element sanitizeNonTitleTag(Element e, String tag);
}
