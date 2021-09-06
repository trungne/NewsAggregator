package Application.Model;

import Business.News.Article;
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
    public String getCategory(){
        return this.category;
    }

    @Override
    protected Task<List<Article>> createTask() {
        return new GetArticleListTask(category);
    }
}
