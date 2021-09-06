package Business.Scraper.Sanitizer;

import Business.Scraper.Helper.ScrapingUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.safety.Safelist;
import org.jsoup.select.NodeFilter;
import org.jsoup.select.NodeTraversor;

public abstract class MainContentFilter implements NodeFilter {
    // CSS Classes for content in main content
    public static final String PARAGRAPH = "content-paragraph";
    public static final String FIGURE = "content-pic";
    public static final String QUOTE = "content-quote";
    public static final String VIDEO = "content-video";
    public static final String AUTHOR = "author";

    // TODO: Thai comments this
    private Element root;

    /** Sanitize the main content part of an article
    * This function traverses through the tag and get important info, which will be appended in root node
    * To indicate which tag/class/etc. as important, extend the MainContentFilter class and implement all necessary methods */
    public Element sanitizeMainContent(Element mainContent){
        root = new Element("div");
        NodeTraversor.filter(this, mainContent);
        return root;
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
                e = getFilteredParagraph(e).addClass(PARAGRAPH);
            }
            else if (isFigure(e)){
                e = getFilteredFigure(e).addClass(FIGURE);
            }
            else if (isVideo(e)){
                e = getFilteredVideo(e).addClass(VIDEO);
            }
            else if (isQuote(e)){
                e = getFilteredQuote(e).tagName("blockquote").addClass(QUOTE);
            }
            else if (isStandaloneImage(e)){
                e = getFilteredStandaloneImage(e).addClass(FIGURE);
            }
            else if (isAuthor(e)){
                e = getFilteredAuthor(e).addClass(AUTHOR);
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
