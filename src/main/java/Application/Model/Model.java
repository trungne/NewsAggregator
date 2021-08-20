package Application.Model;

import Application.Controller;
import business.News.Article;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Model{
    private final HashMap<String, List<Article>> articlesByCategories = new HashMap<>();
    private final Controller controller;
    private final GetArticleListService service = new GetArticleListService();

    public Model(Controller controller){
        this.controller = controller;
    }

    public GetArticleListService getService() {
        return service;
    }

    // this function is called when the service finished a.k.a when scraping articles is done
    // the default behaviour is to display page 1 of category selected
    public void notifyController(){
        this.controller.receiveNotificationByModel();
    }

    public List<Article> getArticleSublist(String category, int startIndex, int endIndex){
        List<Article> subList = new ArrayList<>();
        List<Article> articleList = articlesByCategories.get(category);
        for (int i = startIndex; i < endIndex; i++){
            try {
                subList.add(articleList.get(i));
            } catch (IndexOutOfBoundsException e){
                break;
            }
        }
        return subList;
    }

    public void loadArticles(String category){
        if (articlesByCategories.get(category) != null){
            notifyController();
            return;
        }

        service.reset();
        service.setCategory(category);
        service.setOnSucceeded(e -> {
            List<Article> newlyScrapedArticles = (List<Article>) e.getSource().getValue();
            articlesByCategories.put(category, newlyScrapedArticles);
            notifyController();
        });
        service.start();
    }
}
