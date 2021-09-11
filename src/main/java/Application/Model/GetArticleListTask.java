package Application.Model;

import Business.GetNewsOutlets;
import Business.News.Article;
import Business.NewsOutlet;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static Business.Scraper.Helper.ScrapingUtils.MAX_ARTICLES_DISPLAYED;
import static Business.Scraper.Helper.ScrapingUtils.MAX_TERMINATION_TIME;

// an interface for presentation layer to access scraped articles
public class GetArticleListTask extends Task<List<Article>> {
    private static final List<NewsOutlet> NEWS_OUTLETS = GetNewsOutlets.createNewsOutlets();
    private final String category;

    public GetArticleListTask(String category){
        this.category = category;
    }

    @Override
    protected List<Article> call() {
        ObservableList<Article> articles = FXCollections
                .synchronizedObservableList(
                        FXCollections.observableList(
                        new ArrayList<>()));

        // update progress bar
        articles.addListener((ListChangeListener<Article>) change -> {
            updateProgress(change.getList().size(), MAX_ARTICLES_DISPLAYED);
            updateMessage("Scraping articles (" + change.getList().size() + "/" + MAX_ARTICLES_DISPLAYED + ")");
            if (change.getList().size() >= MAX_ARTICLES_DISPLAYED){
                updateMessage("Sorting articles...");
            }
        });

        updateMessage("Scraping articles...");
        fillUpArticleList(articles, category);
        return articles;
    }

    /** Get all scrapers to scrape article in a particular category
     * @param articles a provided list where articles will be added to
     * @param category a provided category where scrapers will get articles from
     * */
    public void fillUpArticleList(List<Article> articles, String category){
        ExecutorService es = Executors.newCachedThreadPool();
        for (NewsOutlet newsOutlet : NEWS_OUTLETS) {
            es.execute(()-> newsOutlet.populateArticleList(articles, category));
        }
        // TODO: something is slow here but I cant figure out what
        shutdownAndAwaitTermination(es);
        updateMessage("Sorting articles...");
        Collections.sort(articles);
    }

    // https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ExecutorService.html
    /** Stop accepting new tasks and wait for other threads in the pool to finish
     * @param pool the thread pool that we want to stop and join
     * */
    private void shutdownAndAwaitTermination(ExecutorService pool) {
        pool.shutdown(); // Disable new tasks from being submitted
        try {
            // Wait a while for existing tasks to terminate
            if (!pool.awaitTermination(MAX_TERMINATION_TIME, TimeUnit.SECONDS)) {
                System.err.println("Shutdown now...");
                pool.shutdownNow(); // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
                if (!pool.awaitTermination(MAX_TERMINATION_TIME, TimeUnit.SECONDS))
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