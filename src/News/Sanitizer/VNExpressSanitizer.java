package News.Sanitizer;

import News.CSS;
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
            case CSS.DESCRIPTION:
                safelist = Safelist.basic();
                cleanHtml = Jsoup.clean(e.html(), safelist);
                newHtmlElement = new Element("p").html(cleanHtml);

                // deal with span tag (for location)
                Elements spanTags = newHtmlElement.getElementsByTag("span");
                spanTags.tagName("strong");
                for (Element ele : spanTags) {
                    ele.addClass(CSS.LOCATION);
                    ele.text(ele.text() + " - ");
                }

                return newHtmlElement;
            case CSS.MAIN_CONTENT:
                safelist = Safelist.relaxed();

                // modify safelist based on obversation of VNExpress article
                safelist.addAttributes("img", "data-src"); // src sometimes is stored in data-src
                safelist.addTags("figure", "figcaption");

                // remove unncessary tags
                safelist.removeTags("div");

                // create a clean html by cleaning target html with the specified safe list
                cleanHtml = Jsoup.clean(e.html(), safelist);

                // create a div that wraps the html
                newHtmlElement = new Element("div").html(cleanHtml);

                // provide url to src attribute in img tag
                // sometimes img urls are stored in data-src (for whatever reason)
                // we need to extract url in data-src and assign it to src attribute in order for img tag to display img
                for (Element picture: newHtmlElement.getElementsByTag("img")){
                    if(picture.hasAttr("data-src")){
                        picture.attr("src", picture.attr("data-src"));

                        // the url is now stored in src, no need to keep data-src
                        picture.removeAttr("data-src");
                    }
                }

                // return the newly sanitize element
                return newHtmlElement;
            default:
                return e;
        }


    }
}
