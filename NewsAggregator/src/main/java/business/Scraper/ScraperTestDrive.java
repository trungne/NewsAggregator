package business.Scraper;

import business.ArticleListGetter;
import business.Helper.CATEGORY;
import business.News.Article;

import java.util.List;

public class ScraperTestDrive {
    public static void main(String[] args) {
        final long startTime = System.currentTimeMillis();

        final long endTime = System.currentTimeMillis();
//        System.out.println("Total preview: " + articles.size());
        System.out.println("Total execution time: " + (double) (endTime - startTime) / 1000);

    }
}