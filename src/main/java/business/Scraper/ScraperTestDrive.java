package business.Scraper;

import business.*;
import business.Helper.CATEGORY;
import business.News.Preview;

import java.util.*;

public class ScraperTestDrive {
    public static void main(String[] args) {
        final long startTime = System.currentTimeMillis();

        Collection<Preview> previews = ArticleCollection.getPreviewsByCategory(CATEGORY.COVID);

        for (Preview preview: previews){
            System.out.println(preview);
        }

        final long endTime = System.currentTimeMillis();
        System.out.println("Total execution time: " + (double) (endTime - startTime)/1000);

    }
}