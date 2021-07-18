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

    public void addCategory(String category){
        // check valid category
        this.categories.add(category);
    }

    // TODO: only for testing
    public void displayTitleAndCategory(){
        System.out.println(url);
        System.out.println("Title: " + this.description + ". Category: " + this.categories.toString());
    }


    public String getDateTime(){
        return dtf.format(this.dateTime);
    }

    public String getThumbNailUrl() {
        return thumbNailUrl;
    }
}
