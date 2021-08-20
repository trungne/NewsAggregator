package Application.Model;

import Application.Controller;
import business.News.Article;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Model {
    private final HashMap<String, List<Article>> articlesByCategories = new HashMap<>();

    private final Controller controller;
    private final GetArticleListService service = new GetArticleListService();

    public Model(Controller controller){
        this.controller = controller;
    }

    public GetArticleListService getService() {
        return service;
    }

    // this function is called when the service finished, that is when scraping articles is done
    public void notifyController(){
        this.controller.updatePreviewsPane();
    }

    public void notifyController(List<Article> newlyScrapedArticles){
        this.controller.updatePreviewsPane();
    }

    public List<IndexedArticle> getArticleSublist(String category, int startIndex, int endIndex){
        List<IndexedArticle> subList = new ArrayList<>();
        List<Article> articleList = articlesByCategories.get(category);
        for (int i = startIndex; i < endIndex; i++){
            try {
                IndexedArticle indexedArticle = new IndexedArticle(articleList.get(i), i);
                subList.add(indexedArticle);
            } catch (IndexOutOfBoundsException e){
                break;
            }
        }
        return subList;
    }

    public String getArticleContent(String category, int index){
        return articlesByCategories.get(category).get(index).getHtml();
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
