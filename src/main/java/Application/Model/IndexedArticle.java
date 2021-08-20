package Application.Model;

import business.News.Article;

import java.net.URL;

public class IndexedArticle{
    private final int index;
    private final Article article;
    public IndexedArticle(Article article, int index){
        this.article = article;
        this.index = index;
    }

    public String getThumbNail(){
        return article.getThumbNail();
    }

    public String getTitle(){
        return article.getTitle();
    }

    public String getDescription(){
        return article.getDescription();
    }

    public String getPublishedTime(){
        return article.getRelativeTime();
    }

    public String getSource(){
        return article.getNewsSource();
    }

    public int getIndex(){
        return index;
    }


}
