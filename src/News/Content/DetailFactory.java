package News.Content;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public abstract class DetailFactory {
    public static final String TITLE_CSS_CLASS = "title";
    public static final String DESCRIPTION_CSS_CLASS = "description";
    public static final String MAIN_CONTENT_CSS_CLASS = "main-content";
    public static final String THUMBNAIL_CSS_CLASS = "thumbnail";
    public Element createHtmlTag(Element e, String type){
        Element target = e;
        // Remove attributes
        target.clearAttributes();

        // Add specific css classes to each detail
        target.addClass(type);

        //
        target = createCustomTag(target, type);
        return e;
    }

    protected abstract Element createCustomTag(Element e, String tag);
}
