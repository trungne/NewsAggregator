package business.Helper;

import business.News.Article;
import business.NewsSources.NewsOutlet;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;

import java.io.IOException;
import java.net.*;
import java.time.LocalDateTime;
import java.util.*;

import static business.Helper.ScrapingConfiguration.*;


public class ArticleListGenerator{
    private final NewsOutlet newsOutlet;
    private final String category;

    public ArticleListGenerator(NewsOutlet newsOutlet, String category){
        this.newsOutlet = newsOutlet;
        this.category = category;
    }

    public void populateArticleList(List<Article> articleList) {
        Set<URL> articleUrls = newsOutlet.getLinksFromCategory(category);
        extractArticlesFromLinks(articleUrls, articleList);
    }

    private void extractArticlesFromLinks(Set<URL> urls, List<Article> articles) {
        int articleSuccessfullyAdded = 0;
        for (URL url : urls) {
            if(articleSuccessfullyAdded == MAX_ARTICLES_PER_SOURCE){
                break;
            }

            Document articleDoc;
            try {
                articleDoc = Jsoup
                        .connect(url.toString())
                        .timeout(MAX_WAIT_TIME_WHEN_ACCESS_URL)
                        .get();
            } catch (SocketTimeoutException e) {
                System.out.println("Cannot scrape: " + url);
                continue;
            } catch (IOException ioException) {
                ioException.printStackTrace();
                continue;
            }

            Article article = new Article(url, category);
            boolean addedSuccessfully = extractContentFromDocument(articleDoc, article);

            if (addedSuccessfully) {
                articles.add(article);
                articleSuccessfullyAdded++;
            }
        }
    }

    private boolean extractContentFromDocument(Document articleDoc, Article article) {
        Element titleTag = newsOutlet.getTitle(articleDoc);
        Element descriptionTag = newsOutlet.getDescription(articleDoc);
        Element mainContentTag = newsOutlet.getMainContent(articleDoc);
        String thumbNail = newsOutlet.getThumbnail(articleDoc);
        List<String> categories = newsOutlet.getCategoryNames(articleDoc);
        LocalDateTime publishedTime = newsOutlet.getPublishedTime(articleDoc);

        try {
            article.setContent(titleTag, descriptionTag, mainContentTag,
                                publishedTime, thumbNail, categories);
        } catch (IllegalArgumentException e) {
            return false;
        }

        return true;
    }
}


