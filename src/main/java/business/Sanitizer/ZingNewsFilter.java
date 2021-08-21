package business.Sanitizer;

import business.Helper.CSS;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.safety.Safelist;
import org.jsoup.select.NodeFilter;

public final class ZingNewsFilter implements NodeFilter {
    Element root;

    public ZingNewsFilter(Element root) {
        this.root = root;
    }

    @Override
    public FilterResult head(Node node, int i) {
        if (!(node instanceof Element)) return FilterResult.SKIP_ENTIRELY;

        Element child = (Element) node;
        boolean validTag = false;

        // get paragraph (some article have <div class="notebox"> )
        if (child.tagName().equals("p")) {
            // Remove "Bài liên quan" Section then get other paragraphs
            if (!child.hasClass("summary") && !child.hasClass("cover") && !child.hasClass("article-meta")) {
                Element paragraphTag = filterParagraphTag(child);
                if (paragraphTag != null) {
                    child.addClass(CSS.PARAGRAPH);
                    root.append(paragraphTag.outerHtml());
                }
                validTag = true;
            } else validTag = false;
        }
        // get quote
        else if (child.tagName().equals("blockquote")) {
            child.clearAttributes();
            child.addClass(CSS.QUOTE);
            for (Element p : child.getElementsByTag("p")) {
                p.clearAttributes();
                p.addClass(CSS.PARAGRAPH);
            }
            root.append(child.outerHtml());
            validTag = true;
        }

        // get figure
//        else if (child.tagName().equals("table") && child.hasClass("CSS.ZING_FIGURE")) {
        else if (child.tagName().equals("table") && child.hasClass("picture")) {
            Element figure = filterFigureTag(child);
            if (figure != null) {
                figure.addClass(CSS.FIGURE);
                root.append(figure.outerHtml());
            }
            validTag = true;
        }
        // get relevant news (haven't include "Bài liên quan" Section)
//        else if (child.tagName().equals("div") && child.hasClass("CSS.ZING_RELEVANT_NEWS") ) {
//        else if (child.tagName().equals("div") && child.hasClass("section-content")) {
//            child.clearAttributes();
//            Element relevantNews = filterRelevantNewsTag(child);
//            if (relevantNews != null) {
//                relevantNews.addClass(CSS.RELEVANT_NEWS);
//                root.append(relevantNews.outerHtml());
//            }
//
//            validTag = true;
//        }
        // get video
        else if (child.tagName().equals("figure")) {
//            if (child.hasClass("CSS.ZING_VIDEO")) {
            if (child.hasClass("video")) {
                Element videoTag = filterVideoTag(child);
                if (videoTag != null) {
                    videoTag.addClass(CSS.VIDEO);
                    root.append(videoTag.outerHtml());
                }
                validTag = true;
            }
        }

        if (validTag) return FilterResult.SKIP_ENTIRELY;
        else return FilterResult.CONTINUE;

    }

    private static Element filterParagraphTag(Element tag) {
        Safelist safelist = Safelist.basic();
        Jsoup.clean(tag.html(), safelist);
//        tag.clearAttributes();
        return tag;
    }

    private static Element filterFigureTag(Element tag) {
//        Safelist safelist = Safelist.relaxed();
//        Element figure = new Element("figure");
//        for (Element picture: tag.getElementsByTag("img")){
//            if(picture.hasAttr("data-src")){
//                String src = picture.attr("data-src");
//                picture.clearAttributes();
//                picture.attr("src", src);
//            }
//        }
//
//        Element figCaption = new Element("figcaption");
//        figCaption.html(tag.getElementsByTag("p").outerHtml());
//
//        safelist.removeTags("p");
//
//        String cleanHtml = Jsoup.clean(tag.html(), safelist);
//        if (cleanHtml.isEmpty()) return null;
//        return figure.html(cleanHtml + figCaption);
        String src = tag.getElementsByTag("img").attr("data-src");
        Element figureCaption = new Element("figcaption").html(tag.getElementsByTag("p").html());
        Element figureSource = new Element("img");
        figureSource.attr("src", src);

        Element newWrapTag = new Element("figure");
        newWrapTag.appendChild(figureSource);
        newWrapTag.appendChild(figureCaption);

        return newWrapTag;
    }

