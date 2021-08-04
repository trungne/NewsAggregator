package business;

import business.Helper.*;
import business.News.*;
import business.NewsSources.*;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

// an interface for presentation layer to access scraped articles
public class ArticleCollection {
    // map articles with their category
    public static final HashMap<String, Collection<Article>> articlesByCategories = new HashMap<>();

    // get all news outlet css info
    public static final HashMap<String, NewsOutlet> newsOutlets = GetNewsOutlets.newsOutlets;

    // generate previews from articles scraped
    public static List<Preview> getPreviewsByCategory(String category) {
        // load articles if they haven't been loaded before
        if (articlesByCategories.get(category) == null) {
            try {
                loadArticlesByCategory(category);
            } catch (InterruptedException e) {
                // TODO: display error message when articles cannot be load
                e.printStackTrace();
                return new ArrayList<>();
            }
        }
        return createPreviewsByCategory(category);


    }
    private static List<Preview> createPreviewsByCategory(String category){
        // loop through articles in the category and create a list of their previews
        // sort article by published time
        return articlesByCategories.get(category)
                .stream()
                .map(Article::getPreview)
                .sorted()
                .collect(Collectors.toList());
    }


    // MULTI THREADING!
    private static void loadArticlesByCategory(String category) throws InterruptedException{
        List<Article> safeArticleList = Collections.synchronizedList(new ArrayList<>());
        ExecutorService es = Executors.newCachedThreadPool();
//        CovidInfo.loadInfo();
        for (NewsOutlet newsOutlet: newsOutlets.values()){
            es.execute(() -> {
                List<Article> articles = ArticleListGenerator
                        .getArticlesInCategory(newsOutlet, category);
                safeArticleList.addAll(articles);
            });
        }

        es.shutdown();
        boolean finished = es.awaitTermination(30, TimeUnit.SECONDS);


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


        if (finished){
            articlesByCategories.put(category, safeArticleList);
        }
    }
}
