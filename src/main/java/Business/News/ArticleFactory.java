/*
  RMIT University Vietnam
  Course: INTE2512 Object-Oriented Programming
  Semester: 2021B
  Assessment: Final Project
  Created  date: 10/08/2021
  Author: Nguyen Quoc Hoang Trung, S3818328
  Last modified date: 18/09/2021
  Author: Nguyen Quoc Hoang Trung, S3818328
  Acknowledgement: Factory pattern is used to encapsulate the creation of article object
   https://www.tutorialspoint.com/design_pattern/factory_pattern.htm
*/

package Business.News;

import org.jsoup.nodes.Element;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;
import java.util.Set;

public class ArticleFactory {
    private static final String PUBLISHED_TIME = "published-time";
    private static final String ARTICLE_HEADER = "article-header";
    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";
    private static final String ARTICLE_CATEGORY = "article-category";
    private static final String ARTICLE_SOURCE = "source";
    private static final String MAIN_CONTENT = "main-content";

    private static final String CSS_STYLE = loadCssStyle();
    private static String loadCssStyle(){
        Path path = Paths.get("src", "main", "resources", "Styles", "article-style.css");
        File file = new File(path.toAbsolutePath().toString());
        Scanner reader;
        try {
            reader = new Scanner(file);
        } catch (IOException e){
            return "";
        }
        StringBuilder style = new StringBuilder();
        while (reader.hasNextLine()){
            style.append(reader.nextLine());
        }
        reader.close();
        return style.toString();
    }

    static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("EE, dd/MMMM/yyyy, kk:mm ");

    public static Article createArticle(String newsSource,
                                        String url,
                                        String title,
                                        String description,
                                        Element mainContent,
                                        String thumbnail,
                                        Set<String> categories,
                                        LocalDateTime time){
        // skip all articles that are more than one week old
        if (isMoreThanAWeek(time)){
            return null;
        }
        Element html = new Element("html").attr("lang", "vi");
        html.appendChild(getHeadTag());
        Element source = getSourceTag(url, newsSource);
        Element header = getHeader(categories, time);
        html.appendChild(getBody(header, title, description, mainContent, source));
        String rawHtml = "<!DOCTYPE html>\n" + html.outerHtml();
        return new Article(newsSource, title, description, thumbnail, time, rawHtml);
    }
    private static boolean isMoreThanAWeek(LocalDateTime time){
        return ChronoUnit.MINUTES.between(time, LocalDateTime.now()) >= 24 * 60 * 7; // a day in minutes
    }
    private static Element getHeadTag() {
        final Element head = new Element("head").append("<meta charset=\"UTF-8\">" +
                "<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
        return head.appendChild(new Element("style").text(CSS_STYLE));
    }
    /** Get a header tag that contains category and published time
     * */
    private static Element getHeader(Set<String> categories, LocalDateTime time){
        Element header = new Element("div");

        // create category div
        Element categoryTag = new Element("div")
                .addClass(ARTICLE_CATEGORY);

        for (String cate : categories) {
            categoryTag.appendChild(new Element("div")
                    .addClass(cate)
                    .text(cate));
        }

        // create published time div
        Element publishedTime = new Element("div")
                .addClass(PUBLISHED_TIME)
                .text(getAbsoluteTime(time));

        header.appendChild(categoryTag);
        header.appendChild(publishedTime);
        return header;
    }

    private static String getAbsoluteTime(LocalDateTime time) {
        return dtf.format(time);
    }

    private static Element getBody(Element header,
                                   String title,
                                   String description,
                                   Element mainContent,
                                   Element source){

        Element body = new Element("body");

        body.appendChild(header.addClass(ARTICLE_HEADER));
        body.appendChild(new Element("h1").text(title).addClass(TITLE));
        body.appendChild(new Element("div").text(description).addClass(DESCRIPTION));
        body.appendChild(mainContent.addClass(MAIN_CONTENT));
        body.appendChild(source.addClass(ARTICLE_SOURCE));

        return body;

    }

    private static Element getSourceTag(String url, String newsSource){
        Element source = new Element("p").text("Source: ");
        Element link = new Element("a")
                .attr("href", url)
                .text(newsSource);
        return source.appendChild(link);

    }

}
