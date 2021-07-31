package News;

import Scraper.Scraper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;

public class ArticleListGenerator {
    private static final int MAX_WAIT_TIME = 5000; // ms

    public static ArrayList<Article> getArticlesInCategory(NewsOutletInfo newsOutletInfo, String category){
        return extractArticlesFromCategory(newsOutletInfo, category);
    }

    private static ArrayList<Article> extractArticlesFromCategory(NewsOutletInfo newsOutletInfo, String category){
        ArrayList<Article> articles = new ArrayList<>();
        ArrayList<URL> articleUrls = extractLinksFromCategory(newsOutletInfo, category);
        if (articleUrls != null){
            for (URL url: articleUrls){
                Article article = new Article(url, newsOutletInfo, category);
                Document articleDoc;
                try {
                    articleDoc = Jsoup.connect(url.toString()).timeout(MAX_WAIT_TIME).get();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                    continue;
                }

                article = createArticle(articleDoc, newsOutletInfo, article);
                if (article != null){
                    articles.add(article);
                }
            }
        }
        return articles;
    }

    private static ArrayList<URL> extractLinksFromCategory(NewsOutletInfo newsOutletInfo, String category){
        if (newsOutletInfo.categories.containsKey(category)){
            URL categoryUrl;
            try {
                categoryUrl = new URL(newsOutletInfo.categories.get(category));
                return new ArrayList<>(Scraper.scrapeLinksByClass(categoryUrl, newsOutletInfo.titleLinkCssClass));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static Article createArticle(URL url, String newsOulet){
        NewsOutletInfo newsOutletInfo = GetNewsOutlets.newsOutlets.get(newsOulet);
        if (newsOutletInfo == null) return null;
        Document articleDoc;
        try {
            articleDoc = Jsoup.connect(url.toString()).timeout(MAX_WAIT_TIME).get();
        } catch (IOException ioException) {
            ioException.printStackTrace();
            return null;
        }

        String category = newsOutletInfo.getCategory(articleDoc);
        // TODO: wtf?
        Article article = new Article(url, newsOutletInfo, category);

        return createArticle(articleDoc, newsOutletInfo, article);
    }

    public static Article createArticle(Document articleDoc, NewsOutletInfo newsOutletInfo, Article article){
        Element titleTag = newsOutletInfo.getTitleTag(articleDoc);
        Element descriptionTag = newsOutletInfo.getDescriptionTag(articleDoc);
        Element mainContentTag = newsOutletInfo.getMainContent(articleDoc);
        Element thumbNail = newsOutletInfo.getThumbnail(articleDoc);
        LocalDateTime publishedTime = newsOutletInfo.getPublishedTime(articleDoc);

        // no need to check for thumbnail and datetime because default values will be assigned if they are null
        if (titleTag == null || descriptionTag == null || mainContentTag == null){
            return null;
        }

        // assign default alt if there is none
        if (!thumbNail.hasAttr("alt"))
            thumbNail.attr("alt", !titleTag.text().isEmpty() ? titleTag.text() : "thumbnail");

        // sanitize all scraped tags and customize them
        titleTag = newsOutletInfo.sanitizer.sanitize(titleTag, CSS.TITLE);
        descriptionTag = newsOutletInfo.sanitizer.sanitize(descriptionTag, CSS.DESCRIPTION);
        mainContentTag = newsOutletInfo.sanitizer.sanitize(mainContentTag, CSS.MAIN_CONTENT);
        thumbNail = newsOutletInfo.sanitizer.sanitize(thumbNail, CSS.THUMBNAIL);

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
            return null;
        }

        return article;
    }
}
