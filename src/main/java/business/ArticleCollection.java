package business;

import business.Helper.ArticleListGenerator;
import business.Helper.GetNewsOutlets;
import business.News.Article;
import business.News.Preview;
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

    // generate previews from articles scraped
    public static List<Preview> getPreviewsByCategory(String category) {
        // load articles if they haven't been loaded before
        if (articlesByCategories.get(category) == null) {
            loadArticlesByCategory(category);
        }

        return createPreviewsByCategory(category);
    }

    private static List<Preview> createPreviewsByCategory(String category) {
        // loop through articles in the category and create a list of their previews
        // sort article by published time
        List<Article> articles = articlesByCategories.get(category);
        if (articles == null) {
            return new ArrayList<>();
        }
        return articles
                .stream()
                .map(Article::getPreview)
                .sorted()
                .collect(Collectors.toList());
    }


    // MULTI THREADING!
    private static void loadArticlesByCategory(String category) {
        List<Article> safeArticleList = Collections.synchronizedList(new ArrayList<>());
        ExecutorService es = Executors.newCachedThreadPool();
//        CovidInfo.loadInfo();
        for (NewsOutlet newsOutlet : newsOutlets.values()) {
            es.execute(() -> {
                List<Article> articles = ArticleListGenerator
                        .getArticlesInCategory(newsOutlet, category);

                safeArticleList.addAll(articles);
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
