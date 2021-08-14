package business;

import business.Helper.ArticleListGenerator;
import business.Helper.GetNewsOutlets;
import business.News.Article;
import business.NewsSources.NewsOutlet;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static business.Helper.ScrapingConfiguration.MAX_TERMINATION_TIME;

// an interface for presentation layer to access scraped articles
public class ArticleListGetter extends Task<List<Article>> {
    // map articles with their category
    private static final HashMap<String, List<Article>> articlesByCategories = new HashMap<>();
    // get all news outlet css info
    private static final HashMap<String, NewsOutlet> newsOutlets = GetNewsOutlets.newsOutlets;

    private final String category;

    public ArticleListGetter(String category){
        this.category = category;
    }

    @Override
    protected List<Article> call() throws Exception {
        if (articlesByCategories.get(category) != null){
            return articlesByCategories.get(category);
        }

        ObservableList<Article> articles = FXCollections
                .synchronizedObservableList(
                        FXCollections.observableList(
                        new ArrayList<>()));

        // update progress bar
        articles.addListener((ListChangeListener<Article>) change ->
                updateProgress(change.getList().size(), 50));

        updateArticleList(articles);

        return articles;
    }

    public void updateArticleList(List<Article> articles) {
        ExecutorService es = Executors.newSingleThreadExecutor();
        for (NewsOutlet newsOutlet : newsOutlets.values()) {
            es.execute(()->{
                ArticleListGenerator generator = new ArticleListGenerator(newsOutlet, category);
                generator.populateArticleList(articles);
            });
        }

        shutdownAndAwaitTermination(es);

        Collections.sort(articles);
        articlesByCategories.put(category, articles);

    }

// https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ExecutorService.html
    public void shutdownAndAwaitTermination(ExecutorService pool) {
        pool.shutdown(); // Disable new tasks from being submitted
        try {
            // Wait a while for existing tasks to terminate
            if (!pool.awaitTermination(30, TimeUnit.SECONDS)) {
                pool.shutdownNow(); // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
                if (!pool.awaitTermination(30, TimeUnit.SECONDS))
                    System.err.println("Pool did not terminate");
            }
        } catch (InterruptedException ie) {
            // (Re-)Cancel if current thread also interrupted
            pool.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
        }

    }


}

