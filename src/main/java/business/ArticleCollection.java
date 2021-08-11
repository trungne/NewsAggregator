package business;

import business.Helper.ArticleListGenerator;
import business.Helper.CATEGORY;
import business.Helper.GetNewsOutlets;
import business.News.Article;
import business.NewsSources.NewsOutlet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static business.Helper.ScrapingConfiguration.MAX_TERMINATION_TIME;

// an interface for presentation layer to access scraped articles
public class ArticleCollection {
    // map articles with their category
    public static final HashMap<String, List<Article>> articlesByCategories = new HashMap<>();
    // get all news outlet css info
    public static final HashMap<String, NewsOutlet> newsOutlets = GetNewsOutlets.newsOutlets;

    public static void loadAllArticles(){
        for (String category: CATEGORY.ALL_CATEGORIES){
            articlesByCategories.put(category, getArticlesByCategory(category));
        }
    }

    // generate previews from articles scraped
    public static List<Article> getArticlesByCategory(String category) {
        // load articles if they haven't been loaded before
        if (articlesByCategories.get(category) == null) {
            loadArticlesByCategory(category);
        }

        return articlesByCategories.get(category);
    }



    // MULTI THREADING!
    public static void loadArticlesByCategory(String category) {
        List<Article> safeArticleList = Collections.synchronizedList(new ArrayList<>());
        ExecutorService es = Executors.newCachedThreadPool();
//        CovidInfo.loadInfo();
        for (NewsOutlet newsOutlet : newsOutlets.values()) {
            List<Article> finalSafeArticleList = safeArticleList;
            es.execute(() -> {
                List<Article> articles = ArticleListGenerator
                        .getArticlesInCategory(newsOutlet, category);

                finalSafeArticleList.addAll(articles);
            });
        }

        es.shutdown(); // Disable new tasks from being submitted
        try {
            if (!es.awaitTermination(MAX_TERMINATION_TIME, TimeUnit.SECONDS)) {
                es.shutdownNow(); // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
                if (!es.awaitTermination(MAX_TERMINATION_TIME, TimeUnit.SECONDS))
                    System.err.println("Pool did not terminate");
            }
        } catch (InterruptedException e) {
            es.shutdownNow();
        }

        safeArticleList = safeArticleList.stream().sorted().collect(Collectors.toList());
        articlesByCategories.put(category, safeArticleList);
    }
}

/*
 * Special add-on info for Covid news
 * Delete this when Covid is not a thing anymore
 */
//        if (category.equals(CATEGORY.COVID)){
//            for (Article article: articlesByCategories.get(category)){
//                try{
//                    article.getMainContent().append(CovidInfo.getCovidInfo());
//                } catch (NullPointerException e) {
//                    System.out.println(article.url);
//                    System.out.println(article.title);
//                }
//            }
//        }