    private static Element filterRelevantNewsTag(Element tag) {
        // get articleThumbnail of relevantNews
        for (Element picture : tag.getElementsByTag("img")) {
            if (picture.hasAttr("data-src")) {
                String src = picture.attr("data-src"); // store original "data-src" attribute
                picture.removeAttr("data-src");
                picture.removeAttr("src");
                picture.removeAttr("alt");
                picture.attr("src", src); // add new "src" attribute with src value
            }
        }
        // get articleTitle of relevantNews
        for (Element hyperlink : tag.getElementsByTag("p")) {
            // only access paragraph with class = article-title
            if (hyperlink.hasClass("article-title")) {
                String oldAttributeValue = tag.getElementsByTag("a").attr("href"); // store original attribute value (error link)
                String newAttributeValue = "https://zingnews.vn" + oldAttributeValue; // fix original attribute value --> ((correct link)) stored as new string
                hyperlink.getElementsByTag("a").removeAttr("href"); // remove original "href" attribute
                hyperlink.getElementsByTag("a").attr("href", newAttributeValue); // add new "href" attribute with new value

            }
        }
        tag.getElementsByTag("img").addClass("relevant-news-thumbnail");
//        tag.getElementsByClass("article-title").addClass("relevant-news-title");
//        tag.getElementsByClass("article-summary").addClass("relevant-news-description");

        tag.getElementsByClass("article-title").removeClass("article-title"); // remove redundant class
        tag.getElementsByClass("article-summary").removeClass("article-summary"); // remove redundant class

        String newRelevantNewsHtml = ""; // Empty String to store all html of RelevantNews
        newRelevantNewsHtml += "<h3> Tin Liên Quan </h3>"; // Edit a little
//        TempHtml += "<h3> Dịch Covid-19 </h3>"; // Edit a little

        for (Element article : tag.getElementsByTag("article")) {
            Element articleRelevantNews = new Element("li"); // empty <li> tag
            // content of RelevantNews include thumbnail, title and description
            String relevantNewsInfo = article.getElementsByClass("relevant-news-thumbnail").outerHtml()
                    + article.getElementsByClass("relevant-news-title").outerHtml()
                    + article.getElementsByClass("relevant-news-description").outerHtml();
            articleRelevantNews.html(relevantNewsInfo);
            newRelevantNewsHtml += articleRelevantNews; // all contents are added here.
        }

        String cleanHtml = newRelevantNewsHtml;
//        String cleanHtml = tag.outerHtml();
        Element relevantNews = new Element("ul");

        if (!cleanHtml.isEmpty()) {
            return relevantNews.html(cleanHtml);
        } else return null;
    }

    private static Element filterVideoTag(Element tag) {
        // get the URL of video source
        String src = "";
        src += tag.getElementsByClass("video").attr("data-video-src");

        // get Html for Video Caption
        String cap = "";
        cap += "<strong><a href= '"
                + "https://zingnews.vn" + tag.getElementsByTag("a").attr("href")
                + "'>"
                + tag.getElementsByTag("a").text() + " </a></strong>"
                + tag.getElementsByTag("figcaption").text();

        Element newVideo = new Element("video"); // new <video>/</video> tag for video source
        newVideo.attr("controls", "true"); // add attribute
        Element newVideoSrc = new Element("source"); // new <source> tag
        newVideoSrc.attr("src", src); // add attribute with value is url of video
        newVideo.appendChild(newVideoSrc); // append <source> in to <video>

        Element newVideoCaption = new Element("p"); // new <p></p> tag for video caption
//        newVideoCaption.attr("class","content-video-caption");
        newVideoCaption.html(cap); // add 'cap' to <div>

        Element newWrapTag = new Element(("div")); // new <div></div> to contain video source <video> and video caption <p>
        newWrapTag.appendChild(newVideo);
        newWrapTag.appendChild(newVideoCaption);

        return newWrapTag;
//        return tag;

    }

    @Override
    public FilterResult tail(Node node, int i) {
        return null;
    }


}
