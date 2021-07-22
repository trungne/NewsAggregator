package News;

import org.jsoup.nodes.Element;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;

public class Article {
    static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("EE, dd/MMMM/yyyy, kk:mm ");
    // attributes
    URL url;
    Element title;
    Element description;
    Element mainContent;
    Element thumbNail;
    LocalDateTime dateTime;
    HashSet<String> categories = new HashSet<>();

    public Article(){

    }

    public boolean belongsToCategory(String category){
        return this.categories.contains(category);
    }

    public String getHtml(){
        Element article = new Element("article");

        // create header div
        Element header = new Element("div");
        header.addClass(CSSConvention.ARTICLE_HEADER);

        // category div
        Element categories = new Element("div");
        categories.addClass(CSSConvention.ARTICLE_CATEGORY);
        StringBuilder categoriesStrBuilder = new StringBuilder();
        for (String category: this.categories){
            categoriesStrBuilder.append(category).append(" - ");
        }
        String categoriesStr = categoriesStrBuilder.substring(0, categoriesStrBuilder.length() - 2);
        categories.text(categoriesStr);

        // published time div
        Element publishedTime = new Element("div");
        publishedTime.addClass(CSSConvention.PUBLISHED_TIME);
        publishedTime.text(getAbsoluteTime());

        header.appendChild(categories);
        header.appendChild(publishedTime);

        // create article content div which contains title, desp, and main content
        Element content = new Element("div");
        content.addClass(CSSConvention.ARTICLE_CONTENT);

        content.appendChild(title);
        content.appendChild(description);
        content.appendChild(mainContent);

        article.appendChild(header);
        article.appendChild(content);

        return article.outerHtml();
    }

    public String getAbsoluteTime(){
        return dtf.format(this.dateTime);
    }

    public String getRelativeTime(){
        long minutes = ChronoUnit.MINUTES.between(dateTime, LocalDateTime.now());

        if (minutes == 0){
            long seconds = ChronoUnit.SECONDS.between(dateTime, LocalDateTime.now());
            if (seconds < 2)
                return "Just now.";
            return seconds + " seconds" + " ago.";
        }
        else if (minutes < 1440){ // 1440 minutes = 1 day
            long hours = minutes/60;
            return hours + (hours == 1 ? " hour " : " hours ") + minutes%60 + (minutes == 1 ? " minute" : " minutes") + " ago.";
        }
        else if (minutes >= 1440){
            long days = minutes/1440;
            return days + (days == 1 ? " day" : "days") + " ago.";
        }
        else{
            return "Just now.";
        }

    }

    public long getMinutesSincePublished() {
        return ChronoUnit.MINUTES.between(dateTime, LocalDateTime.now());
    }


    // setters
    public void setUrl(URL url) {
        this.url = url;
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

    public void setThumbNailUrl(Element thumbNail) {
        this.thumbNail = thumbNail;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void addCategory(String category){
        // check valid category
        this.categories.add(category);
    }


    public Preview getPreview(){
        Element preview = new Element("div");
        preview.addClass(CSSConvention.PREVIEW);

        Element relativeTime = new Element("div");
        relativeTime.addClass(CSSConvention.PUBLISHED_TIME);
        relativeTime.text(getRelativeTime());

        Element thumbTitle = new Element("div");
        thumbTitle.addClass(CSSConvention.THUMBNAIL_TITLE);
        thumbTitle.text(title.text());

        Element thumbDesp = new Element("div");
        thumbDesp.addClass(CSSConvention.THUMBNAIL_DESCRIPTION);
        thumbDesp.text(description.text());

        preview.appendChild(thumbNail);
        preview.appendChild(thumbTitle);
        preview.appendChild(thumbDesp);
        preview.appendChild(relativeTime);

        return new Preview(preview, this);

    }


}
