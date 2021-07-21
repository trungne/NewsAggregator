package Scraper;

import News.*;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ScraperTestDrive {
    public static void main(String[] args) throws InterruptedException {
        final long startTime = System.currentTimeMillis();

        ArticleCollection.loadArticles();
        ArrayList<String> previews = ArticleCollection.getPreviewsByCategory(CATEGORY.POLITICS);
        for (String preview: previews){
            System.out.println(preview);
        }

        final long endTime = System.currentTimeMillis();
        System.out.println("Total execution time: " + (double) (endTime - startTime)/1000);

    }
}