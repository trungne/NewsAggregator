package business.Scraper;

import business.ArticleCollection;
import business.Helper.CATEGORY;
import business.News.Article;

import java.util.Collection;
import java.util.List;

public class ScraperTestDrive {
    public static void main(String[] args) {
        final long startTime = System.currentTimeMillis();
        List<Article> articles = ArticleCollection.getArticlesByCategory(CATEGORY.NEW);

        for (Article article : articles) {
            System.out.println("=============");
            System.out.println(article.getUrl());
            System.out.println(article.getCategory());
        }

        final long endTime = System.currentTimeMillis();
        System.out.println("Total preview: " + articles.size());
        System.out.println("Total execution time: " + (double) (endTime - startTime) / 1000);

    }
}