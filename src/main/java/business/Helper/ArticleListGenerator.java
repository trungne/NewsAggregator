package business.Helper;

import business.News.Article;
import business.Scraper.ElementNotFound;
import business.Scraper.NewsOutlet;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static business.Helper.ScrapingConfiguration.MAX_ARTICLES_PER_SOURCE;
import static business.Helper.ScrapingConfiguration.MAX_WAIT_TIME_WHEN_ACCESS_URL;


public class ArticleListGenerator {
    private final NewsOutlet newsOutlet;
    private final String category;

    public ArticleListGenerator(NewsOutlet newsOutlet, String category) {
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
            if (articleSuccessfullyAdded == MAX_ARTICLES_PER_SOURCE) {
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
        try {
            Element titleTag = newsOutlet.scrapeTitle(articleDoc);
            Element descriptionTag = newsOutlet.scrapeDescription(articleDoc);
            Element mainContentTag = newsOutlet.scrapeMainContent(articleDoc);
            String thumbNail = newsOutlet.scrapeThumbnail(articleDoc);
            Set<String> categories = newsOutlet.scrapeCategoryNames(articleDoc);
            LocalDateTime publishedTime = newsOutlet.scrapePublishedTime(articleDoc);
            article.setContent(titleTag, descriptionTag, mainContentTag,
                    publishedTime, thumbNail, categories);
        }
        catch (ElementNotFound | IllegalArgumentException e){
            return false;
        }

        return true;
    }
}


