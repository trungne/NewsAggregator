package News;

import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

// an interface for presentation layer to access scraped articles
public class ArticleCollection {
    private static Collection<Article> articles;
    private static HashMap<String, URL> database;

    public static ArrayList<String> getPreviewsByCategory(String category){
        ArrayList<String> matchedArticles = new ArrayList<>();
        for (Article a: articles){
            // TODO: sort article by published time
            if(a.belongsToCategory(category)){
                matchedArticles.add(a.getPreview());
            }
        }
        return matchedArticles;
    }

    public static ArrayList<String> getArticlesByCategory(String category) {
        ArrayList<String> matchedArticles = new ArrayList<>();
        for (Article a : articles) {
            if (a.belongsToCategory(category)) {
                matchedArticles.add(a.getHtml());
            }
        }
        return matchedArticles;
    }


    // default are new category.
//    public static ArrayList<String> getDefaultArticles() {
//        return getArticlesByCategory("New");
//    }

    public static void loadArticles() throws InterruptedException {
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
