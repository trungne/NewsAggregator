package Application.Model;

import business.News.Article;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.List;

public class GetArticleListService extends Service<List<Article>> {
    String category;

    public GetArticleListService(){

    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    protected Task<List<Article>> createTask() {
        return new ArticleListGetter(category);
    }
}
