package Business.Scraper;

import Business.News.NewsOutlet;
import Business.Scraper.ArticleCrawler.Scraper;
import Business.Scraper.LinksCrawler.LinksCrawler;
import Business.Scraper.Sanitizer.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GetNewsOutlets {
    public static List<NewsOutlet> createNewsOutlets(){
        NewsOutlet[] newsOutlets = new NewsOutlet[] {
                initVNExpress(),
                initTuoiTre(),
                initThanhNien(),
                initNhanDan(),
                initZingNews()
        };
        List<NewsOutlet> validNewsOutlets = new ArrayList<>();
        for (NewsOutlet newsOutlet: newsOutlets){
            if(newsOutlet != null){
                validNewsOutlets.add(newsOutlet);
            }
        }
        return validNewsOutlets;
    }

    private static NewsOutlet initVNExpress(){
        try {
            LinksCrawler VNExpressLinksCrawler = new LinksCrawler("https://vnexpress.net/", "main-nav", "title-news");
            Scraper VNExpressScraper = new Scraper("title-detail", "", "breadcrumb", "description",
                    "fck_detail", "fig-picture", "", "datePublished");
            return new NewsOutlet("VNExpress", VNExpressScraper,
                    new Sanitizer(new VNExpressFilter()), VNExpressLinksCrawler,
                    "https://s1.vnecdn.net/vnexpress/restruct/i/v420/logo_default.jpg");
        } catch (IOException e) {
            return null;
        }
    }

    private static NewsOutlet initTuoiTre(){
        try{
            Scraper TuoiTreScraper = new Scraper("article-title", "", "bread-crumbs", "sapo",
                    "fck", "VCSortableInPreviewMode", "", "article:published_time");
            LinksCrawler TuoiTreLinksCrawler = new LinksCrawler("https://tuoitre.vn/", "menu-category", "title-news");
            return new NewsOutlet("Tuoi Tre", TuoiTreScraper,
                    new Sanitizer(new TuoiTreFilter()), TuoiTreLinksCrawler,
                    "https://dangkyxettuyennghe.tuoitre.vn/img/logo-tt.png");
        } catch (IOException e){
            return null;
        }
    }

    private static NewsOutlet initThanhNien(){
        try {
            Scraper ThanhNienScraper = new Scraper("details__headline", "details__author", "breadcrumbs", "sapo",
                    "pswp-content", "pswp-content__image", "", "article:published_time");
            LinksCrawler ThanhNienLinksCrawler = new LinksCrawler("https://thanhnien.vn/", "site-header__nav", "story__thumb");
            return new NewsOutlet("Thanh Nien", ThanhNienScraper,
                    new Sanitizer(new ThanhNienFilter()), ThanhNienLinksCrawler,
                    "https://static.thanhnien.vn/v2/App_Themes/images/logo-tn-2.png");
        } catch (IOException e){
            return null;
        }
    }

    private static NewsOutlet initZingNews(){
        try {
            Scraper ZingScraper = new Scraper("the-article-title", "the-article-author", "the-article-category", "the-article-summary",
                    "the-article-body", "pic", "", "article:published_time");
            LinksCrawler ZingLinksCrawler = new LinksCrawler("https://zingnews.vn/", "category-menu", "article-title");
            return new NewsOutlet("ZingNews", ZingScraper,
                    new Sanitizer(new ZingNewsFilter()), ZingLinksCrawler,
                    "https://brandcom.vn/wp-content/uploads/2016/02/zingnews-logo.png");
        } catch (IOException e){
            return null;
        }
    }

    private static NewsOutlet initNhanDan(){
        try {
            Scraper NhanDanScraper = new Scraper("box-title-detail", "", "bc-item", "box-des-detail",
                    "box-content-detail", "box-detail-thumb", "box-detail-thumb", "box-date");
            LinksCrawler NhanDanLinksCrawler = new LinksCrawler("https://nhandan.vn/", "main-menu", "box-title");
            return new NewsOutlet("Nhan Dan", NhanDanScraper,
                    new Sanitizer(new NhanDanFilter()), NhanDanLinksCrawler,
                    "https://brandcom.vn/wp-content/uploads/2016/02/zingnews-logo.png");
        } catch (IOException e){
            return null;
        }
    }


}
