package News;

import org.jsoup.nodes.Element;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;

public class Article {
    static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("EE, dd/MMMM/yyyy, kk:mm ");
    // attributes
    URL url;
    Element title;
    Element description;
    Element mainContent;
    String thumbNailUrl;
    LocalDateTime dateTime;
    HashSet<String> categories = new HashSet<>();

    public Article(){

    }
    public Article(URL url,
                   String category,
                   Element title,
                   Element description,
                   Element mainContent,
                   String thumbNailUrl,
                   LocalDateTime dateTime
                   ){
        this.url = url;
        this.title = title;
        this.description = description;
        this.mainContent = mainContent;
        this.thumbNailUrl = thumbNailUrl;
        this.dateTime = dateTime;
        this.categories.add(category);
    }

    public boolean belongsToCategory(String category){
        return this.categories.contains(category);
    }



    // TODO: only for testing
    public void displayTitleAndCategory(){
        System.out.println(url);
        System.out.println(this.mainContent + ". Category: " + this.categories.toString());
    }

    public String getHtml(){
        // String builder may not be thread-safe
        StringBuilder s = new StringBuilder(title.outerHtml());
        s.append(description.outerHtml());
        s.append(mainContent.outerHtml());
        return s.toString();
    }

    public String getUrl(){
        return this.url.toString();
    }

    public String getTitle() {
        return title.text();
    }

    public String getDateTime(){
        return dtf.format(this.dateTime);
    }

    public String getThumbNailUrl() {
        return thumbNailUrl;
    }

    // setters
    public void setUrl(URL url) {
        this.url = url;
    }

    public void setCategories(HashSet<String> categories) {
        this.categories = categories;
    }

    public void setTitle(Element title) {
        this.title = title;
    }

    public void setDescription(Element description) {
        this.description = description;
    }

    public void setMainContent(Element mainContent) {
        this.mainContent = mainContent;
    }

    public void setThumbNailUrl(String thumbNailUrl) {
        this.thumbNailUrl = thumbNailUrl;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void addCategory(String category){
        // check valid category
        this.categories.add(category);
    }
}
