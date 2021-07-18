package News.Content;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public abstract class DetailFactory {
    public static final String TITLE_CSS_CLASS = "title";
    public static final String DESCRIPTION_CSS_CLASS = "description";
    public static final String MAIN_CONTENT_CSS_CLASS = "main-content";
    public static final String LOCATION_CSS_CLASS = "location";
    public Element createHtmlTag(Element e, String type){
        Element target = e;

        // sanitize tag. This also removes all attributes.
        target = sanitizeTag(target, type);

        // Add specific css classes to each detail
        target.addClass(type);


        return target;
    }

    protected abstract Element sanitizeTag(Element e, String tag);
}
