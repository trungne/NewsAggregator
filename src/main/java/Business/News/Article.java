/*
  RMIT University Vietnam
  Course: INTE2512 Object-Oriented Programming
  Semester: 2021B
  Assessment: Final Project
  Created  date: 10/08/2021
  Author: Nguyen Quoc Hoang Trung, S3818328
  Last modified date: 18/09/2021
  Author: Nguyen Quoc Hoang Trung, S3818328
  Acknowledgement: Comparable interface is used to compare 2 articles
   https://docs.oracle.com/javase/8/docs/api/java/lang/Comparable.html
*/

package Business.News;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Article implements Comparable<Article> {
    private final String TITLE;
    private final String DESCRIPTION;
    private final String THUMBNAIL;
    private final LocalDateTime DATETIME;
    private final String NEWS_SOURCE;
    private final String HTML;

    public Article(String source,
                   String title,
                   String description,
                   String thumbnail,
                   LocalDateTime time,
                   String html) {
        this.NEWS_SOURCE = source;
        this.TITLE = title;
        this.DESCRIPTION = description;
        this.THUMBNAIL = thumbnail;
        this.DATETIME = time;
        this.HTML = html;
    }

    public String getHtml() {
        return HTML;
    }

    public String getNewsSource() {
        return NEWS_SOURCE;
    }

    public String getTitle() {
        return TITLE;
    }

    public String getDescription() {
        return DESCRIPTION;
    }

    public String getThumbnail() {
        return THUMBNAIL;
    }

    public String getRelativeTime() {
        long minutes = ChronoUnit.MINUTES.between(DATETIME, LocalDateTime.now());

        if (minutes == 0) {
            long seconds = ChronoUnit.SECONDS.between(DATETIME, LocalDateTime.now());
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
        return ChronoUnit.MINUTES.between(DATETIME, LocalDateTime.now());
    }


    @Override
    public int compareTo(Article a) {
        return (int) (this.getMinutesSincePublished() - a.getMinutesSincePublished());
    }
}
