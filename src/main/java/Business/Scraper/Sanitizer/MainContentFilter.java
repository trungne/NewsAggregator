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

    private Element root;

    /** Sanitize the main content of an article using Jsoup NodeTraversor and NodeFilter.
     *  This function traverses through the tag and get important info, which will be appended in root node.
     *  To indicate which tag/class/etc. as important, the NodeFilter class extend the MainContentFilter class and implement all necessary methods.
     * @param mainContent main content element
     * @return cleaned main content element
     */
    public Element sanitizeMainContent(Element mainContent){
        if (mainContent == null){
            return null;
        }

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

    /** Check if the node is h1, h2, h3, etc.
     * @param node article element
     * @return true if node contains h1 - h6 tag
     * */    protected boolean isHeader(Element node){
        return node.tagName().matches("h\\d");
    }

    /** Implement how to identify these tags */
    /* Create abstract methods which will be changed later inside each news' filter  */
    /* Theses method meant to be used to check for particular tag. */
    protected abstract boolean isFigure(Element node); // contain both img and caption
    protected abstract boolean isVideo(Element node);
    protected abstract boolean isQuote(Element node);
    protected boolean isAuthor(Element node){
        return node.hasClass("author");
    }
    protected boolean isStandaloneImage(Element node){
        return node.tagName().equals("img");
    }

    /** By default, a paragraph is filtered by
     * 1. clear all its attributes
     * 2. clean the tag with basic safelist */
    protected Element getFilteredParagraph(Element node){
        for (Element a : node.getElementsByTag("a")){
            a.unwrap();
        }
        node.clearAttributes();
        node.html(Jsoup.clean(node.html(), Safelist.basic()));
        node.tagName("p");
        return node;
    }

    /** By default, a header is filtered by
     * 1. clearing all its attributes
     * 2. clean the tag with basic safelist */
    protected Element getFilteredHeader(Element node){
        return node.clearAttributes();
    }

    /** Implement how the node should be filtered */
    /* Create abstract methods which will be changed later inside each news' filter  */
    /* These methods meant to be used to get the filtered version of particular tag */
    protected abstract Element getFilteredFigure(Element node);
    protected abstract Element getFilteredVideo(Element node);
    protected abstract Element getFilteredQuote(Element node);
    protected Element getFilteredStandaloneImage(Element node){
        return ScrapingUtils.createCleanImgTag(node);
    }
    protected Element getFilteredAuthor(Element node){
        return node;
    }

    /** Provide conditions where a node should be skipped */
    protected abstract boolean skip(Element node);

    /** Filter main content element, overriding original method in Jsoup NodeFilter interface.
     * Traverse through the node, filter it if it matches the requirements.
     * E.g. only use the filter method for image if it is image element, the other elements will uses other filter methods.
     * Append filtered elements into the root element.
     * SKIP_ENTIRELY if the node has been filtered.
     * CONTINUE looking if the node cannot be filtered, which means it is an invalid node.
     * @param node main content element
     */
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
