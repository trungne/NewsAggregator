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

            Article article = new Article(url, newsOutlet, category);
            boolean addedSuccessfully = extractContentFromDocument(articleDoc, article);
            if (addedSuccessfully) {
                articles.add(article);
                // TODO: update progress bar
                articleSuccessfullyAdded++;
            }
        }
    }
//    private Article createArticle(URL url, String name) {
//        NewsOutlet newsOutlet = GetNewsOutlets.newsOutlets.get(name);
//        if (newsOutlet == null) return null;
//
//        Document articleDoc;
//        try {
//            articleDoc = Jsoup
//                    .connect(url.toString())
//                    .timeout(MAX_WAIT_TIME_WHEN_ACCESS_URL)
//                    .get();
//        } catch (IOException ioException) {
//            ioException.printStackTrace();
//            return null;
//        }
//
//        List<String> category = newsOutlet.getCategoryNames(articleDoc);
//        Article article = new Article(url, newsOutlet, category);
//
//        boolean isAddedSuccessfully = extractContentFromDocument(articleDoc, article);
//
//        if (isAddedSuccessfully)
//            return article;
//
//        return null;
//    }

    private boolean extractContentFromDocument(Document articleDoc, Article article) {
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
