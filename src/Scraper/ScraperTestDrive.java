package Scraper;

import News.Article;
import News.NewsOutlet;

import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ScraperTestDrive {
    public static void main(String[] args) throws InterruptedException {
        NewsOutlet[] newsOutlets = NewsOutlet.initializeNewsOutlets();
        HashSet<Article> allArticles = new HashSet<>();
        ExecutorService es = Executors.newCachedThreadPool();

        final long startTime = System.currentTimeMillis();

        for (int i = 0; i < newsOutlets.length; i++){
//            es.execute(scrapers[i]);
            final int INDEX = i;
            es.execute(() -> {
                Scraper scraper = new Scraper();
                allArticles.addAll(scraper.scrape(newsOutlets[INDEX]));
            });
        }
        es.shutdown();
        boolean finished = es.awaitTermination(1, TimeUnit.MINUTES);


        final long endTime = System.currentTimeMillis();

        System.out.println(allArticles.size());
//
//        for (Article article: allArticles){
//            article.displayTitleAndCategory();
//            System.out.println("---- time: " + article.getDateTime());
//        }
        System.out.println("Total execution time: " + (double) (endTime - startTime)/1000);

    }
}