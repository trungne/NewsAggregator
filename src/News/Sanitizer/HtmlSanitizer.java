package News.Sanitizer;

import org.jsoup.nodes.Element;

public abstract class HtmlSanitizer {
    public static final String TITLE_CSS_CLASS = "title";
    public static final String DESCRIPTION_CSS_CLASS = "description";
    public static final String MAIN_CONTENT_CSS_CLASS = "main-content";
    public static final String LOCATION_CSS_CLASS = "location";
    public Element sanitize(Element e, String type){
        Element target = e;

        if (TITLE_CSS_CLASS.equals(type)){
            target.clearAttributes();
        }
        else{
            target = sanitizeTag(target, type);
        }

        // Add specific css classes to each detail
        target.addClass(type);


        return target;
    }

    protected abstract Element sanitizeTag(Element e, String tag);
}
