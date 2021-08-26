package business.Sanitizer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Safelist;
import org.jsoup.select.NodeFilter;
import org.jsoup.select.NodeTraversor;

public interface Sanitizable {
    // TODO: Thai comments this
    default Element sanitizeTitle(Element e) {
        return e.clearAttributes();
    }
    default Element sanitizeDescription(Element e){
        Safelist safelist;
        String cleanHtml;
        safelist = Safelist.simpleText();
        cleanHtml = Jsoup.clean(e.html(), safelist);
        return new Element("p").html(cleanHtml);
    }

    // Sanitize the main content part of an article
    // This function traverses through the tag and get important info, which will be appended in root node
    // To indicate which tag/class/etc. as important, extend the MainContentFilter class and implement all necessary methods
    default Element sanitizeMainContent(Element e){
        Element root = new Element("div");
        NodeFilter filter = getNodeFilter(root);
        NodeTraversor.filter(filter, e);
        return root;
    }

    NodeFilter getNodeFilter(Element root);
}
