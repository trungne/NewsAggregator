package business.Sanitizer;

import business.Helper.CSS;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.safety.Safelist;
import org.jsoup.select.NodeFilter;
import org.jsoup.select.NodeTraversor;

public class TuoiTreSanitizer extends HtmlSanitizer {
    @Override
    public Element sanitizeDescription(Element e) {
        Safelist safelist;
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
        Element newRoot = new Element("div");
        NodeFilter TuoiTreFilter = new TuoiTreFilter(newRoot);
        NodeTraversor.filter(TuoiTreFilter, e);
        return newRoot.addClass(CSS.MAIN_CONTENT);
    }
}

final class TuoiTreFilter implements NodeFilter {
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
// https://vcplayer.mediacdn.vn/1.1/?_site=tuoitre&new_info_domain=true&stgdmn=hls.tuoitre.vn&vid=tuoitre/2021/8/16/canh-tuong-hon-loan-tai-san-bay-o-kabul-1629076101410637687007-9f5f3.mp4&autoplay=false&poster=https://video-thumbs.tuoitre.vn/tuoitre/2021/8/16/canh-tuong-hon-loan-tai-san-bay-o-kabul-1629076101410637687007-9f5f3.jpg&_info=78b439c0fe2e11eb9ee133497e1cea56
// https://vcplayer.mediacdn.vn/1.1/?_site=tuoitre&new_info_domain=true&stgdmn=hls.tuoitre.vn&vid=tuoitre/2021/8/16/tt-hoi-suc-3-1629084913995275780961-35e6d.mp4&autoplay=false&poster=https://video-thumbs.tuoitre.vn/tuoitre/2021/8/16/tt-hoi-suc-3-1629084913995275780961-35e6d.jpg&_info=06ab84e0fe4311ebb7bb795867eb9fc4
// https://vcplayer.mediacdn.vn/1.1/?_site=tuoitre&new_info_domain=true&stgdmn=hls.tuoitre.vn&vid=tuoitre/2021/8/16/tt-hoi-suc-3-1629084913995275780961-35e6d.mp4&autoplay=false&poster=https://video-thumbs.tuoitre.vn/tuoitre/2021/8/16/tt-hoi-suc-3-1629084913995275780961-35e6d.jpg&_info=06ab84e0fe4311ebb7bb795867eb9fc4
// https://vcplayer.mediacdn.vn/1.1/?_site=tuoitre&amp;new_info_domain=true&amp;stgdmn=hls.tuoitre.vn&amp;vid=tuoitre/2021/8/16/tt-hoi-suc-3-1629084913995275780961-35e6d.mp4&amp;autoplay=false&amp;poster=https://video-thumbs.tuoitre.vn/tuoitre/2021/8/16/tt-hoi-suc-3-1629084913995275780961-35e6d.jpg&amp;_info=06ab84e0fe4311ebb7bb795867eb9fc4
// & = &amp; ????
        // get relevant news // && child.hasClass("VCSortableInPreviewMode")
        else if (child.tagName().equals("div") && child.attr("type").equals("RelatedOneNews")) {
            Element relevantNewsTag = filterRelevantNewsTag(child);
            if (relevantNewsTag != null) {
                relevantNewsTag.addClass(CSS.RELEVANT_NEWS);
                root.append(relevantNewsTag.outerHtml());
            }
//            root.append("<div> ======== This is relevant news ======== </div>");
            validTag = true;
        }

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