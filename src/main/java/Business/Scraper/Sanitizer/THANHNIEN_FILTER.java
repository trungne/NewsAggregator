/*
  RMIT University Vietnam
  Course: INTE2512 Object-Oriented Programming
  Semester: 2021B
  Assessment: Final Project
  Created  date: 10/08/2021
  Author: Nguyen Quoc Hoang Trung, S3818328
  Last modified date: 18/09/2021
  Author: Nguyen Quoc Hoang Trung, S3818328
  Acknowledgement: No external knowledge
*/

package Business.Scraper.Sanitizer;

import Business.Scraper.Helper.ScrapingUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;

public final class THANHNIEN_FILTER extends MainContentFilter {

    /** Identify paragraph in main content
     * @param node main content element
     * @return true if node contains only paragraph
     */
    @Override
    protected boolean isParagraph(Element node) {
        return node.classNames().isEmpty()
                && (node.tagName().equals("div") || node.tagName().equals("p"))
                && !StringUtils.isEmpty(node.text());
    }

    /** Identify figure in main content
     * @param node main content element
     * @return true if node contains figure
     */
    @Override
    protected boolean isFigure(Element node) {
        // count thumbnail as a figure tag
        return node.hasClass("imagefull")
                || node.id().equals("contentAvatar");
    }

    /** Identify video in main content
     * @param node main content element
     * @return true if node contains video
     */
    @Override
    protected boolean isVideo(Element node) {
        return node.hasClass("cms-video");
    }

    /** Identify quote tag in main content
     * @param node main content element
     * @return true if node contains quote
     */
    @Override
    protected boolean isQuote(Element node) {
        return node.hasClass("quote");
    }

    @Override
    protected Element getFilteredParagraph(Element node) {
        Element p = new Element("p");

        // handle figure in the paragraph
        for (Element figure: node.getElementsByClass("imagefull")){
            Element e = getFilteredFigure(figure);
            if (e != null){
                p.appendChild(e);
            }
            figure.remove();
        }

        // handle standalone picture in the paragraph
        for (Element img: node.getElementsByTag("img")){
            Element e = ScrapingUtils.createCleanImgTag(img);
            if (e != null){
                p.appendChild(e);
            }
            img.remove();
        }

        // handle video in the paragraph
        for (Element video: node.getElementsByClass("cms-video")){
            Element e = getFilteredVideo(video);
            if (e != null){
                p.appendChild(e);
            }
            video.remove();
        }

        return p.prependText(node.text());
    }

    /** Clean figure tag using Jsoup Node
     * @param node uncleaned figure element
     * @return cleaned figure element
     */
    @Override
    protected Element getFilteredFigure(Element node) {
        Element figure = new Element("figure");
        // this loop always runs once as there is always 1 picture in this node
        for (Element img : node.getElementsByTag("img")) {
            Element imgTag = ScrapingUtils.createCleanImgTag(img);
            if (imgTag != null) {
                figure.appendChild(imgTag);
            }

        }

        // this loop always runs once as there is always 1 caption in this node
        for (Element caption : node.getElementsByClass("imgcaption")) {
            Element figcaption = new Element("figcaption");

            // first, find the source, append it and remove immediately
            for (Element src : caption.getElementsByClass("source")) {
                figcaption.appendChild(new Element("em").text(src.text()).prepend("<br>"));
                src.remove();
            }

            // second, prepend all the remaining text, which is the caption
            figcaption.prependText(caption.text());
            figure.appendChild(figcaption);
        }

        // if no images are found, return null
        if (figure.children().isEmpty()) {
            return null;
        }

        return figure;
    }

    /** Clean video tag using Jsoup Safelist and Jsoup Node
     * @param node uncleaned video element
     * @return cleaned video element
     */
    @Override
    protected Element getFilteredVideo(Element node) {
        String src = node.attr("data-video-src");
        if (StringUtils.isEmpty(src)) {
            return null;
        }

        Element source = new Element("source")
                .attr("src", src);
        return new Element("video")
                .attr("controls", true)
                .appendChild(source);
    }

    /** Clean quote tag
     * @param node uncleaned quote element
     * @return null since we dont get ThanhNien quote
     */
    @Override
    protected Element getFilteredQuote(Element node) {
        return null;
    }

    /** Identify redundant section in main content
     * @param node main content element
     * @return true if node contains article description or relevant news
     */
    @Override
    protected boolean skip(Element node) {
        return node.hasClass("sapo")
                || node.id().equals("chapeau")
                || node.hasClass("details__morenews");
    }

}
