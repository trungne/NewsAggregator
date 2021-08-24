package business.Scraper;

import business.Helper.LocalDateTimeParser;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public interface Scrapable {
    default Element scrapeFirstElementByClass(Document doc, String cls){
        String query = cls;
        if (!query.startsWith(".")) {
            query = "." + query;
        }
        Element temp = doc.selectFirst(query);
        if (temp != null){
            return new Element("div").html(temp.outerHtml());
        }
        else{
            return null;
        }

    }

    default Element scrapeAuthor(Document doc, String cls){
        Element author = scrapeFirstElementByClass(doc, cls);
        if (author != null){
            return new Element("p")
                    .attr("style", "text-align:right;")
                    .appendChild(new Element("strong").text(author.text()));
        }
        else{
            return null;
        }
    }

    default String scrapeFirstImgUrl(Document doc, String cls){
        Element firstElementOfClass = scrapeFirstElementByClass(doc, cls);
        if (firstElementOfClass != null){
            // this loop only runs once
            for (Element imgTag: firstElementOfClass.getElementsByTag("img")){
                // some news outlets store url of img in data-src
                String url = imgTag.attr("data-src");

                // if the img tag doesn't have data-src attr, check its src attr
                if(StringUtils.isEmpty(url)){
                    url = imgTag.attr("src");
                    if (StringUtils.isEmpty(url)){
                        return "";
                    }
                }
                return url;
            }
        }
        return "";
    }

    default LocalDateTime scrapePublishedTimeFromMeta(Document doc, String key, String value, String attribute){
        Elements dateTimeTag = doc.getElementsByAttributeValue(key, value);
        String dateTimeStr = dateTimeTag.attr(attribute);
        if (StringUtils.isEmpty(dateTimeStr)) {
            return LocalDateTime.now();
        }
        return LocalDateTimeParser.parse(dateTimeStr);
    }

    default Set<String> scrapeCategoryNamesInBreadcrumb(Document doc , String breadcrumbCls){
        Set<String> categoryList = new HashSet<>();
        Element breadcrumb = scrapeFirstElementByClass(doc, breadcrumbCls);
        if (breadcrumb != null){
            // breadcrumb contains links to categories
            Elements linkTags = breadcrumb.getElementsByTag("a");
            for(Element link: linkTags) {
                // name of category is the title of its link
                String category = link.attr("title");

                // convert from Vietnamese to English name
                category = Category.convert(category); // return "" when cannot convert
                categoryList.add(category);
            }
        }
        return categoryList;
    }

    /* Scrape category name in meta tag,
    * returns empty string if no category found
    * */
    default String scrapeCategoryNamesInMeta(Document doc, String key, String value, String attribute){
        Element tagContainsCategoryInfo = doc.getElementsByAttributeValue(key, value).first();
        if(tagContainsCategoryInfo != null){
            String category = tagContainsCategoryInfo.attr(attribute);
            return Category.convert(category);
        }
        return "";
    }

    LocalDateTime scrapePublishedTime(Document doc);
    Set<String> scrapeCategoryNames(Document doc);


}
