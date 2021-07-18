package News;

import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;

// an interface for presentation layer to access scraped articles
public class ArticleCollection {
    private static ArticleCollection uniqueArticleCollection;
    private static Collection<Article> articles;
    private static HashMap<String, URL> database;
    private ArticleCollection(){

    }

    public static ArticleCollection getInstance(){
        return Objects.requireNonNullElseGet(uniqueArticleCollection, ArticleCollection::new);

    }

    public Collection<Article> getArticles() {
        return articles;
    }

    //
    private void loadArticles(){
        // TODO: From scraping news

        // TODO: From database

    }
}
