package Business.News;

import org.jsoup.nodes.Element;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Article implements Comparable<Article> {
    private static final String ARTICLE_HEADER = "article-header";
    private static final String ARTICLE_CATEGORY = "article-category";
    private static final String ARTICLE_CONTENT = "article-content";
    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";
    private static final String MAIN_CONTENT = "main-content";
    private static final String PUBLISHED_TIME = "published-time";
    private static final String CSS_STYLE = loadCssStyle();
    private static String loadCssStyle(){
        Path path = Paths.get("src", "main", "resources", "styles", "article-style.css");
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

    // TODO: Khang comments this
    static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("EE, dd/MMMM/yyyy, kk:mm ");

    private final URL url;
    private Element title;
    private Element description;
    private String thumbNail;
    private LocalDateTime dateTime;
    private final String mainCategory;
    private final Set<String> categories = new HashSet<>();
    private final String newsSource;
    private String html;

    public Article(String source, URL url, String mainCategory) {
        this.newsSource = source;
        this.url = url;
        this.mainCategory = mainCategory;
    }

    private Element getBodyTag(Element mainContent) {
        final Element body = new Element("body");
        body.appendChild(getArticleTag(mainContent));
        return body;
    }

    private Element getHeadTag() {
        final Element head = new Element("head");
        Element title = new Element("title");
        Element style = new Element("style");
        style.text(CSS_STYLE);

        title.text(getTitle());
        head.append("<meta charset=\"UTF-8\">");
        head.append("<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">");
        head.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
        head.appendChild(title);
        head.appendChild(style);
        return head;
    }

    private String createHtml(Element mainContent) {
        Element html = new Element("html");
        html.attr("lang", "vi");

        html.appendChild(getHeadTag());
        html.appendChild(getBodyTag(mainContent));

        String docString = "<!DOCTYPE html>\n";
        return docString + html.outerHtml();
    }

    private Element getCategoryTag() {
        // category div
        Element categories = new Element("div");
        categories.addClass(ARTICLE_CATEGORY);
        StringBuilder categoriesStrBuilder = new StringBuilder();
        categoriesStrBuilder.append(mainCategory).append(" - ");
        for (String category : this.categories) {
            categoriesStrBuilder.append(category).append(" - ");
        }

        // remove the " - " at the end;
        String categoriesStr = categoriesStrBuilder.substring(0, categoriesStrBuilder.length() - 2);
        categories.text(categoriesStr);

        return categories;
    }

    private Element getPublishedTimeTag() {
        Element publishedTime = new Element("div");
        publishedTime.addClass(PUBLISHED_TIME);
        publishedTime.text(getAbsoluteTime());
        return publishedTime;

    }

    private Element getArticleTag(Element mainContent) {
        // create header div
        Element header = new Element("div");
        header.addClass(ARTICLE_HEADER);

        Element categories = getCategoryTag();
        Element publishedTime = getPublishedTimeTag();

        header.appendChild(categories);
        header.appendChild(publishedTime);

        // create article content div which contains title, desp, and main content
        Element content = new Element("div");
        content.addClass(ARTICLE_CONTENT);
        content.appendChild(title);
        content.appendChild(description);
        content.appendChild(mainContent);

        Element article = new Element("article");
        article.appendChild(header);
        article.appendChild(content);
        article.appendChild(getSourceTag());

        return article;
    }
    public Element getSourceTag(){
        Element source = new Element("p").text("Source: ");
        Element tag = new Element("em");
        Element link = new Element("a")
                .attr("href", url.toString())
                .text(newsSource);
        return source.appendChild(tag.appendChild(link));

    }

    public String getHtml() {
        return html;
    }

    public void addCategory(Set<String> categoryList) {
        for (String category: categoryList){
            if (category.equals(mainCategory)){
                continue;
            }
            this.categories.add(category);
        }
    }

    public String getNewsSource() {
        return newsSource;
    }

    public String getTitle() {
        return title.text();
    }

    public String getDescription() {
        return description.text();
    }

    public String getThumbNail() {
        return thumbNail;
    }

    public String getAbsoluteTime() {
        return dtf.format(this.dateTime);
    }

    public String getRelativeTime() {
        long minutes = ChronoUnit.MINUTES.between(dateTime, LocalDateTime.now());

        if (minutes == 0) {
            long seconds = ChronoUnit.SECONDS.between(dateTime, LocalDateTime.now());
            if (seconds < 2)
                return "Just now.";
            return seconds + " seconds" + " ago.";
        } else if (minutes < 60) {
            return minutes + (minutes == 1 ? " minute" : " minutes") + " ago";
        } else if (minutes < 1440) { // 1440 minutes = 1 day
            long hours = minutes / 60;
            return hours + (hours == 1 ? " hour " : " hours ") + minutes % 60 + (minutes % 60 == 1 ? " minute" : " minutes") + " ago.";
        } else {
            long days = minutes / 1440;
            return days + (days == 1 ? " day" : " days") + " ago.";
        }
    }

    public long getMinutesSincePublished() {
        return ChronoUnit.MINUTES.between(dateTime, LocalDateTime.now());
    }


    // setter
    public void setContent(Element title, Element description, Element mainContent,
                           LocalDateTime publishedTime, String thumbNail, Set<String> categories) {
        if (title == null || description == null || mainContent == null || publishedTime == null) {
            throw new IllegalArgumentException();
        }
        this.title = title.addClass(TITLE);
        this.description = description.addClass(DESCRIPTION);
        this.dateTime = publishedTime;
        this.thumbNail = thumbNail;
        addCategory(categories);

        this.html = createHtml(mainContent.addClass(MAIN_CONTENT));
    }

    @Override
    public int compareTo(Article a) {
        return (int) (this.getMinutesSincePublished() - a.getMinutesSincePublished());
    }
}
