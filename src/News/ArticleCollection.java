package News;

import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

// an interface for presentation layer to access scraped articles
public class ArticleCollection {
    private static Collection<Article> articles;

    public static ArrayList<Preview> getPreviewsByCategory(String category) {
        if (articles == null){
            try {
                loadArticles();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        ArrayList<Preview> matchedPreviews = new ArrayList<>();
        for (Article a: articles){
            // TODO: sort article by published time
            if(a.belongsToCategory(category)){
                matchedPreviews.add(a.getPreview());
            }
        }
        Collections.sort(matchedPreviews);
        return matchedPreviews;
    }

    private static void loadArticles() throws InterruptedException {
        // TODO: From scraping news
        NewsOutletInfo[] newsOutletInfos = NewsOutletInfo.initializeNewsOutlets();
        List<Article> safeArticleList = Collections.synchronizedList(new ArrayList<>());
        ExecutorService es = Executors.newCachedThreadPool();

        for (int i = 0; i < newsOutletInfos.length; i++){
            final int INDEX = i;
            es.execute(() -> {
                safeArticleList.addAll((ArticleListGenerator.getArticles(newsOutletInfos[INDEX])));
            });
        }

        es.shutdown();
        boolean finished = es.awaitTermination(1, TimeUnit.MINUTES);

        if (finished){
            articles = safeArticleList;
        }


        // TODO: From database

    }
}
