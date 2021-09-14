/*
  RMIT University Vietnam
  Course: INTE2512 Object-Oriented Programming
  Semester: 2021B
  Assessment: Final Project
  Created  date: dd/mm/yyyy
  Author: Student name, Student ID
  Last modified date: dd/mm/yyyy
  Author: Student name, Student ID
  Acknowledgement: Thanks and give credits to the resources that you used in this file
*/

package Business;

import Business.News.Article;
import Business.Scraper.ArticleCrawler.Scraper;
import Business.Scraper.LinksCrawler.LinksCrawler;

import java.net.URL;
import java.util.List;
import java.util.Set;

import static Business.Scraper.Helper.ScrapingUtils.MAX_ARTICLES_DISPLAYED;


public class NewsOutlet {
    public final Scraper SCRAPER;
    public final LinksCrawler LINKS_CRAWLER;

    public NewsOutlet(Scraper scraper, LinksCrawler LINKS_CRAWLER){
        this.SCRAPER = scraper;
        this.LINKS_CRAWLER = LINKS_CRAWLER;
    }

    public void populateArticleList(List<Article> articleList, String category) {
        Set<URL> urls = LINKS_CRAWLER.getArticleLinks(category);
        for (URL url : urls) {
            if (articleList.size() >= MAX_ARTICLES_DISPLAYED){
                break;
            }

            Article a = SCRAPER.getArticle(url);
            if (a != null){
                articleList.add(a);
            }
        }
    }
}