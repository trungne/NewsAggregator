package Business.News;

import Business.Scraper.ArticleCrawler.ElementNotFound;
import Business.Scraper.ArticleCrawler.Scraper;
import Business.Scraper.LinksCrawler.LinksCrawler;
import Business.Scraper.Sanitizer.Sanitizer;
import org.apache.commons.lang3.StringUtils;
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


public class NewsOutlet {
    // TODO: Khang comments this
    private final String source;
    private final Scraper scraper;
    private final Sanitizer sanitizer;
    private final LinksCrawler linksCrawler;
    private final String defaultThumbnail;

    public NewsOutlet(String source,
                      Scraper scraper,
                      Sanitizer sanitizer,
                      LinksCrawler linksCrawler,
                      String defaultThumbnail){
        this.source = source;
        this.scraper = scraper;
        this.sanitizer = sanitizer;
        this.linksCrawler = linksCrawler;
        this.defaultThumbnail = defaultThumbnail;
    }

    public void populateArticleList(List<Article> articleList, String category) {
        Set<URL> articleUrls = linksCrawler.getArticleLinks(category);
        extractArticlesFromLinks(articleUrls, articleList, category);
    }

    private void extractArticlesFromLinks(Set<URL> urls, List<Article> articles, String category) {
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

            Article article = new Article(source, url, category);
            boolean addedSuccessfully = extractContentFromDocument(articleDoc, article);

            if (addedSuccessfully) {
                articles.add(article);
            }
        }
    }

    private boolean extractContentFromDocument(Document articleDoc, Article article) {
        try {
            Element titleTag = scraper.scrapeTitle(articleDoc);
            titleTag = sanitizer.sanitizeTitle(titleTag);

            Element descriptionTag = scraper.scrapeDescription(articleDoc);
            descriptionTag = sanitizer.sanitizeDescription(descriptionTag);

            Element mainContentTag = scraper.scrapeMainContent(articleDoc);
            mainContentTag = sanitizer.sanitizeMainContent(mainContentTag);

            String thumbNail = scraper.scrapeThumbnail(articleDoc);
            if (StringUtils.isEmpty(thumbNail)){
                thumbNail = defaultThumbnail;
            }

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


