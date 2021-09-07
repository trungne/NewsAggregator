package Business;

import Business.Scraper.ArticleCrawler.Scraper;
import Business.Scraper.LinksCrawler.LinksCrawler;
import Business.Scraper.Sanitizer.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GetNewsOutlets {

    // Get all Scraping info of 5 news, return a list of NewsOutlet
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

    /** Initialize VNExpress Scraper and VNExpress LinksCrawler
     * @return Scraper and LinksCrawler of VNExpress, stored in NewsOutlet
     * */
    public static NewsOutlet initVNExpress(){
        try {
            LinksCrawler VNExpressLinksCrawler = new LinksCrawler(
                    "https://vnexpress.net/",
                    "main-nav",
                    "title-news");
            Scraper VNExpressScraper = new Scraper("VNExpress",
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
            return new NewsOutlet(VNExpressScraper, VNExpressLinksCrawler);
        } catch (IOException e) {
            return null;
        }
    }

    /** Initialize TuoiTre Scraper and TuoiTre LinksCrawler
     * @return Scraper and LinksCrawler of TuoiTre, stored in NewsOutlet
     * */
    public static NewsOutlet initTuoiTre(){
        try{
            Scraper TuoiTreScraper = new Scraper("Tuoi Tre",
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
            return new NewsOutlet(TuoiTreScraper, TuoiTreLinksCrawler);
        } catch (IOException e){
            return null;
        }
    }

    /** Initialize ThanhNien Scraper and ThanhNien LinksCrawler
     * @return Scraper and LinksCrawler of ThanhNien, stored in NewsOutlet
     * */
    public static NewsOutlet initThanhNien(){
        try {
            Scraper ThanhNienScraper = new Scraper(
                    "Thanh Nien",
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
            return new NewsOutlet(ThanhNienScraper, ThanhNienLinksCrawler);
        } catch (IOException e){
            return null;
        }
    }

    /** Initialize ZingNews Scraper and ZingNews LinksCrawler
     * @return Scraper and LinksCrawler of ZingNews, stored in NewsOutlet
     * */
    public static NewsOutlet initZingNews(){
        try {
            Scraper ZingScraper = new Scraper("ZingNews",
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
            return new NewsOutlet(ZingScraper, ZingLinksCrawler);
        } catch (IOException e){
            return null;
        }
    }

    /** Initialize NhanDan Scraper and NhanDan LinksCrawler
     * @return Scraper and LinksCrawler of NhanDan News, stored in NewsOutlet
     * */
    public static NewsOutlet initNhanDan(){
        try {
            Scraper NhanDanScraper = new Scraper("Nhan Dan",
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
            return new NewsOutlet(NhanDanScraper, NhanDanLinksCrawler);
        } catch (IOException e){
            return null;
        }
    }
}
