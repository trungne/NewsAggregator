package News.Sanitizer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Safelist;

public class ZingNewsSanitizer extends HtmlSanitizer {
    @Override
    protected Element sanitizeTag(Element e, String type) {
        Safelist safelist; // modify this safe list according to the type
        String cleanHtml;
        Element newHtmlElement;

        switch (type) {
            case DESCRIPTION_CSS_CLASS:
                safelist = Safelist.basic();
                cleanHtml = Jsoup.clean(e.html(), safelist);
                newHtmlElement = new Element("p").html(cleanHtml.replace("&nbsp;"," "));

                return newHtmlElement;
            case MAIN_CONTENT_CSS_CLASS:

                break;
            default:

                break;
        }
        return e;
    }
}
