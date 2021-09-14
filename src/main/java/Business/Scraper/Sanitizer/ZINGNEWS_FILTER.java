/*
  RMIT University Vietnam
  Course: INTE2512 Object-Oriented Programming
  Semester: 2021B
  Assessment: Final Project
  Created  date: dd/mm/yyyy
  Author: Student name, Student ID
  Last modified date: dd/mm/yyyy
  Author: Student name, Student ID
  Acknowledgement: Thanks and give credits to the resources that you used in this file
*/

package Business.Scraper.Sanitizer;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.safety.Safelist;


public final class ZINGNEWS_FILTER extends MainContentFilter{

    /** Identify figure in main content
     * @param node main content element
     * @return true if node contains figure
     */
    @Override
    protected boolean isFigure(Element node) {
        return node.hasClass("picture");
    }

    /** Identify video in main content
     * @param node main content element
     * @return true if node contains video
     */
    @Override
    protected boolean isVideo(Element node) {
        return node.hasClass("video");
    }

    /** Identify quote tag in main content
     * @param node main content element
     * @return true if node contains quote
     */
    @Override
    protected boolean isQuote(Element node) {
        return node.hasClass("notebox")
                || node.tagName().equals("blockquote");
    }

    /** Clean paragraph tag using Jsoup Safelist and Jsoup Node
     * @param node uncleaned paragraph element
     * @return cleaned paragraph element
     */
    @Override
    protected Element getFilteredFigure(Element node) {
        // assign data-src attr to src for img tag
        for(Element img: node.getElementsByTag("img")){
            String src = img.attr("data-src");
            if (!StringUtils.isEmpty(src)){
                img.attr("src", src);
            }
        }

        // change caption to figcaption to follow the convention
        for (Element e: node.getElementsByClass("caption")){
            e.clearAttributes();
            e.tagName("figcaption");
            // remove p tag but keep the text node
            for (Element p: e.getElementsByTag("p")){
                p.unwrap();
            }
        }

        // clean the tag so that only img and figcaption tag remain
        Safelist safelist = Safelist.basicWithImages();
        safelist.addTags("figcaption");

        // create a figure tag to put img and figcaption in
        return new Element("figure")
                .html(Jsoup.clean(node.html(), safelist));
    }

    /** Clean video tag using Jsoup Node
     * @param node uncleaned video element
     * @return cleaned video element
     */
    @Override
    protected Element getFilteredVideo(Element node) {
        Element videoTag = getVideoTag(node);
        if (videoTag != null){
            // get caption of video
            Element caption = new Element("figcaption");
            for (Element figCaptionTag: node.getElementsByTag("figcaption")){
                String clean = Jsoup.clean(figCaptionTag.html(), Safelist.none());
                caption.appendText(clean);
            }

            videoTag.append(caption.outerHtml());
            return videoTag;
        }

        return null;

    }

    /** Clean quote tag using Jsoup Node
     * @param node uncleaned quote element
     * @return cleaned quote element
     */
    @Override
    protected Element getFilteredQuote(Element node) {
        Element quote = new Element("blockquote");
        for (Element p: node.getElementsByTag("p")){
            quote.appendChild(p.clearAttributes());
        }
        return quote;
    }

    /** Identify redundant section in main content
     * @param node main content element
     * @return true if node contains article relevant news or covid news
     */
    @Override
    protected boolean skip(Element node) {
        return node.hasClass("inner-article")
                || node.hasClass("covid-chart-widget")
                || node.hasClass("z-widget-corona")
                || node.hasClass("article-news-background")
                || node.id().equals("innerarticle")
                || node.id().equals("corona-counter");
    }

    /** Clean video element (not include video caption)
     * @param tag uncleaned video element
     * @return cleaned video element
     */
    private static Element getVideoTag(Element tag) {
        // get the URL of video source
        String src = tag.attr("data-video-src");
        if (StringUtils.isEmpty(src)){
            return null;
        }
        Element video = new Element("video");
        video.attr("controls", "true");

        Element source = new Element("source");
        source.attr("src", src); // add attribute with value is url of video
        video.appendChild(source); // append <source> in to <video>

        return video;
    }

    @Override
    public FilterResult tail(Node node, int i) {
        return null;
    }

}

