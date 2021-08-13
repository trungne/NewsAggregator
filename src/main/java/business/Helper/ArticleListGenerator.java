package business.Helper;

import business.News.Article;
import business.NewsSources.NewsOutlet;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;

import java.io.IOException;
import java.net.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;

import static business.Helper.ScrapingConfiguration.*;


public class ArticleListGenerator {
    private final NewsOutlet newsOutlet;
    private final String category;
    private int articleSuccessfullyAdded = 0;
    public ArticleListGenerator(NewsOutlet newsOutlet, String category){
        this.newsOutlet = newsOutlet;
        this.category = category;
    }
    public void populateArticleList(List<Article> articleList) {
        Set<URL> articleUrls = newsOutlet.getLinksFromCategory(category);
        extractArticlesFromLinks(articleUrls, articleList);
    }

    private void extractArticlesFromLinks(Set<URL> urls, List<Article> articles) {
        ExecutorService es = Executors.newCachedThreadPool();
        for (URL url : urls) {
            es.execute(() -> {
                if (articleSuccessfullyAdded == 10){
                    es.shutdownNow();
                }

                try {
                    Document articleDoc = Jsoup
                            .connect(url.toString())
                            .timeout(MAX_WAIT_TIME_WHEN_ACCESS_URL)
                            .get();
                    Article article = new Article(url, newsOutlet, category);
                    boolean ok = extractContentFromDocument(articleDoc, newsOutlet, article);
                    if (ok) {
                        articles.add(article);
                        articleSuccessfullyAdded++;
                    }
                } catch (SocketTimeoutException e) {
                    System.out.println("Cannot scrape: " + url);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
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
    }

    private static Article createArticle(URL url, String name) {
        NewsOutlet newsOutlet = GetNewsOutlets.newsOutlets.get(name);
        if (newsOutlet == null) return null;

        Document articleDoc;
        try {
            articleDoc = Jsoup
                    .connect(url.toString())
                    .timeout(MAX_WAIT_TIME_WHEN_ACCESS_URL)
                    .get();
        } catch (IOException ioException) {
            ioException.printStackTrace();
            return null;
        }

        List<String> category = newsOutlet.getCategoryNames(articleDoc);
        Article article = new Article(url, newsOutlet, category);

        boolean isAddedSuccessfully = extractContentFromDocument(articleDoc, newsOutlet, article);

        if (isAddedSuccessfully)
            return article;

        return null;
    }

    private static boolean extractContentFromDocument(Document articleDoc, NewsOutlet newsOutlet, Article article) {
        Element titleTag = newsOutlet.getTitle(articleDoc);
        Element descriptionTag = newsOutlet.getDescription(articleDoc);
        Element mainContentTag = newsOutlet.getMainContent(articleDoc);
        String thumbNail = newsOutlet.getThumbnail(articleDoc);
        List<String> categories = newsOutlet.getCategoryNames(articleDoc);
        LocalDateTime publishedTime = newsOutlet.getPublishedTime(articleDoc);

        // no need to check for thumbnail and datetime because default values will be assigned if they are null
        if (titleTag == null || descriptionTag == null || mainContentTag == null) {
            return false;
        }

        article.setDateTime(publishedTime);

        try {
            article.setTitle(titleTag);
            article.setDescription(descriptionTag);
            article.setMainContent(mainContentTag);
            article.setThumbNailUrl(thumbNail);
            article.addCategory(categories);
        } catch (Exception e) {
            return false;
        }

        return true;
    }
}
