package Business.News;

import Business.Scraper.ArticleCrawler.Scraper;
import Business.Scraper.Helper.ScrapingUtils;
import Business.Scraper.LinksCrawler.LinksCrawler;
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
    private final Scraper scraper;
    private final LinksCrawler linksCrawler;

    public NewsOutlet(Scraper scraper, LinksCrawler linksCrawler){
        this.scraper = scraper;
        this.linksCrawler = linksCrawler;
    }

    public void populateArticleList(List<Article> articleList, String category) {
        Set<URL> urls = linksCrawler.getArticleLinks(category);
        for (URL url : urls) {
            if (articleList.size() >= MAX_ARTICLES_DISPLAYED + 20) {
                break;
            }

            Article a = scraper.getArticle(url.toString());
            if (a != null){
                articleList.add(a);
            }
        }
    }
}