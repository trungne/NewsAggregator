package Scraper;

import News.Article;
import News.NewsOutlet;

import java.util.HashMap;
import java.util.HashSet;

public class ScraperTestDrive {
    public static void main(String[] args) {
        HashMap<String, NewsOutlet> newsOutlets = NewsOutlet.initializeNewsOutlets();
        HashSet<Article> allArticles = new HashSet<>();

        final long startTime = System.currentTimeMillis();

        Scraper scraper;
        for (String newsOutlet: newsOutlets.keySet()){
            scraper = new Scraper();
            NewsOutlet target = newsOutlets.get(newsOutlet);
            allArticles.addAll(scraper.scrape(target));
        }

        final long endTime = System.currentTimeMillis();
        System.out.println("Total execution time: " + (double) (endTime - startTime)/1000);

        System.out.println(allArticles.size());
//
        for (Article article: allArticles){
            article.displayTitleAndCategory();
            System.out.println("---- time: " + article.getDateTime());
        }
    }
}