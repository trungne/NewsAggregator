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
    private static final String PUBLISHED_TIME = "published-time";

    private static final String MAIN_CONTENT = "main-content";


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

    private final String url;
    private final String title;
    private final String description;
    private final String thumbNail;
    private final LocalDateTime dateTime;
    private final Set<String> categories = new HashSet<>();
    private final String newsSource;
    private final String html;

    public Article(String source,
                   String url,
                   String title,
                   String description,
                   String thumbnail,
                   LocalDateTime time,
                   String html) {
        this.newsSource = source;
        this.url = url;
        this.title = title;
        this.description = description;
        this.thumbNail = thumbnail;
        this.dateTime = time;
        this.html = html;
    }

    public String getHtml() {
        return html;
    }


    public String getNewsSource() {
        return newsSource;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
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


    @Override
    public int compareTo(Article a) {
        return (int) (this.getMinutesSincePublished() - a.getMinutesSincePublished());
    }
}
