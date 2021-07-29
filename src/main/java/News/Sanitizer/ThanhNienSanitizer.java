package News.Sanitizer;

import News.CSS;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Safelist;

public class ThanhNienSanitizer extends HtmlSanitizer {
    @Override
    protected Element sanitizeNonTitleTag(Element e, String type) {
        Safelist safelist; // modify this safe list according to the type
        String cleanHtml;
        Element newHtmlElement;

        switch (type) {
            case CSS.DESCRIPTION:
                safelist = Safelist.basic();
                cleanHtml = Jsoup.clean(e.html(), safelist);

                // TODO: parse clean html with doc to decode special chars

                newHtmlElement = new Element("p").html(cleanHtml);

                return newHtmlElement;
            case CSS.MAIN_CONTENT:

                break;
            default:

                break;
        }
        return e;
    }
}
