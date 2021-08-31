package business.Scraper;

import business.News.Article;
import business.News.ArticleListGenerator;
import business.Scraper.ArticleCrawler.*;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.io.IOException;
import java.net.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import static business.Scraper.Helper.ScrapingUtils.MAX_TERMINATION_TIME;

public class ScrapingService {
    private static final List<Scraper> SCRAPERS = createScrapers();

    private static List<Scraper> createScrapers(){
        Scraper[] scrapes = new Scraper[]{
                VNExpressScraper.init(),
                ZingNewsScraper.init(),
                ThanhNienScraper.init(),
                TuoiTreScraper.init(),
                NhanDanScraper.init(),
        };
        List<Scraper> validScrapers = new ArrayList<>();
        for (Scraper scraper: scrapes){
            if(scraper != null){
                validScrapers.add(scraper);
            }
        }
        return validScrapers;
    }

    /** Get all scrapers to scrape article in a particular category
     * @param articles a provided list where articles will be added to
     * @param category a provided category where scrapers will get articles from
     * */
    public static void startScraping(List<Article> articles, String category){
        ExecutorService es = Executors.newCachedThreadPool();
        for (Scraper scraper : SCRAPERS) {
            es.execute(()-> {
                ArticleListGenerator generator = new ArticleListGenerator(scraper, category);
                generator.populateArticleList(articles);
            });
        }
        shutdownAndAwaitTermination(es);
        Collections.sort(articles);
    }

    // https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ExecutorService.html
    /** Stop accepting new tasks and wait for other threads in the pool to finish
     * @param pool the thread pool that we want to stop and join
     * */
    private static void shutdownAndAwaitTermination(ExecutorService pool) {
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
