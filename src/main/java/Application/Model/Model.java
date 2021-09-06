package Application.Model;

import Application.Controller;
import Business.News.Article;
import javafx.concurrent.Service;

import java.util.HashMap;
import java.util.List;

public class Model {
    private final HashMap<String, List<Article>> articlesByCategories = new HashMap<>();

    private final Controller controller;
    private final GetArticleListService service = new GetArticleListService();

    public Model(Controller controller){
        this.controller = controller;
        service.setOnSucceeded(e -> {
            List<Article> newlyScrapedArticles = (List<Article>) e.getSource().getValue();
            articlesByCategories.put(service.getCategory(), newlyScrapedArticles);
            notifyController();
        });
    }

    public Service<List<Article>> getService() {
        return service;
    }

    // this function is called when the service finished, that is when scraping articles is done
    private void notifyController(){
        this.controller.updatePreviewsPane();
    }

    /** Get a particular article in a category
     * @param category the category to get article from
     * @param index the index of the article
     * @return a particular article in the category
     * */
    private Article getArticle(String category, int index){
        return articlesByCategories.get(category).get(index);
    }

    public String getArticleTitle(String category, int index){
        Article a = getArticle(category, index);
        if (a == null){
            return "";
        }
        return a.getTitle();
    }

    public String getArticleDescription(String category, int index){
        Article a = getArticle(category, index);
        if (a == null){
            return "";
        }
        return a.getDescription();
    }

    public String getArticleThumbnail(String category, int index){
        Article a = getArticle(category, index);
        if (a == null){
            return "";
        }
        return a.getThumbNail();
    }

    public String getArticleTime(String category, int index){
        Article a = getArticle(category, index);
        if (a == null){
            return "";
        }
        return a.getRelativeTime();
    }

    public String getArticleSource(String category, int index){
        Article a = getArticle(category, index);
        if (a == null){
            return "";
        }
        return a.getNewsSource();
    }

    public String getArticleHtml(String category, int index){
        Article a = getArticle(category, index);
        if (a == null){
            return "";
        }
        return a.getHtml();
    }



    /** First check if the category already has articles scraped. If yes, immediately notify the controller.
     * Otherwise, start scraping service
     * @param category scrape the articles in this category
     * */
    public void loadArticles(String category){
        // Check if articles have been scraped for this category
        if (articlesByCategories.get(category) != null){
            notifyController();
            return;
        }

        service.reset();
        service.setCategory(category);
        service.start();
    }

    /** Clear all articles in a particular category
     * @param category provided category to clear articles from
     * */
    public void refresh(String category){
        articlesByCategories.remove(category);
    }
    /** Clear all articles in the hashmap
     * */
    public void refresh(){
        articlesByCategories.clear();
    }
}
