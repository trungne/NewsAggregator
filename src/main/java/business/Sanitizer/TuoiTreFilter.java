package business.Sanitizer;

import business.Helper.CSS;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.safety.Safelist;
import org.jsoup.select.NodeFilter;

public final class TuoiTreFilter implements NodeFilter {
    Element root;

    public TuoiTreFilter(Element root) {
        this.root = root;
    }

    @Override
    public FilterResult head(Node node, int i) {
        if (!(node instanceof Element)) return FilterResult.SKIP_ENTIRELY;

        Element child = (Element) node;
        boolean validTag = false;

        // get paragraph
        if (child.tagName().equals("p") && child.classNames().isEmpty()) {
            Element figureTag = filterParaTag(child);
            if (figureTag != null) {
                figureTag.clearAttributes();
                figureTag.addClass(CSS.PARAGRAPH);
                root.append(figureTag.outerHtml());
            }
            validTag = true;
        } else if (child.tagName().equals("p") && child.hasClass("quote") && !child.hasClass("VCObjectBoxRelatedNewsItemSapo")) {
            child.clearAttributes();
            child.addClass(CSS.QUOTE);

            for (Element p : child.getElementsByTag("p")) {
                p.clearAttributes();
                p.addClass(CSS.PARAGRAPH);
            }
            root.append(child.outerHtml());
            validTag = true;
        } else if (child.hasClass("author")) {
            child.clearAttributes();
            child.addClass(CSS.AUTHOR);
            root.append(child.outerHtml());
        }

//         get image // && child.hasClass("VCSortableInPreviewMode")
        else if (child.tagName().equals("div") && child.attr("type").equals("Photo")) {
            Element figureTag = filterFigureTag(child);
            if (figureTag != null) {
                figureTag.addClass(CSS.FIGURE);
                root.append(figureTag.outerHtml());
            }
            validTag = true;
        }

        // get video //&& child.hasClass("VCSortableInPreviewMode")
        else if (child.tagName().equals("div") && child.attr("type").equals("VideoStream")) {
            Element videoTag = filterVideoTag(child);
            if (videoTag != null) {
                videoTag.addClass(CSS.VIDEO);
                root.append(videoTag.outerHtml());
            }
//            root.append("<div> ======== This is video ======== </div> ");
            validTag = true;
        }

        // get relevant news // && child.hasClass("VCSortableInPreviewMode")
//        else if (child.tagName().equals("div") && child.attr("type").equals("RelatedOneNews")) {
//            Element relevantNewsTag = filterRelevantNewsTag(child);
//            if (relevantNewsTag != null) {
//                relevantNewsTag.addClass(CSS.RELEVANT_NEWS);
//                root.append(relevantNewsTag.outerHtml());
//            }
//            validTag = true;
//        }

        if (validTag) return FilterResult.SKIP_ENTIRELY;
        else return FilterResult.CONTINUE;
    }

    @Override
    public FilterResult tail(Node node, int i) {
        return null;
    }

    private static Element filterParaTag(Element tag) {
        Safelist safelist = Safelist.basic();
        safelist.removeTags("span");
        String cleanParaTag = Jsoup.clean(tag.html(), safelist);

        Element newWrapTag = new Element("p").html(cleanParaTag);
        return newWrapTag;
//        return tag;
    }


    private static Element filterFigureTag(Element tag) {
        String src = tag.getElementsByTag("img").attr("src");
        if (src == null) {
            src = tag.getElementsByTag("img").attr("data-src");
        }
        Element figureCaption = new Element("figcaption").html(tag.getElementsByTag("p").html());

        Element figureSource = new Element("img");
        figureSource.attr("src", src);

        Element newWrapTag = new Element("figure");
        newWrapTag.appendChild(figureSource);
        newWrapTag.appendChild(figureCaption);

        return newWrapTag;
//        return tag;
    }

    // impossible to display the source
    private static Element filterVideoTag(Element tag) {

//        String src = tag.getElementsByTag("div").attr("data-src");
//        String src = "&";
        String src = "=== I can not display the source because of this: &&&& ===";
//        String newSrc = src.replace("&", "");


        Element videoCaption = new Element("p").html(tag.getElementsByTag("p").html());
//        Element  videoSource = new Element("source");
//        videoSource.attr("src", newSrc);

        String videoSource = "<source src='" + src + "'>";

        Element videoTag = new Element("video").html(videoSource);
//        videoTag.appendChild(videoSource);

        Element newWrapTag = new Element("div");
        newWrapTag.appendChild(videoTag);
        newWrapTag.appendChild(videoCaption);

//        return newWrapTag;
        return newWrapTag;

//        return tag;
    }

    private static Element filterRelevantNewsTag(Element tag) {
        String title = tag.getElementsByTag("a").text();
        String imgSrc = tag.getElementsByTag("img").attr("src");
        if (imgSrc == null) {
            imgSrc = tag.getElementsByTag("img").attr("data-src");
        }
        String link = tag.getElementsByTag("a").attr("href");
        String content = tag.getElementsByTag("p").text();

        Element relevantNewsThumbnail = new Element("img");
        relevantNewsThumbnail.attr("src", imgSrc);

        String relevantNewsTitle = "<p> <a href='" + link + "'>" + title + "</a>" + "</p>";

        Element relevantNewsContent = new Element("p");
        relevantNewsContent.append(content);

        Element newWrapTag = new Element("div");
        newWrapTag.append("<h3> Tin Liên Quan </h3>");
        newWrapTag.append(relevantNewsThumbnail.outerHtml());
        newWrapTag.append(relevantNewsTitle);
        newWrapTag.append(relevantNewsContent.outerHtml());
        return newWrapTag;
//        return tag;
    }
}
