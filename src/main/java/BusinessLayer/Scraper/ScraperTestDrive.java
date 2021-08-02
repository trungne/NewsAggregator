package BusinessLayer.Scraper;

import BusinessLayer.*;
import BusinessLayer.Helper.CATEGORY;
import BusinessLayer.News.Preview;

import java.util.*;

public class ScraperTestDrive {
    public static void main(String[] args) {
        final long startTime = System.currentTimeMillis();

        Collection<Preview> previews = ArticleCollection.getPreviewsByCategory(CATEGORY.HEALTH);

        for (Preview preview: previews){
            System.out.println(preview);
        }

        final long endTime = System.currentTimeMillis();
        System.out.println("Total execution time: " + (double) (endTime - startTime)/1000);

    }
}