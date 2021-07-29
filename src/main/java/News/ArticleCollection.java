package News;

import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

// an interface for presentation layer to access scraped articles
public class ArticleCollection {
    private static final HashMap<String, Collection<Article>> articlesByCategories = new HashMap<>();
    private static final NewsOutletInfo[] newsOutletInfos = NewsOutletInfo.initializeNewsOutlets();

    public static ArrayList<Preview> getPreviewsByCategory(String category) {
        if (!articlesByCategories.containsKey(category) || articlesByCategories.get(category) == null) {
            try {
                loadArticlesByCategory(category);
            } catch (InterruptedException e) {
                // TODO: display error message when articles cannot be load
                e.printStackTrace();
            }
        }

        ArrayList<Preview> matchedPreviews = new ArrayList<>();
        for (Article article: articlesByCategories.get(category)){
            matchedPreviews.add(article.getPreview());
        }


        // sort article by published time
        Collections.sort(matchedPreviews);
        return matchedPreviews;
    }

    private static void loadArticlesByCategory(String category) throws InterruptedException{
        List<Article> safeArticleList = Collections.synchronizedList(new ArrayList<>());
        ExecutorService es = Executors.newCachedThreadPool();

        for (int i = 0; i < newsOutletInfos.length; i++){
            final int INDEX = i;
            es.execute(() -> {

                Collection<Article> articles = ArticleListGenerator.getArticlesInCategory(newsOutletInfos[INDEX], category);
                safeArticleList.addAll(articles);
            });
        }

        es.shutdown();
        boolean finished = es.awaitTermination(30, TimeUnit.SECONDS);

        if (finished){
            articlesByCategories.put(category, safeArticleList);
        }
    }

    private static void loadArticles() throws InterruptedException {

    }
}
