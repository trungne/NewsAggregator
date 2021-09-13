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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class VNExpressFilter extends MainContentFilter{
    /** Identify figure in main content
     * @param node main content element
     * @return true if node contains figure
     */
    @Override
    protected boolean isFigure(Element node) {
        return node.tagName().equals("figure") && node.attr("itemprop").contains("image");
    }

    /** Identify video in main content
     * @param node main content element
     * @return true if node contains video
     */
    @Override
    protected boolean isVideo(Element node) {
        return node.tagName().equals("video");
    }

    /** Identify quote in main content
     * @param node main content element
     * @return false since we dont get VNExpress quote
     */
    @Override
    protected boolean isQuote(Element node) {
        return false;
    }

    /** Clean figure tag using Jsoup safelist and Jsoup Node
     * @param node uncleaned figure element
     * @return cleaned figure element
     */
    @Override
    protected Element getFilteredFigure(Element node) {
        node.clearAttributes();
        // get img and caption in figure tag
        // assign data-src attr to src (VNExpress stores their img url in data-src for god knows why)
        // clean the figure tag by safelist
        for(Element img: node.getElementsByTag("img")){
            String src = img.attr("data-src");
            if (!StringUtils.isEmpty(src)){
                img.attr("src", src);
            }
        }
        // unwrap all p tag in figcaption to avoid awkward newline between img and caption
        for (Element caption: node.getElementsByTag("figcaption")){
            for (Element p: caption.children()){
                if (p.tagName().equals("p")){
                    p.unwrap();
                }
            }
        }
        // clean the figure tag
        Safelist safelist = Safelist.basicWithImages();
        safelist.addTags("figcaption", "figure");

        // clean and add custom css class to the figure tag
        node.clearAttributes();
        node.html(Jsoup.clean(node.html(), safelist));
        return node;
    }

    /** Clean video tag using Jsoup Node
     * @param node uncleaned video element
     * @return cleaned video element
     */
    @Override
    protected Element getFilteredVideo(Element node) {
        URL src;
        try {
            src = new URL(node.attr("src"));
        } catch (MalformedURLException e) {
            return null;
        }
        String protocol = src.getProtocol();
        String host = src.getHost();
        String file = src.getFile();

        host = host.replaceFirst(Pattern.quote("d1"), Matcher.quoteReplacement("v"));

        // remove an extra /video from file path
        file = file.replaceFirst(Pattern.quote("/video"), Matcher.quoteReplacement(""));

        // remove resolution from file path
        file = file.replaceFirst("(?<=mp4/)(.*?)(/)", Matcher.quoteReplacement(""));

        // change extension to mp4
        file = file.replaceFirst(Pattern.quote("/vne/master.m3u8"), Matcher.quoteReplacement(".mp4"));

        try {
            src = new URL(protocol, host, file);
        } catch (MalformedURLException e) {
            return null;
        }
        Element source = new Element("source").attr("src", src.toString());
        return new Element("video").attr("controls", true).appendChild(source);
    }

    /** Clean quote tag
     * @param node uncleaned quote element
     * @return null since we don't get VNExpress quote
     */
    @Override
    protected Element getFilteredQuote(Element node) { return null; }

    /** Identify redundant section in main content
     * @param node main content element
     * @return true if node contains graph
     */
    @Override
    protected boolean skip(Element node) {
        return node.hasClass("box_img_video")
                || (node.attr("style").contains("display: none"));
    }
}

