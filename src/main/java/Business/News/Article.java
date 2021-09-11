package Business.News;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Article implements Comparable<Article> {
    private final String title;
    private final String description;
    private final String thumbNail;
    private final LocalDateTime dateTime;
    private final String newsSource;
    private final String html;

    public Article(String source,
                   String title,
                   String description,
                   String thumbnail,
                   LocalDateTime time,
                   String html) {
        this.newsSource = source;
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
