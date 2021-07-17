package News;

import java.util.Collection;
import java.util.Objects;

// an interface for presentation layer to access scraped articles
public class ArticleCollection {
    private static ArticleCollection uniqueArticleCollection;
    private static Collection<Article> articles;

    private ArticleCollection(){

    }

    public static ArticleCollection getInstance(){
        return Objects.requireNonNullElseGet(uniqueArticleCollection, ArticleCollection::new);

    }

    public Collection<Article> getArticles() {
        return articles;
    }

    public void setArticles(Collection<Article> articles) {
        ArticleCollection.articles = articles;
    }

    // TODO:
    private void loadArticles(){

    }
}
