package News.Sanitizer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Safelist;
import org.jsoup.select.Elements;


public class VNExpressSanitizer extends HtmlSanitizer {
    @Override
    protected Element sanitizeNonTitleTag(Element e, String type) {
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
                spanTags.tagName("strong");
                for (Element ele : spanTags) {
                    ele.addClass(LOCATION_CSS_CLASS);
                    ele.text(ele.text() + " - ");
                }

                return newHtmlElement;
            case MAIN_CONTENT_CSS_CLASS:
                safelist = Safelist.relaxed();

                // modify safelist based on obversation of VNExpress article
                safelist.addAttributes("img", "data-src"); // src sometimes is stored in data-src
                safelist.addAttributes("class", "parser_title"); // parser_title is the caption for video
                safelist.addTags("figure", "figcaption");

                safelist.removeTags("div");

                cleanHtml = Jsoup.clean(e.html(), safelist);

                newHtmlElement = new Element("div").html(cleanHtml);

                for (Element picture: newHtmlElement.getElementsByTag("img")){
                    if(picture.hasAttr("data-src")){
                        picture.attr("src", picture.attr("data-src"));
                        picture.removeAttr("data-src");
                    }
                }

                return newHtmlElement;
            default:
                return e;
        }


    }
}
