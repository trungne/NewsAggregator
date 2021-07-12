package Scraper;

import News.Article;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.HashSet;

public class ScraperTestDrive {
    public static void main(String[] args) {

        final long startTime = System.currentTimeMillis();

        HashMap<String, URL> VNExpressCategories = new HashMap<>();
        try {
            VNExpressCategories.put("Covid", new URL("https://vnexpress.net/covid-19/tin-tuc"));
            VNExpressCategories.put("Politics", new URL("https://vnexpress.net/thoi-su/chinh-tri"));
            VNExpressCategories.put("Business", new URL("https://vnexpress.net/kinh-doanh"));
            VNExpressCategories.put("Technology", new URL("https://vnexpress.net/khoa-hoc"));
            VNExpressCategories.put("Health", new URL("https://vnexpress.net/suc-khoe"));
            VNExpressCategories.put("Sports", new URL("https://vnexpress.net/the-thao"));
            VNExpressCategories.put("Entertainment", new URL("https://vnexpress.net/giai-tri"));
            VNExpressCategories.put("World", new URL("https://vnexpress.net/the-gioi"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        HashMap<String, URL> ZingCategories = new HashMap<>();
        try {
            ZingCategories.put("Covid", new URL("https://zingnews.vn/tieu-diem/covid-19.html"));
            ZingCategories.put("Politics", new URL("https://zingnews.vn/chinh-tri.html"));
            ZingCategories.put("Business", new URL("https://zingnews.vn/kinh-doanh-tai-chinh.html"));
            ZingCategories.put("Technology", new URL("https://zingnews.vn/cong-nghe.html"));
            ZingCategories.put("Health", new URL("https://zingnews.vn/suc-khoe.html"));
            ZingCategories.put("Sports", new URL("https://zingnews.vn/the-thao.html"));
            ZingCategories.put("Entertainment", new URL("https://zingnews.vn/giai-tri.html"));
            ZingCategories.put("World", new URL("https://zingnews.vn/the-gioi.html"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        HashMap<String, URL> TuoitreCategories = new HashMap<>();
        try{
            TuoitreCategories.put("Covid", new URL("https://tuoitre.vn/covid-19.html"));
            TuoitreCategories.put("Politics", new URL("https://tuoitre.vn/thoi-su.htm"));
            TuoitreCategories.put("Business", new URL("https://tuoitre.vn/kinh-doanh.htm"));
            TuoitreCategories.put("Technology", new URL("https://tuoitre.vn/khoa-hoc.htm"));
            TuoitreCategories.put("Health", new URL("https://tuoitre.vn/suc-khoe.htm"));
            TuoitreCategories.put("Sports", new URL("https://tuoitre.vn/the-thao.htm"));
            TuoitreCategories.put("Entertainment", new URL("https://tuoitre.vn/giai-tri.htm"));
            TuoitreCategories.put("World", new URL("https://tuoitre.vn/the-gioi.htm"));
        } catch (MalformedURLException e){
            e.printStackTrace();
        }

        HashMap<String, URL> ThanhNienCategories = new HashMap<>();
        try{
            ThanhNienCategories.put("Covid", new URL("https://thanhnien.vn/covid-19/"));
            ThanhNienCategories.put("Politics", new URL("https://thanhnien.vn/thoi-su/"));
            ThanhNienCategories.put("Business", new URL("https://thanhnien.vn/tai-chinh-kinh-doanh/"));
            ThanhNienCategories.put("Technology", new URL("https://thanhnien.vn/cong-nghe/"));
            ThanhNienCategories.put("Health", new URL("https://thanhnien.vn/suc-khoe/"));
            ThanhNienCategories.put("Sports", new URL("https://thanhnien.vn/the-thao/"));
            ThanhNienCategories.put("Entertainment", new URL("https://thanhnien.vn/giai-tri/"));
            ThanhNienCategories.put("World", new URL("https://thanhnien.vn/the-gioi/"));
        } catch (MalformedURLException e){
            e.printStackTrace();
        }

        HashMap<String, URL> NhanDanCategories = new HashMap<>();
        try{
            NhanDanCategories.put("Covid", new URL("https://nhandan.vn/tieu-diem"));
            NhanDanCategories.put("Politics", new URL("https://nhandan.vn/chinhtri"));
            NhanDanCategories.put("Business", new URL("https://nhandan.vn/kinhte"));
            NhanDanCategories.put("Technology", new URL("https://nhandan.vn/khoahoc-congnghe\n"));
            NhanDanCategories.put("Health", new URL("https://nhandan.vn/y-te"));
            NhanDanCategories.put("Sports", new URL("https://nhandan.vn/thethao"));
//            NhanDanCategories.put("Entertainment", new URL("??"));
            NhanDanCategories.put("World", new URL("https://nhandan.vn/thegioi"));
        } catch (MalformedURLException e){
            e.printStackTrace();
        }


        Scraper VNExpressScraper = new Scraper("https://vnexpress.net/", "title-news",
                "title-detail", "description", "fck_detail",
                "datePublished","fig-picture", VNExpressCategories, new RetrieveInMetaTag());

        Scraper ZingScrapper = new Scraper("https://zingnews.vn/",
                "article-title",
                "the-article-title",
                "the-article-summary",
                "the-article-body",
                "article:published_time",
                "pic",
                ZingCategories,
                new RetrieveInMetaTag());

        Scraper TuoitreScrapper = new Scraper("https://tuoitre.vn/",
                "title-news",
                "article-title",
                "sapo",
                "content fck",
                "article:published_time",
                "VCSortableInPreviewMode", // wtf? fix this pls, cant use "lightbox-content" (class of img) to scrape img
                TuoitreCategories,
                new RetrieveInMetaTag());

        Scraper ThanhNienScrapper = new Scraper("https://thanhnien.vn/",
                "story__thumb",
                "details__headline",
                "sapo",
                "details__content",
                "article:published_time",
                "pswp-content__image",
                ThanhNienCategories,
                new RetrieveInMetaTag());

        Scraper NhanDanScraper = new Scraper("https://nhandan.vn/",
                "box-title",
                "box-title-detail",
                "box-des-detail",
                "detail-content-body ",
                "box-date pull-left",
                "box-detail-thumb",
                NhanDanCategories,
                new RetrieveInBodyTag());

        HashSet<Article> allArticles = new HashSet<>();
        allArticles.addAll(ZingScrapper.getArticlesFromCategories());
        allArticles.addAll(VNExpressScraper.getArticlesFromCategories());
        allArticles.addAll(TuoitreScrapper.getArticlesFromCategories());
        allArticles.addAll(ThanhNienScrapper.getArticlesFromCategories());
        allArticles.addAll(NhanDanScraper.getArticlesFromCategories());

        System.out.println(allArticles.size());

        for (Article article: allArticles){
            article.displayTitleAndCategory();
            System.out.println("---- time: " + article.getDateTime());
        }

        final long endTime = System.currentTimeMillis();
        System.out.println("Total execution time: " + (endTime - startTime)/1000);

    }
}