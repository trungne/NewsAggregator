package BusinessLayer.Sanitizer;

import BusinessLayer.Helper.CSS;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.safety.Safelist;
import org.jsoup.select.NodeFilter;
import org.jsoup.select.NodeTraversor;

public class NhanDanSanitizer extends HtmlSanitizer {
    @Override
    protected Element sanitizeNonTitleTag(Element e, String type) {


        switch (type) {
            case CSS.DESCRIPTION:
                Safelist safelist; // modify this safe list according to the type
                String cleanHtml;
                Element newHtmlElement;
                safelist = Safelist.basic();
                safelist.removeTags("span", "p");
                cleanHtml = Jsoup.clean(e.html(), safelist);

                newHtmlElement = new Element("p").html(cleanHtml);

                return newHtmlElement;
            case CSS.MAIN_CONTENT:
                Element newRoot = new Element("div");
                NodeFilter NhanDanFilter = new NhanDanFilter(newRoot);
                NodeTraversor.filter(NhanDanFilter, e);
                return newRoot;
            default:
                return e;
        }
    }

    @Override
    public Element sanitizeDescription(Element e) {
        Safelist safelist; // modify this safe list according to the type
        String cleanHtml;
        Element newHtmlElement;
        safelist = Safelist.basic();
        safelist.removeTags("span", "p");
        cleanHtml = Jsoup.clean(e.html(), safelist);

        newHtmlElement = new Element("p").html(cleanHtml);

        return newHtmlElement;
    }

    @Override
    public Element sanitizeMainContent(Element e) {
        Element newRoot = new Element("div");
        NodeFilter NhanDanFilter = new NhanDanFilter(newRoot);
        NodeTraversor.filter(NhanDanFilter, e);
        return newRoot;
    }
}


final class NhanDanFilter implements NodeFilter{
    Element root;

    public NhanDanFilter(Element root){
        this.root = root;
    }

    @Override
    public FilterResult head(Node node, int i) {
        if (!(node instanceof Element)) return FilterResult.SKIP_ENTIRELY;

        Element child = (Element) node;
        boolean validTag = false;

        // skipped tag
        if (child.hasClass(CSS.NHANDAN_DESCRIPTION)){
            return FilterResult.SKIP_ENTIRELY;
        }


        // get paragraph
        if (child.tagName().equals("p")){
            child.clearAttributes();
            child.addClass(CSS.PARAGRAPH);
            root.append(child.outerHtml());
            validTag = true;
        }
        else if (child.tagName().equals("blockquote")){
            child.clearAttributes();
            child.addClass(CSS.QUOTE);

            for (Element p: child.getElementsByTag("p")){
                p.clearAttributes();
                p.addClass(CSS.PARAGRAPH);
            }

            root.append(child.outerHtml());
            validTag = true;
        }
        else if (child.tagName().equals("figure")){
            Element figure = filterFigureTag(child);

            if (figure != null){
                figure.addClass(CSS.FIGURE);
                root.append(figure.outerHtml());
                validTag = true;
            }
        }
        else if (child.hasClass(CSS.NHANDAN_AUTHOR)){
            String cleanHtml = Jsoup.clean(child.html(), Safelist.simpleText());

            Element para = new Element("p").html(cleanHtml);
            para.addClass(CSS.AUTHOR);

            if (!para.outerHtml().isEmpty()){
                root.append(para.outerHtml());
                validTag = true;
            }

        }
//        else if (child.hasClass(CSS.NHANDAN_RELEVANT_NEWS)){
//            System.out.println(child);
//
//            Element div = new Element("div");
//            div.addClass(CSS.RELEVANT_NEWS);
//
//            Safelist safelist = Safelist.basicWithImages();
//
//            String cleanHtml = Jsoup.clean(child.html(), safelist);
//            div.html(cleanHtml);
//            root.append(div.outerHtml());
//            validTag = true;
//        }

        if (validTag) return FilterResult.SKIP_ENTIRELY;
        else return FilterResult.CONTINUE;


    }

    // create a figure tag with img AND figcaption tagS
    private static Element filterFigureTag(Element tag){
        Safelist safelist = Safelist.basicWithImages();
        safelist.addTags("figcaption");
        Element figure = new Element("figure");
        String cleanHtml = Jsoup.clean(tag.html(), safelist);
        if (cleanHtml.isEmpty()) return null;
        return figure.html(cleanHtml);
    }

    @Override
    public FilterResult tail(Node node, int i) {
        return null;
    }
}