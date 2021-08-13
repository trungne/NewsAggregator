package business;

import business.Helper.ArticleListGenerator;
import business.Helper.GetNewsOutlets;
import business.News.Article;
import business.NewsSources.NewsOutlet;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

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
public class ArticleListGetter extends Task<List<Article>> {
    // map articles with their category
    private static final HashMap<String, List<Article>> articlesByCategories = new HashMap<>();
    // get all news outlet css info
    private static final HashMap<String, NewsOutlet> newsOutlets = GetNewsOutlets.newsOutlets;

    private final String category;
    private boolean finished = false;
    public ArticleListGetter(String category){
        this.category = category;
    }

    @Override
    protected List<Article> call() throws Exception {
        if (articlesByCategories.get(category) != null){
            return articlesByCategories.get(category);
        }

        List<Article> articles = Collections.synchronizedList(new ArrayList<>());
        updateArticleList(articles);
        return articles;
    }

    public void updateArticleList(List<Article> articles) {
        List<ArticleListGenerator> generators = new ArrayList<>();
        for (NewsOutlet newsOutlet : newsOutlets.values()) {
            ArticleListGenerator generator = new ArticleListGenerator(newsOutlet, category, articles);
            generator.start();
            generators.add(generator);
        }

        Executors.newSingleThreadExecutor().execute(
                () -> {
                    while(true){
                        updateProgress(articles.size(), 50);
                        if (finished) break;
                    }
                });


        for (ArticleListGenerator generator: generators){
            try {
                generator.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Collections.sort(articles);
        articlesByCategories.put(category, articles);
        finished = true;
    }


}
