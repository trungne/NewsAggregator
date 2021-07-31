package News;

import org.jsoup.nodes.Element;

public class Preview implements Comparable<Preview>{
    Element preview;
    Article article;

    public Preview(Element preview, Article article){
        this.preview = preview;
        this.article = article;
    }

    public String toString(){
        return preview.outerHtml();
    }

    public String getHtml(){
        return preview.outerHtml();
    }

    public String getArticleHtml(){
        return article.getHtml();
    }


    @Override
    public int compareTo(Preview p) {
        return (int) (this.article.getMinutesSincePublished() - p.article.getMinutesSincePublished());
    }
}
