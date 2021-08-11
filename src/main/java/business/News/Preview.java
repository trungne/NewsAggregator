package business.News;

import org.jsoup.nodes.Element;

public class Preview implements Comparable<Preview> {
    private final String title;
    private final String description;
    private final String thumbnail;
    private final String relativePublishedTime;
    private final Article article;

    public Preview(Article article){
        this.article = article;
        this.title = article.getTitle();
        this.description = article.getDescription();
        this.thumbnail = article.getThumbNail();
        this.relativePublishedTime = article.getRelativeTime();
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getRelativePublishedTime() {
        return relativePublishedTime;
    }

    public String getNewsSource(){
        return article.getNewsSource();
    }


    public String toString() {
        return article.getHtml();
    }

    public String getHtml() {
        return article.getHtml();
    }

    public String getArticleHtml() {
        return article.getHtml();
    }

    public String getUrl(){
        return article.getUrl();
    }
    public String getCategory(){
        return article.getCategory();
    }
    @Override
    public int compareTo(Preview p) {
        return (int) (this.article.getMinutesSincePublished() - p.article.getMinutesSincePublished());
    }
}
