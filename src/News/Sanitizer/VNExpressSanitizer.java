package News.Sanitizer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Safelist;
import org.jsoup.select.Elements;


public class VNExpressSanitizer extends HtmlSanitizer {
    @Override
    protected Element sanitizeTag(Element e, String type) {
        Safelist safelist; // modify this safe list according to the type
        String cleanHtml;
        Element newHtmlElement;

        switch (type) {
            case DESCRIPTION_CSS_CLASS:
                safelist = Safelist.basic();
                cleanHtml = Jsoup.clean(e.html(), safelist);
                newHtmlElement = new Element("p").html(cleanHtml);

                // deal with span tag (for location)
                Elements spanTags = newHtmlElement.getElementsByTag("span");
                spanTags.tagName("h3");
                for (Element ele : spanTags) {
                    ele.addClass(LOCATION_CSS_CLASS);
                    ele.text(ele.text() + " - ");
                }

                return newHtmlElement;
            case MAIN_CONTENT_CSS_CLASS:
                safelist = Safelist.relaxed();
                safelist.addAttributes("img", "scr", "data-src");
                safelist.removeTags("div");
                cleanHtml = Jsoup.clean(e.html(), safelist);
                newHtmlElement = new Element("div").html(cleanHtml);
                return newHtmlElement;
            default:
                return e;
        }


    }
}
