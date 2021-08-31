package Business.Scraper.Sanitizer;

import Business.Scraper.Helper.CSS;
import Business.Scraper.Helper.ScrapingUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.safety.Safelist;
import org.jsoup.select.NodeFilter;

public abstract class MainContentFilter implements NodeFilter {
    // TODO: Thai comments this
    protected Element root;

    protected MainContentFilter(Element root){
        this.root = root;
    }
    /** Default implementation for identifying a paragraph tag. Normally, just check the tag name
     * @param node check if this node is a paragraph
     * @return true if the node is a paragraph
     * */
    protected boolean isParagraph(Element node){
        return node.tagName().equals("p");
    }
    /** Check if the node is h1, h2, h3, etc.*/
    protected boolean isHeader(Element node){
        return node.tagName().matches("h\\d");
    }

    /** Implement how to identify these tags */
    protected abstract boolean isFigure(Element node); // contain both img and caption
    protected abstract boolean isVideo(Element node);
    protected abstract boolean isQuote(Element node);
    protected abstract boolean isAuthor(Element node);
    protected boolean isStandaloneImage(Element node){
        return node.tagName().equals("img");
    }

    /** By default, a paragraph is filtered by
     * 1. clear all its attributes
     * 2. clean the tag with basic safelist */
    protected Element getFilteredParagraph(Element node){
        node.clearAttributes();
        node.html(Jsoup.clean(node.html(), Safelist.basic()));
        return node;
    }

    /** By default, a header is filtered by
     * 1. clearing all its attributes
     * 2. clean the tag with basic safelist */
    protected Element getFilteredHeader(Element node){
        return getFilteredParagraph(node);
    }

    /** Implement how the node should be filtered */
    protected abstract Element getFilteredFigure(Element node);
    protected abstract Element getFilteredVideo(Element node);
    protected abstract Element getFilteredQuote(Element node);
    protected Element getFilteredStandaloneImage(Element node){
        return ScrapingUtils.createCleanImgTag(node);
    }
    protected abstract Element getFilteredAuthor(Element node);

    /** Provide conditions where a node should be skipped */
    protected abstract boolean skip(Element node);

    /**
     * SKIP_ENTIRELY if the node is filter and thus has been filtered
     * CONTINUE looking if the node cannot be filtered, which means it is an invalid node.
     * */
    @Override
    public FilterResult head(Node node, int depth) {
//        if (node instanceof TextNode e){
//            if (StringUtils.isEmpty(e.text())){
//                return FilterResult.CONTINUE;
//            }
//            String text = "TextNode: " + e.text();
//            Element p = new Element("div").text(text);
//            if (!StringUtils.isEmpty(p.text())){
//                root.append(p.addClass(CSS.PARAGRAPH).outerHtml());
//
//            }
//            return FilterResult.CONTINUE;
//        }


        // only consider Element, skip TextNode
        if (!(node instanceof Element e)) {
            return FilterResult.SKIP_ENTIRELY;
        }

        try {
            if (skip(e)){
                return FilterResult.SKIP_ENTIRELY;
            }
            if (isHeader(e)) {
                e = getFilteredHeader(e);
            }
            else if(isParagraph(e)){
                e = getFilteredParagraph(e).addClass(CSS.PARAGRAPH);
            }
            else if (isFigure(e)){
                e = getFilteredFigure(e).addClass(CSS.FIGURE);
            }
            else if (isVideo(e)){
                e = getFilteredVideo(e).addClass(CSS.VIDEO);
            }
            else if (isQuote(e)){
                e = getFilteredQuote(e).addClass(CSS.QUOTE);
            }
            else if (isStandaloneImage(e)){
                e = getFilteredStandaloneImage(e).addClass(CSS.FIGURE);
            }
            else if (isAuthor(e)){
                e = getFilteredAuthor(e).addClass(CSS.AUTHOR);
            }
            else{
                return FilterResult.CONTINUE;
            }

            root.append(e.outerHtml());
            return FilterResult.SKIP_ENTIRELY;
        } catch (NullPointerException er){
            return FilterResult.CONTINUE;
        }
    }

    @Override
    public FilterResult tail(Node node, int depth) {
        return null;
    }
}
