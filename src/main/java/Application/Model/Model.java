/*
  RMIT University Vietnam
  Course: INTE2512 Object-Oriented Programming
  Semester: 2021B
  Assessment: Final Project
  Created  date: dd/mm/yyyy
  Author: Student name, Student ID
  Last modified date: dd/mm/yyyy
  Author: Student name, Student ID
  Acknowledgement: Thanks and give credits to the resources that you used in this file
*/

package Application.Model;

import Application.Controller.MainController;
import Business.News.Article;
import javafx.concurrent.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Model {
    private final HashMap<String, List<Article>> articlesByCategories = new HashMap<>();
    private final int MAX_ARTICLES_IN_STACK = 10;
    private final LinkedList<Article> selectedArticles = new LinkedList<>();
    private int currentSelectedArticleIndex;

    private final MainController controller;
    private final GetArticleListService service = new GetArticleListService();

    public Model(MainController controller){
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
    public Article getArticle(String category, int index){
        if (articlesByCategories.get(category) == null){
            return null;
        }
        return articlesByCategories.get(category).get(index);
    }

    /** Get a particular article in a category and store it in article stack
     * @param category the category to get article from
     * @param index the index of the article
     * @return a particular article in the category
     * */
    public Article getArticleAndStore(String category, int index){
        Article a = getArticle(category, index);
        if (a == null){
            return null;
        }
        addArticleToStack(a);
        currentSelectedArticleIndex = 0;

        return a;
    }

    public void addArticleToStack(Article a){
        if (selectedArticles.size() == MAX_ARTICLES_IN_STACK){
            selectedArticles.removeLast();
        }

        selectedArticles.remove(a);

        selectedArticles.addFirst(a);
    }

    public Article nextArticle(){
        if (hasNoNextArticle()){
            return null;
        }

        currentSelectedArticleIndex--;
        return selectedArticles.get(currentSelectedArticleIndex);

    }

    public boolean hasNoNextArticle(){
        return currentSelectedArticleIndex == 0;
    }

    public Article previousArticle(){
        if (hasNoPreviousArticle()){
            return null;
        }

        currentSelectedArticleIndex++;
        return selectedArticles.get(currentSelectedArticleIndex);
    }

    public boolean hasNoPreviousArticle(){
        return currentSelectedArticleIndex == selectedArticles.size() - 1;
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
        selectedArticles.clear();
        articlesByCategories.remove(category);
    }
    /** Clear all articles in the hashmap
     * */
    public void refresh(){
        selectedArticles.clear();
        articlesByCategories.clear();
    }
}
