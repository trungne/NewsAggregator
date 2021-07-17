package Scraper;

import News.Article;
import News.NewsOutlet;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ScraperTestDrive {
    public static void main(String[] args) throws InterruptedException {
        NewsOutlet[] newsOutlets = NewsOutlet.initializeNewsOutlets();
        List<Article> safeArticleList = Collections.synchronizedList(new ArrayList<>());
        ExecutorService es = Executors.newCachedThreadPool();

        final long startTime = System.currentTimeMillis();

        for (int i = 0; i < newsOutlets.length; i++){
            final int INDEX = i;
            es.execute(() -> {
                (new Scraper()).scrapeWebAndFillCollection(newsOutlets[INDEX], safeArticleList);
//                allArticles.addAll((new Scraper()).scrape(newsOutlets[INDEX]));
            });
        }
        es.shutdown();
        boolean finished = es.awaitTermination(1, TimeUnit.MINUTES);


        final long endTime = System.currentTimeMillis();

        System.out.println(safeArticleList.size());
//
        for (Article article: safeArticleList){
            article.displayTitleAndCategory();
            System.out.println("---- time: " + article.getDateTime());
        }
        System.out.println("Total execution time: " + (double) (endTime - startTime)/1000);

    }
}