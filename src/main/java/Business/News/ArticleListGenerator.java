package Business.News;

import Business.Scraper.ArticleCrawler.ElementNotFound;
import Business.Scraper.ArticleCrawler.Scraper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static Business.Scraper.Helper.ScrapingUtils.MAX_ARTICLES_DISPLAYED;
import static Business.Scraper.Helper.ScrapingUtils.MAX_WAIT_TIME_WHEN_ACCESS_URL;


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
        for (URL url : urls) {
            if (articles.size() >= MAX_ARTICLES_DISPLAYED + 20) {
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

            Article article = new Article(scraper.getName(), url, category);
            boolean addedSuccessfully = extractContentFromDocument(articleDoc, article);

            if (addedSuccessfully) {
                articles.add(article);
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


