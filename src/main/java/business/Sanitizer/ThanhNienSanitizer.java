package business.Sanitizer;

import business.Helper.CSS;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.safety.Safelist;
import org.jsoup.select.NodeFilter;
import org.jsoup.select.NodeTraversor;

public class ThanhNienSanitizer extends HtmlSanitizer {
    @Override
    public Element sanitizeDescription(Element e) {
        Safelist safelist; // modify this safe list according to the type
        String cleanHtml;
        Element newHtmlElement;
        safelist = Safelist.basic();
        cleanHtml = Jsoup.clean(e.html(), safelist);
        newHtmlElement = new Element("p").html(cleanHtml);
        newHtmlElement.addClass(CSS.DESCRIPTION);
        return newHtmlElement;
    }

    @Override
    public Element sanitizeMainContent(Element e) {
        Element newRoot = new Element("div"); //<div></div>
        NodeFilter ThanhNienFilter = new ThanhNienFilter(newRoot);
        NodeTraversor.filter(ThanhNienFilter, e);
        return newRoot.addClass(CSS.MAIN_CONTENT);
    }
}

final class ThanhNienFilter implements NodeFilter {
    Element root;
    public ThanhNienFilter(Element root){
        this.root = root;
    }

    @Override
    public FilterResult head(Node node, int i) {
        if (!(node instanceof Element)) return FilterResult.SKIP_ENTIRELY;

        Element child = (Element) node;
        boolean validTag = false;

        // get video
        if (child.tagName().equals("table") && child.hasClass("video")) {
            Element videoTag = filterVideoTag(child);
            videoTag.addClass(CSS.VIDEO);
            root.append(videoTag.outerHtml());
            validTag = true;
        }
        // get image
        else if (child.tagName().equals("table") && child.hasClass("imagefull")) {
            Element figureTag = filterFigureTag(child);
            figureTag.addClass(CSS.FIGURE);
            root.append(figureTag.outerHtml());
            validTag = true;
        }

        else if (child.hasClass("details__morenews")){
            child.clearAttributes();
            Element relevantNews = filterRelevantNewsTag(child);
            if (relevantNews != null) {
                relevantNews.addClass(CSS.RELEVANT_NEWS);
                root.append(relevantNews.outerHtml());
            }
            validTag = true;
        }

        // get quote . CSS.ThanhNien_Quote
//        else if (child.hasClass("quote") && child.tagName().equals("div")){
//            Element quoteTag = new Element("div");
//            quoteTag.addClass(CSS.QUOTE);
//            for (Element div : child.getElementsByClass("quote__content")) {
//                Element quoteContent = new Element("div");
//                quoteContent.addClass(CSS.PARAGRAPH);
//                quoteContent.append(div.outerHtml());
//                quoteTag.append(quoteContent.outerHtml());
//            }
//            root.append(quoteTag.outerHtml());
//            child.clearAttributes();
//            child.addClass(CSS.QUOTE);
//            if (child.children().hasClass("quote__content")){
//                child.children().removeAttr("class");
//                child.children().attr("class", CSS.PARAGRAPH);
//            }
//            root.append(child.outerHtml());
//            validTag = true;
//        }

//         get paragraph
//        else if (!child.tagName().equals("table") && !child.hasAttr("class") && !child.hasAttr("data-widget_id") && child.tagName().equals("div")){
        else if (child.tagName().equals("div") && child.classNames().isEmpty()){
//        else {
//            if (child.tagName().equals("div") && !child.tagName().equals("script") && !child.hasAttr("class") && !child.hasAttr("data-widget_id") && !child.hasClass("video")){

            Element paragraphTag = filterParagraphTag(child);
            if (paragraphTag != null){
                paragraphTag.addClass(CSS.PARAGRAPH);
//                root.append(paragraphTag.outerHtml());
            }
            validTag = true;
        }

        if (validTag) return FilterResult.SKIP_ENTIRELY;
        else return FilterResult.CONTINUE;

    }

    private static Element filterParagraphTag(Element tag){
        Element paraContent = new Element("p");
        for (Element para: tag.getElementsByTag("div")) {
            if (para.text().length() > 0){
                paraContent = para ;
            }
        }

        Safelist safelist = Safelist.relaxed();
        safelist.removeTags("p");
//        safelist.removeTags("div");
//        String paraTag = Jsoup.clean(paraContent.html(), safelist);
        String paraTag = Jsoup.clean(tag.outerHtml(), safelist);
        Element newWrapTag = new Element("p").append(paraTag);
//        return newWrapTag;
        return tag;
    }

    private static Element filterFigureTag (Element tag) {
        String src = tag.getElementsByTag("img").attr("data-src");
        Element figureCaption = new Element("figcaption").html(tag.getElementsByTag("p").html());
        Element figureSource = new Element("img");
        figureSource.attr("src", src);

        Element newWrapTag = new Element("figure");
        newWrapTag.appendChild(figureSource);
        newWrapTag.appendChild(figureCaption);

        return newWrapTag;
    }

    private static Element filterVideoTag (Element tag){
        // get video url source and create <source> tag for it
        String url = tag.getElementsByClass("clearfix").attr("data-video-src");
        String videoSrc = "<source src='" + url + "'>";

        // create <video></video> tag to include <source> tag
        Element videoTag = new Element("video");
        videoTag.attr("controls", "true");
        videoTag.append(videoSrc);

        // get video caption and create <p></p> tag for it
        String videoCap = tag.getElementsByTag("p").outerHtml();

        // create new <div></div> to contain video source <video> and video caption <p>
        Element newWrapTag = new Element(("div"));
        newWrapTag.appendChild(videoTag);
        newWrapTag.append(videoCap);

        return newWrapTag;
    }

    private static Element filterRelevantNewsTag (Element tag){

        for (Element hyperlink: tag.getElementsByTag("a")){
            String oldAttributeValue = tag.getElementsByTag("a").attr("href"); // store original attribute value (error link)
            String newAttributeValue = "https://thanhnien.vn" + oldAttributeValue; // fix original attribute value --> ((correct link)) stored as new string
            hyperlink.getElementsByTag("a").removeAttr("href"); // remove original "href" attribute
            hyperlink.getElementsByTag("a").attr("href", newAttributeValue); // add new "href" attribute with new value
        }

        Safelist safelist = Safelist.relaxed();
        safelist.removeTags("span");
        String cleanRelevantNewsTag = Jsoup.clean(tag.html(), safelist);
        Element newWrapTag = new Element("div");
        newWrapTag.append(cleanRelevantNewsTag);

        return newWrapTag;
    }
    @Override
    public FilterResult tail(Node node, int i) {
        return null;
    }
}