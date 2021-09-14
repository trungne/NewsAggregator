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
import org.jsoup.safety.Safelist;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class TUOITRE_FILTER extends MainContentFilter{
    /** Identify figure in main content
     * @param node main content element
     * @return true if node contains figure
     */
    @Override
    protected boolean isFigure(Element node) {
        return node.hasClass("VCSortableInPreviewMode")
                && node.attr("type").equals("Photo");
    }

    /** Identify video in main content
     * @param node main content element
     * @return true if node contains video
     */
    @Override
    protected boolean isVideo(Element node) {
        return node.hasClass("VCSortableInPreviewMode")
                && node.attr("type").equals("VideoStream");
    }

    /** Identify quote tag in main content
     * @param node main content element
     * @return true if node contains quote
     */
    @Override
    protected boolean isQuote(Element node) {
        if (node.hasClass("VCSortableInPreviewMode")){
            String type = node.attr("type");
            return type.equals("wrapnote")
                    || type.equals("SimpleQuote");
        }
        else {
            return false;
        }
    }

    /** Clean figure tag using Jsoup Node
     * @param node uncleaned figure element
     * @return cleaned figure element
     */
    @Override
    protected Element getFilteredFigure(Element node) {
        Element figure = new Element("figure");

        // pull out img tag
        for (Element imgTag: node.getElementsByTag("img")){
            String src = imgTag.attr("src");

            if (StringUtils.isEmpty(src)){
                continue;
            }

            String alt = imgTag.attr("alt");
            Element img = new Element("img")
                    .attr("src", src)
                    .attr("alt", alt);
            figure.append(img.outerHtml());
        }

        // pull out caption
        for (Element caption: node.getElementsByTag("p")){
            Element figcaption = new Element("figcaption")
                    .text(caption.text());
            figure.append(figcaption.outerHtml());
        }
        return figure;
    }

    /** Clean video tag using Jsoup Node
     * @param node uncleaned video element
     * @return cleaned video element
     */
    @Override
    protected Element getFilteredVideo(Element node) {
        String rawUrl = node.attr("data-src");

        // extract the link from rawUrl by regex
        String regex = "(?<=vid=)(.*?)(?=&)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(rawUrl);
        if (matcher.find()) {
            String src = "https://hls.tuoitre.vn/" + matcher.group(1);

            Element source = new Element("source").attr("src", src);
            return new Element("video").attr("controls", true).appendChild(source);
        }
        else{
            return null;
        }
    }

    /** Clean quote tag using Jsoup Safelist and Jsoup Node
     * @param node uncleaned quote element
     * @return cleaned quote element
     */
    @Override
    protected Element getFilteredQuote(Element node) {
        node.clearAttributes();
        Safelist safelist = Safelist.basic();
        safelist.removeTags("a"); // no need to keep link in quote

        node.html(Jsoup.clean(node.html(), safelist));
        // add css class to the paragraphs in quote
        for (Element p: node.getElementsByTag("p")) {
            p.clearAttributes();
            // style paragraph containing author's name
            if (p.hasClass("StarNameCaption")){
                p.tagName("span");
                p.attr("style", "text-align:right;");
            }
        }
        return node;
    }


    /** Identify redundant section in main content
     * @param node main content element
     * @return true if node contains article relevant news
     */
    @Override
    protected boolean skip(Element node) {
        return (node.attr("type").equals("RelatedOneNews"));
    }


}
