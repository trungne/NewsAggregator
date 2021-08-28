package Application.Model;

import business.News.Article;
import business.Scraper.ScrapingService;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import java.util.ArrayList;

import java.util.List;

import static business.Scraper.Helper.ScrapingUtils.MAX_ARTICLES_DISPLAYED;

// an interface for presentation layer to access scraped articles
public class GetArticleListTask extends Task<List<Article>> {
    private final String category;

    public GetArticleListTask(String category){
        this.category = category;
    }

    @Override
    protected List<Article> call() {
        ObservableList<Article> articles = FXCollections
                .synchronizedObservableList(
                        FXCollections.observableList(
                        new ArrayList<>()));

        // update progress bar
        articles.addListener((ListChangeListener<Article>)
                change -> updateProgress(
                        change.getList().size(),
                        MAX_ARTICLES_DISPLAYED + 20));

        ScrapingService.startScraping(articles, category);
        return articles;
    }
}

