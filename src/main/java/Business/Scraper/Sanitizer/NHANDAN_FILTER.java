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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Safelist;


public final class NHANDAN_FILTER extends MainContentFilter{
    /** Identify figure in main content
     * @param node main content element
     * @return true if node contains figure
     */
    @Override
    protected boolean isFigure(Element node) {
        return node.tagName().equals("figure");
    }

    /** Identify video in main content
     * @param node main content element
     * @return false since NhanDan News doesn't have video inside main content
     */
    @Override
    protected boolean isVideo(Element node) {
        return false;
    } // NhanDan doesn't have video in their articles

    /** Identify quote tag in main content
     * @param node main content element
     * @return true if node contains quote
     */
    @Override
    protected boolean isQuote(Element node) {
        return node.hasClass("blockquote");
    }

    /** Identify author name in main content
     * @param node main content element
     * @return true if node contains author name
     */
    @Override
    protected boolean isAuthor(Element node) {
        return node.hasClass("box-author");
    }

    /** Clean figure tag using Jsoup Safelist
     * @param node uncleaned figure element
     * @return cleaned figure element
     */
    @Override
    protected Element getFilteredFigure(Element node) {
        Safelist safelist = Safelist.basicWithImages();
        safelist.addTags("figcaption");
        node.html(Jsoup.clean(node.html(), safelist));
        return node;
    }

    /** Clean video tag
     * @param node uncleaned video element
     * @return null since NhanDan News doesn't have video
     */
    @Override
    protected Element getFilteredVideo(Element node) {
        return null;
    }

    /** Clean quote tag using Jsoup safelist.
     * @param node uncleaned quote element
     * @return cleaned quote element
     */
    @Override
    protected Element getFilteredQuote(Element node) {
        return getFilteredParagraph(node);
    }

    /** Clean author tag using Jsoup safelist
     * @param node uncleaned author element
     * @return cleaned author element
     */
    @Override
    protected Element getFilteredAuthor(Element node) {
        return new Element("p")
                .html(Jsoup.clean(node.html(), Safelist.simpleText()));
    }

    /** Identify redundant section in main content
     * @param node main content element
     * @return true if node contains article description
     */
    @Override
    protected boolean skip(Element node) {
        return node.hasClass("box-des-detail");
    }


    // "https://www.nhandan-printing.vn/datafiles_D_D/setmulti/nhandan_copy.jpg"

}
