package News.Sanitizer;

import News.CSSConvention;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Safelist;

public class NhanDanSanitizer extends HtmlSanitizer {
    @Override
    protected Element sanitizeNonTitleTag(Element e, String type) {
        Safelist safelist; // modify this safe list according to the type
        String cleanHtml;
        Element newHtmlElement;

        switch (type) {
            case CSSConvention.DESCRIPTION:
                safelist = Safelist.basic();
                safelist.removeTags("span", "p");
                cleanHtml = Jsoup.clean(e.html(), safelist);

                newHtmlElement = new Element("p").html(cleanHtml);

                return newHtmlElement;
            case CSSConvention.MAIN_CONTENT:

                break;
            default:

                break;
        }
        return e;
    }
}
