package News;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

// an interface for presentation layer to access scraped articles
public class ArticleCollection {
    // map articles with their category
    public static final HashMap<String, Collection<Article>> articlesByCategories = new HashMap<>();

    // get all news outlet css info
    public static final HashMap<String, NewsOutletInfo> newsOutlets = GetNewsOutlets.newsOutlets;

    public static Collection<Preview> getPreviewsByCategory(String category) {
        // load articles if they haven't been loaded before
        if (!articlesByCategories.containsKey(category) || articlesByCategories.get(category) == null) {
            try {
                loadArticlesByCategory(category);
            } catch (InterruptedException e) {
                // TODO: display error message when articles cannot be load
                e.printStackTrace();
                return new ArrayList<>();
            }
        }

        /*
         * Special add-on info for Covid news
         * Delete this when Covid is not a thing anymore
         */
        if (category.equals(CATEGORY.COVID)){
            for (Article article: articlesByCategories.get(category)){
                try{
                    article.getMainContent().append(CovidInfo.getCovidInfo());
                } catch (NullPointerException e) {
                    System.out.println(article.url);
                    System.out.println(article.title);
                }
            }
        }

        // loop through articles in the category and create a list of their previews
        ArrayList<Preview> matchedPreviews = new ArrayList<>();
        for (Article article: articlesByCategories.get(category)) {
            matchedPreviews.add(article.getPreview());
        }

        // sort article by published time
        Collections.sort(matchedPreviews);

        return matchedPreviews;
    }

    // MULTI THREADING!
    private static void loadArticlesByCategory(String category) throws InterruptedException{
        List<Article> safeArticleList = Collections.synchronizedList(new ArrayList<>());
        ExecutorService es = Executors.newCachedThreadPool();
//        CovidInfo.loadInfo();
        for (String newsOutlet: newsOutlets.keySet()){
            es.execute(() -> {
                Collection<Article> articles = ArticleListGenerator
                        .getArticlesInCategory(newsOutlets.get(newsOutlet), category);
                safeArticleList.addAll(articles);
            });
        }

        es.shutdown();
        boolean finished = es.awaitTermination(30, TimeUnit.SECONDS);

        if (finished){
            articlesByCategories.put(category, safeArticleList);
        }
    }
}
