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

    public static NewsOutlet initVNExpress(){
        try {
            LinksCrawler VNExpressLinksCrawler = new LinksCrawler(
                    "https://vnexpress.net/",
                    "main-nav",
                    "title-news");
            Scraper VNExpressScraper = new Scraper(
                    new Sanitizer(new VNExpressFilter()),
                    "https://s1.vnecdn.net/vnexpress/restruct/i/v420/logo_default.jpg",
                    "title-detail",
                    "",
                    "breadcrumb",
                    "description",
                    "fck_detail",
                    "fig-picture",
                    "",
                    "datePublished");
            return new NewsOutlet("VNExpress",
                    VNExpressScraper,
                    VNExpressLinksCrawler);
        } catch (IOException e) {
            return null;
        }
    }

    public static NewsOutlet initTuoiTre(){
        try{
            Scraper TuoiTreScraper = new Scraper(
                    new Sanitizer(new TuoiTreFilter()),
                    "https://dangkyxettuyennghe.tuoitre.vn/img/logo-tt.png",
                    "article-title",
                    "",
                    "bread-crumbs",
                    "sapo",
                    "fck",
                    "VCSortableInPreviewMode",
                    "",
                    "article:published_time");
            LinksCrawler TuoiTreLinksCrawler = new LinksCrawler(
                    "https://tuoitre.vn/",
                    "menu-category",
                    "title-news");
            return new NewsOutlet("Tuoi Tre",
                    TuoiTreScraper,
                    TuoiTreLinksCrawler);
        } catch (IOException e){
            return null;
        }
    }

    public static NewsOutlet initThanhNien(){
        try {
            Scraper ThanhNienScraper = new Scraper(
                    new Sanitizer(new ThanhNienFilter()),
                    "https://static.thanhnien.vn/v2/App_Themes/images/logo-tn-2.png",
                    "details__headline",
                    "details__author",
                    "breadcrumbs",
                    "sapo",
                    "pswp-content",
                    "pswp-content__image",
                    "",
                    "article:published_time");
            LinksCrawler ThanhNienLinksCrawler = new LinksCrawler(
                    "https://thanhnien.vn/",
                    "site-header__nav",
                    "story__thumb");
            return new NewsOutlet("Thanh Nien",
                    ThanhNienScraper,
                    ThanhNienLinksCrawler);
        } catch (IOException e){
            return null;
        }
    }

    public static NewsOutlet initZingNews(){
        try {
            Scraper ZingScraper = new Scraper(
                    new Sanitizer(new ZingNewsFilter()),
                    "https://brandcom.vn/wp-content/uploads/2016/02/zingnews-logo.png",
                    "the-article-title",
                    "the-article-author",
                    "the-article-category",
                    "the-article-summary",
                    "the-article-body",
                    "pic",
                    "",
                    "article:published_time");
            LinksCrawler ZingLinksCrawler = new LinksCrawler(
                    "https://zingnews.vn/",
                    "category-menu",
                    "article-title");
            return new NewsOutlet("ZingNews",
                    ZingScraper,
                    ZingLinksCrawler);
        } catch (IOException e){
            return null;
        }
    }

    public static NewsOutlet initNhanDan(){
        try {
            Scraper NhanDanScraper = new Scraper(
                    new Sanitizer(new NhanDanFilter()),
                    "https://brandcom.vn/wp-content/uploads/2016/02/zingnews-logo.png",
                    "box-title-detail",
                    "",
                    "bc-item",
                    "box-des-detail",
                    "box-content-detail",
                    "box-detail-thumb",
                    "box-detail-thumb",
                    "box-date");
            LinksCrawler NhanDanLinksCrawler = new LinksCrawler(
                    "https://nhandan.vn/",
                    "main-menu",
                    "box-title");
            return new NewsOutlet("Nhan Dan",
                    NhanDanScraper,
                    NhanDanLinksCrawler);
        } catch (IOException e){
            return null;
        }
    }
}
