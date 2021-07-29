package Scraper;

import News.*;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ScraperTestDrive {
    public static void main(String[] args) {
        final long startTime = System.currentTimeMillis();

        ArrayList<Preview> previews = ArticleCollection.getPreviewsByCategory(CATEGORY.HEALTH);

        for (Preview preview: previews){
            System.out.println(preview.getArticleHtml());
        }

        final long endTime = System.currentTimeMillis();
        System.out.println("Total execution time: " + (double) (endTime - startTime)/1000);

    }
}