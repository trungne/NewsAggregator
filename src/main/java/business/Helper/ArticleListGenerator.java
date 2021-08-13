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


public class ArticleListGenerator extends Thread {
    private final NewsOutlet newsOutlet;
    private final String category;
    private List<Article> articleList = null;
    private int articleSuccessfullyAdded = 0;
    public ArticleListGenerator(NewsOutlet newsOutlet, String category, List<Article> articleList){
        this.newsOutlet = newsOutlet;
        this.category = category;
        this.articleList = articleList;
    }

    @Override
    public void run(){
        populateArticleList();
    }
    public void populateArticleList() {
        Set<URL> articleUrls = newsOutlet.getLinksFromCategory(category);
        extractArticlesFromLinks(articleUrls, articleList);
    }

    private void extractArticlesFromLinks(Set<URL> urls, List<Article> articles) {
        for (URL url : urls) {
            if(articleSuccessfullyAdded == 10){
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

            Article article = new Article(url, newsOutlet, category);
            boolean addedSuccessfully = extractContentFromDocument(articleDoc, newsOutlet, article);
            if (addedSuccessfully) {
                articles.add(article);
                articleSuccessfullyAdded++;
            }
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
