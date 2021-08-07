package business.Helper;

import business.News.Article;
import business.NewsSources.NewsOutlet;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static business.Helper.ScrapingConfiguration.MAX_TERMINATION_TIME;
import static business.Helper.ScrapingConfiguration.MAX_WAIT_TIME_WHEN_ACCESS_URL;

public class ArticleListGenerator {
    public static List<Article> getArticlesInCategory(NewsOutlet newsOutlet, String category) {
        Set<URL> articleUrls = newsOutlet.getLinksFromCategory(category);
        return extractArticlesFromLinks(newsOutlet, category, articleUrls);
    }

    private static List<Article> extractArticlesFromLinks(NewsOutlet newsOutlet, String category, Collection<URL> urls) {
        List<Article> articles = Collections.synchronizedList(new ArrayList<>());
        // TODO: MULTI threading here
        ExecutorService es = Executors.newCachedThreadPool();
        for (URL url : urls) {
            es.execute(() -> {
                try {
                    Document articleDoc = Jsoup
                            .connect(url.toString())
                            .timeout(MAX_WAIT_TIME_WHEN_ACCESS_URL)
                            .get();
                    Article article = new Article(url, newsOutlet, category);
                    boolean ok = extractContentFromDocument(articleDoc, newsOutlet, article);
                    if (ok) {
                        articles.add(article);
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

        return articles;
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
        Element thumbNail = newsOutlet.getThumbnail(articleDoc);
        List<String> categories = newsOutlet.getCategoryNames(articleDoc);
        LocalDateTime publishedTime = newsOutlet.getPublishedTime(articleDoc);

        // no need to check for thumbnail and datetime because default values will be assigned if they are null
        if (titleTag == null || descriptionTag == null || mainContentTag == null) {
            return false;
        }

        // assign default alt if there is none
        if (!thumbNail.hasAttr("alt"))
            thumbNail.attr("alt", StringUtils.isEmpty(titleTag.text()) ? "thumbnail" : titleTag.text());

        article.setDateTime(publishedTime);
        article.setNewsSource(newsOutlet.getName());

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
