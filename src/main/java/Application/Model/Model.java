package Application.Model;

import Application.Controller;
import business.News.Article;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Model extends Task<List<Article>> {
    private final HashMap<String, List<Article>> articlesByCategories = new HashMap<>();
    private final HashMap<Integer, List<Article>> currentArticles = new HashMap<>();
    private final ObservableList<Article> articles =
            FXCollections.synchronizedObservableList(
                    FXCollections.observableList(
                            new ArrayList<>()));

    private final Controller controller;
    GetArticleListService service = new GetArticleListService();

    public Model(Controller controller){
        this.controller = controller;
        service.setOnSucceeded(e -> {
            List<Article> newlyScrapedArticles = (List<Article>) e.getSource().getValue();
            populateArticlePages(newlyScrapedArticles);
            this.controller.displayPreviews(1);
        });
    }

    public GetArticleListService getService() {
        return service;
    }

    private void populateArticlePages(List<Article> articles){
        for (int i = 1; i <= 5; i++){
            int lowerBound = (i - 1) * 10;
            int upperBound = i * 10;
            try{
                List<Article> sublist = articles.subList(lowerBound, upperBound);
                currentArticles.put(i, sublist);
            } catch (IndexOutOfBoundsException ignored){};

        }
    }

    @Override
    protected List<Article> call() throws Exception {
        return null;
    }

    public List<Article> getArticles(int page){
        if (page <= 0 || page > 5){
            throw new IllegalArgumentException();
        }
        return currentArticles.get(page);
    }


    public void loadArticles(String category){
        service.reset();
        service.setCategory(category);
        service.start();
    }
}
