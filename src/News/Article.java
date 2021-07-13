package News;

import News.Content.Detail;
import News.Content.DetailFactory;
import News.Content.ContentFactory;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;

public class Article {
    static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("EE, dd/MMMM/yyyy, kk:mm ");
    // attributes
    URL url;
    String title;
    String description;
    ArrayList<Detail> details;
    String thumbNailUrl;
    LocalDateTime dateTime;
    HashSet<String> categories = new HashSet<>();

    public Article(URL url,
                   String category,
                   String title,
                   String description,
                   ArrayList<Detail> details,
                   String thumbNailUrl,
                   LocalDateTime dateTime
                   ){
        this.url = url;
        this.title = title;
        this.description = description;
        this.details = details;
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

    public void displayTitleAndCategory(){
        System.out.println("Title: " + this.title + ". Category: " + this.categories.toString());
    }

    public String getDateTime(){
        return dtf.format(this.dateTime);
    }

    public String getThumbNailUrl() {
        return thumbNailUrl;
    }
}
