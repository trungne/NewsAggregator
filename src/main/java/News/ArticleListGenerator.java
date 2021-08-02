package News;

import Scraper.Scraper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;

public class ArticleListGenerator {
    private static final int MAX_WAIT_TIME = 5000; // ms

    public static List<Article> getArticlesInCategory(NewsOutlet newsOutlet, String category){
        return extractArticlesFromCategory(newsOutlet, category);
    }

    private static List<Article> extractArticlesFromCategory(NewsOutlet newsOutlet, String category){
        ArrayList<Article> articles = new ArrayList<>();
        Collection<URL> articleUrls = newsOutlet.getLinksFromCategory(category);

        if (articleUrls.size() != 0){
            for (URL url: articleUrls){
                Document articleDoc;
                try {
                    articleDoc = Jsoup.connect(url.toString()).timeout(MAX_WAIT_TIME).get();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                    continue;
                }

                Article article = new Article(url, newsOutlet, category);
                boolean ok = addContentToArticle(articleDoc, newsOutlet, article);
                if (ok){
                    articles.add(article);
                }
            }
        }
        return articles;
    }


    public static Article createArticle(URL url, String name){
        NewsOutlet newsOutlet = GetNewsOutlets.newsOutlets.get(name);
        if (newsOutlet == null) return null;

        Document articleDoc;
        try {
            articleDoc = Jsoup.connect(url.toString()).timeout(MAX_WAIT_TIME).get();
        } catch (IOException ioException) {
            ioException.printStackTrace();
            return null;
        }

        String category = newsOutlet.getCategory(articleDoc);
        Article article = new Article(url, newsOutlet, category);
        boolean ok = addContentToArticle(articleDoc, newsOutlet, article);

        if (ok) return article;
        return null;
    }

    public static boolean addContentToArticle(Document articleDoc, NewsOutlet newsOutletInfo, Article article){
        Element titleTag = newsOutletInfo.getTitle(articleDoc);
        Element descriptionTag = newsOutletInfo.getDescription(articleDoc);
        Element mainContentTag = newsOutletInfo.getMainContent(articleDoc);
        Element thumbNail = newsOutletInfo.getThumbnail(articleDoc);
        LocalDateTime publishedTime = newsOutletInfo.getPublishedTime(articleDoc);

        // no need to check for thumbnail and datetime because default values will be assigned if they are null
        if (titleTag == null || descriptionTag == null || mainContentTag == null){
            return false;
        }

        // assign default alt if there is none
        if (!thumbNail.hasAttr("alt"))
            thumbNail.attr("alt", !titleTag.text().isEmpty() ? titleTag.text() : "thumbnail");

        article.setDateTime(publishedTime);
        article.setNewsSource(newsOutletInfo.name);

        try{
            article.setTitle(titleTag);
            article.setDescription(descriptionTag);
            article.setMainContent(mainContentTag);
            article.setThumbNailUrl(thumbNail);
        }
        catch (Exception e){
            // TODO write to err log
            System.out.println(e.toString());
            return false;
        }

        return true;
    }
}
