package business.Sanitizer;

import business.Helper.CSS;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.safety.Safelist;
import org.jsoup.select.NodeFilter;

public final class NhanDanFilter implements NodeFilter {
    Element root;

    public NhanDanFilter(Element root) {
        this.root = root;
    }

    @Override
    public FilterResult head(Node node, int i) {
        if (!(node instanceof Element)) return FilterResult.SKIP_ENTIRELY;

        Element child = (Element) node;
        boolean validTag = false;

        // skipped tag
        if (child.hasClass(CSS.NHANDAN_DESCRIPTION)) {
            return FilterResult.SKIP_ENTIRELY;
        }


        // get paragraph
        if (child.tagName().equals("p")) {
            child.clearAttributes();
            child.addClass(CSS.PARAGRAPH);
            root.append(child.outerHtml());
            validTag = true;
        } else if (child.tagName().equals("blockquote")) {
            child.clearAttributes();
            child.addClass(CSS.QUOTE);

            for (Element p : child.getElementsByTag("p")) {
                p.clearAttributes();
                p.addClass(CSS.PARAGRAPH);
            }

            root.append(child.outerHtml());
            validTag = true;
        } else if (child.tagName().equals("figure")) {
            Element figure = filterFigureTag(child);

            if (figure != null) {
                figure.addClass(CSS.FIGURE);
                root.append(figure.outerHtml());
                validTag = true;
            }
        } else if (child.hasClass(CSS.NHANDAN_AUTHOR)) {
            String cleanHtml = Jsoup.clean(child.html(), Safelist.simpleText());

            Element para = new Element("p").html(cleanHtml);
            para.addClass(CSS.AUTHOR);

            String content = para.outerHtml();
            if (!StringUtils.isEmpty(content)) {
                root.append(content);
                validTag = true;
            }

        }

        if (validTag)
            return FilterResult.SKIP_ENTIRELY;
        else
            return FilterResult.CONTINUE;


    }

    // create a figure tag with img AND figcaption tagS
    private static Element filterFigureTag(Element tag) {
        Safelist safelist = Safelist.basicWithImages();
        safelist.addTags("figcaption");
        Element figure = new Element("figure");
        String cleanHtml = Jsoup.clean(tag.html(), safelist);

        if (StringUtils.isEmpty(cleanHtml))
            return null;
        return figure.html(cleanHtml);
    }

    @Override
    public FilterResult tail(Node node, int i) {
        return null;
    }
}
