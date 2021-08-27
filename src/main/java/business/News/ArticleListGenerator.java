package business.News;

import business.Scraper.ArticleCrawler.ElementNotFound;
import business.Scraper.ArticleCrawler.Scraper;
import business.Scraper.Service;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static business.Scraper.Helper.ScrapingUtils.MAX_WAIT_TIME_WHEN_ACCESS_URL;


public class ArticleListGenerator {
    // TODO: Khang comments this
    private final Scraper scraper;
    private final String category;

    public ArticleListGenerator(Scraper scraper, String category) {
        this.scraper = scraper;
        this.category = category;
    }

    public void populateArticleList(List<Article> articleList) {
        Set<URL> articleUrls = scraper.getLinksFromCategory(category);
        extractArticlesFromLinks(articleUrls, articleList);
    }

    private void extractArticlesFromLinks(Set<URL> urls, List<Article> articles) {
        int articleSuccessfullyAdded = 0;
        for (URL url : urls) {
            if (articleSuccessfullyAdded == Service.MAX_ARTICLES_PER_SOURCE) {
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
            Element titleTag = scraper.scrapeTitle(articleDoc);
            Element descriptionTag = scraper.scrapeDescription(articleDoc);
            Element mainContentTag = scraper.scrapeMainContent(articleDoc);
            String thumbNail = scraper.scrapeThumbnail(articleDoc);
            Set<String> categories = scraper.scrapeCategoryNames(articleDoc);
            LocalDateTime publishedTime = scraper.scrapePublishedTime(articleDoc);
            article.setContent(titleTag, descriptionTag, mainContentTag,
                    publishedTime, thumbNail, categories);
        }
        catch (ElementNotFound | IllegalArgumentException e){
            return false;
        }

        return true;
    }
}


