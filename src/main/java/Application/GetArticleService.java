package Application;

import business.News.Article;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class GetArticleService extends Service<ObservableList<Article>> {

    @Override
    protected Task<ObservableList<Article>> createTask() {
        return null;
    }
}
