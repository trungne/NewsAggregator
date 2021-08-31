package Application.Model;

import Application.Controller;
import Business.News.Article;

import java.util.ArrayList;
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
            articlesByCategories.put(service.category, newlyScrapedArticles);
            notifyController();
        });
    }

    public GetArticleListService getService() {
        return service;
    }

    // this function is called when the service finished, that is when scraping articles is done
    private void notifyController(){
        this.controller.updatePreviewsPane();
    }


    /** Get a sub list of articles in a category
     * @param category category to get articles from
     * @param startIndex start index of the sub list
     * @param endIndex end index of the sub list
     * @return the sub list of articles in a category
     * */
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

    /** Get a particular article in a category
     * @param category the category to get article from
     * @param index the index of the article
     * @return a particular article in the category
     * */
    public Article getArticleContent(String category, int index){
        return articlesByCategories.get(category).get(index);
    }

    /** First check if the category already has articles scraped. If yes, immediately notify the controller.
     * Otherwise, start scraping service
     * @param category scrape the articles in this category
     * */
    public void loadArticles(String category){
        if (hasData(category)){
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

    /** Check if data (articles) have been scraped for this category
     * @param category the category to check
     * @return true if the category has articles scraped, otherwise false
     * */
    public boolean hasData(String category){
        return articlesByCategories.get(category) != null;
    }
}
