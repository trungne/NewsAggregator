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

        ArrayList<Preview> previews = ArticleCollection.getPreviewsByCategory(CATEGORY.POLITICS);
//        for (Preview preview: previews){
//            System.out.println(preview);
//        }
        System.out.println(previews.get(0).toString());
        System.out.println(previews.get(0).getArticleHtml());

        final long endTime = System.currentTimeMillis();
        System.out.println("Total execution time: " + (double) (endTime - startTime)/1000);

    }
}