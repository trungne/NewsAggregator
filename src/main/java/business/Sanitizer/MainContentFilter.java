package business.Sanitizer;

import business.Helper.CSS;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.safety.Safelist;
import org.jsoup.select.NodeFilter;

public abstract class MainContentFilter implements NodeFilter {
    protected Element root;

    public MainContentFilter(Element root){
        this.root = root;
    }

    /** Default implementation for identifying a paragraph tag*/
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
    protected abstract boolean isStandaloneImage(Element node);

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
        return getFilteredFigure(node);
    }

    /** Implement how the node should be filtered */
    protected abstract Element getFilteredFigure(Element node);
    protected abstract Element getFilteredVideo(Element node);
    protected abstract Element getFilteredQuote(Element node);
    protected abstract Element getFilteredStandaloneImage(Element node);

    /** Provide conditions where a node should be skipped */
    protected abstract boolean skip(Element node);

    /**
     * SKIP_ENTIRELY if the node is filter and thus has been filtered
     * CONTINUE looking if the node cannot be filtered, which means it is an invalid node.
     * */
    @Override
    public FilterResult head(Node node, int depth) {
        // only consider Element, skip TextNode
        if (!(node instanceof Element)) {
            return FilterResult.SKIP_ENTIRELY;
        }

        Element e = (Element) node;
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
            e = getFilteredStandaloneImage(e);
        }
        else{
            return FilterResult.CONTINUE;
        }

        if (e != null){
            root.append(e.outerHtml());
            return FilterResult.SKIP_ENTIRELY;
        }
        else{
            return FilterResult.CONTINUE;
        }


    }

    @Override
    public FilterResult tail(Node node, int depth) {
        return null;
    }
}
