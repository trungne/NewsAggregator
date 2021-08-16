package business.News;

import business.Helper.CSS;
import business.NewsSources.NewsOutlet;
import org.jsoup.nodes.Element;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class Article implements Comparable<Article>{
    static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("EE, dd/MMMM/yyyy, kk:mm ");

    static boolean validateTag(Element e, String type, URL url) throws Exception {
        if (e == null)
            throw new Exception("Element For " + type + " Not Found");
        return true;
    }

    private Element getBodyTag(){
        Element body = new Element("body");
        body.appendChild(getArticleTag());
        return body;
    }

    private Element getHeadTag(){
        Element head = new Element("head");
        Element title = new Element("title");
        title.text(getTitle());

        head.append("<meta charset=\"UTF-8\">");
        head.append("<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">");
        head.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
        head.appendChild(title);
        head.appendChild(style);
        return head;
    }

    private Element getHtmlTag(){
        Element html = new Element("html");
        html.attr("lang", "vi");
        return html;
    }
    public String getHtml(){
        String docString = "<!DOCTYPE html>";
        Element html = getHtmlTag();
        html.appendChild(getHeadTag());
        html.appendChild(getBodyTag());
        return docString + html.outerHtml();
    }

    private URL url;
    private Element title;
    private Element description;
    private Element mainContent;
    private String thumbNail;
    private LocalDateTime dateTime;
    private final List<String> categories = new ArrayList<>();
    private final String newsSource;

    private final Element style = new Element("style");

    public Article(URL url, NewsOutlet newsOutlet, List<String> categoryList) {
        this.url = url;
        this.newsSource = newsOutlet.getName();
        addCategory(categoryList);
    }

    public Article(URL url, NewsOutlet newsOutlet, String parentCategory) {
        this.url = url;
        this.newsSource = newsOutlet.getName();
        addCategory(parentCategory);
    }

    public void addCategory(String category) {
        if (!this.categories.contains(category)) {
            this.categories.add(category);
        }

    }

    public void addCategory(List<String> categoryList) {
        for (String category : categoryList) {
            if (!this.categories.contains(category)) {
                this.categories.add(category);
            }
        }
    }

    public String getCategory(){
        return categories.toString();
    }

    public String getUrl() {
        return url.toString();
    }

    public String getTitle() {
        return title.text();
    }

    public String getDescription(){
        return description.text();
    }

    public String getThumbNail() {
        return thumbNail;
    }

    public String getNewsSource() {
        return newsSource;
    }

    public boolean belongsToCategory(String category) {
        return this.categories.contains(category);
    }

    private Element getArticleTag() {
        Element article = new Element("article");

        // create header div
        Element header = new Element("div");
        header.addClass(CSS.ARTICLE_HEADER);

        // category div
        Element categories = new Element("div");
        categories.addClass(CSS.ARTICLE_CATEGORY);
        StringBuilder categoriesStrBuilder = new StringBuilder();
        for (String category : this.categories) {
            categoriesStrBuilder.append(category).append(" - ");
        }

        // remove the " - " at the end;
        String categoriesStr = categoriesStrBuilder.substring(0, categoriesStrBuilder.length() - 2);
        categories.text(categoriesStr);

        // published time div
        Element publishedTime = new Element("div");
        publishedTime.addClass(CSS.PUBLISHED_TIME);
        publishedTime.text(getAbsoluteTime());

        header.appendChild(categories);
        header.appendChild(publishedTime);

        // create article content div which contains title, desp, and main content
        Element content = new Element("div");
        content.addClass(CSS.ARTICLE_CONTENT);
        content.appendChild(title);
        content.appendChild(description);
        content.appendChild(mainContent);

        article.appendChild(header);
        article.appendChild(content);

        return article;
    }
    public void setStyle(String css){
        this.style.text(css);
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


    // setters
    public void setUrl(URL url) {
        this.url = url;
    }

    public void setTitle(Element title) throws Exception {
        if (validateTag(title, "Title", url))
            this.title = title;
    }

    public void setDescription(Element description) throws Exception {
        if (validateTag(description, "Description", url))
            this.description = description;
    }

    public void setMainContent(Element mainContent) throws Exception {
        if (validateTag(mainContent, "Main Content", url))
            this.mainContent = mainContent;
    }

    public void setThumbNailUrl(String thumbNail){
        this.thumbNail = thumbNail;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public int compareTo(Article a) {
        return (int) (this.getMinutesSincePublished() - a.getMinutesSincePublished());
    }
}
