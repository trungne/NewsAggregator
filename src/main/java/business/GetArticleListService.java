package business;

import business.ArticleListGetter;
import business.News.Article;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;

import java.util.List;

public class GetArticleListService extends Service<List<Article>> {
    String category;
    public GetArticleListService(String category){
        this.category = category;
    }

    public  GetArticleListService(){

    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    protected Task<List<Article>> createTask() {
        return new ArticleListGetter(category);
    }
}
