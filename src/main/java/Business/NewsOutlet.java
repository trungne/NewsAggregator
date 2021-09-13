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
    public final Scraper scraper;
    public final LinksCrawler linksCrawler;

    public NewsOutlet(Scraper scraper, LinksCrawler linksCrawler){
        this.scraper = scraper;
        this.linksCrawler = linksCrawler;
    }

    public void populateArticleList(List<Article> articleList, String category) {
        Set<URL> urls = linksCrawler.getArticleLinks(category);
        for (URL url : urls) {
            if (articleList.size() >= MAX_ARTICLES_DISPLAYED){
                break;
            }

            Article a = scraper.getArticle(url);
            if (a != null){
                articleList.add(a);
            }
        }
    }
}