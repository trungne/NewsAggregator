package Application.Model;

import business.News.ArticleListGenerator;
import business.News.Article;
import business.Scraper.ArticleCrawler.*;
import business.Scraper.Service;
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

import static business.Scraper.Helper.ScrapingUtils.MAX_ARTICLES_DISPLAYED;
import static business.Scraper.Helper.ScrapingUtils.MAX_TERMINATION_TIME;

// an interface for presentation layer to access scraped articles
public class GetArticleListTask extends Task<List<Article>> {
    // get all news outlet css info
    private static final List<Scraper> newsOutlets = Service.initScrapers();

    private final String category;

    public GetArticleListTask(String category){
        this.category = category;
    }

    @Override
    protected List<Article> call(){
        ObservableList<Article> articles = FXCollections
                .synchronizedObservableList(
                        FXCollections.observableList(
                        new ArrayList<>()));

        // update progress bar
        articles.addListener((ListChangeListener<Article>)
                change -> updateProgress(
                        change.getList().size(),
                        MAX_ARTICLES_DISPLAYED)); // max 50

        updateArticleList(articles);
        return articles;
    }

    private void updateArticleList(List<Article> articles) {
        ExecutorService es = Executors.newCachedThreadPool();
        for (Scraper scraper : newsOutlets) {
            es.execute(()-> {
                ArticleListGenerator generator = new ArticleListGenerator(scraper, category);
                generator.populateArticleList(articles);
            });
        }

        shutdownAndAwaitTermination(es);
        Collections.sort(articles);
    }

// https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ExecutorService.html
    private void shutdownAndAwaitTermination(ExecutorService pool) {
        pool.shutdown(); // Disable new tasks from being submitted
        try {
            // Wait a while for existing tasks to terminate
            if (!pool.awaitTermination(MAX_TERMINATION_TIME, TimeUnit.SECONDS)) {
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

