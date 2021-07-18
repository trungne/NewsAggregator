package News.Content;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Safelist;
import org.jsoup.select.Elements;

public class TuoiTreElementFactory extends DetailFactory{
    @Override
    protected Element sanitizeTag(Element e, String type) {
        Safelist safelist; // modify this safe list according to the type
        String cleanHtml;
        Element newHtmlElement;

        switch (type) {
            case TITLE_CSS_CLASS:
                // no need for safe list cleaning as there aren't many attribute in the title tag
                return e.clearAttributes();
            case DESCRIPTION_CSS_CLASS:
                safelist = Safelist.basic();
                cleanHtml = Jsoup.clean(e.html(), safelist);
                newHtmlElement = new Element("p").html(cleanHtml.replace("TTO - ", ""));

                return newHtmlElement;
            case MAIN_CONTENT_CSS_CLASS:

                break;
            default:

                break;
        }

        return e;
    }
}
