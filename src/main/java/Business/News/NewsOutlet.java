package Business.News;

import Business.Scraper.ArticleCrawler.Scraper;
import Business.Scraper.Helper.ScrapingUtils;
import Business.Scraper.LinksCrawler.LinksCrawler;
import Business.Scraper.Sanitizer.Sanitizer;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static Business.Scraper.Helper.ScrapingUtils.MAX_ARTICLES_DISPLAYED;


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

            Article a = getArticle(url);
            if (a != null){
                articles.add(a);
            }

        }
    }
    private Article getArticle(URL url){
        Document doc = ScrapingUtils.getDocumentAndDeleteCookies(url.toString());
        if (doc == null){
            return null;
        }

        Element title = scraper.scrapeTitle(doc);
        title = sanitizer.sanitizeTitle(title);

        Element description = scraper.scrapeDescription(doc);
        description = sanitizer.sanitizeDescription(description);

        Element mainContent = scraper.scrapeMainContent(doc);
        mainContent = sanitizer.sanitizeMainContent(mainContent);

        String thumbNail = scraper.scrapeThumbnail(doc);
        if (StringUtils.isEmpty(thumbNail)){
            thumbNail = defaultThumbnail;
        }

        Set<String> categories = scraper.scrapeCategoryNames(doc);
        LocalDateTime publishedTime = scraper.scrapePublishedTime(doc);

        if (title == null || description == null || mainContent == null || publishedTime == null){
            return null;
        }

        return ArticleFactory.createArticle(source,
                url.toString(),
                title,
                description,
                mainContent,
                thumbNail,
                categories, publishedTime);

    }
}


