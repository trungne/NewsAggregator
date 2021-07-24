package News;

import News.Sanitizer.*;
import Scraper.*;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class NewsOutletInfo implements Serializable {
    public static final String VNExpress = "VNExpress";
    public static final String ZingNews = "ZingNews";
    public static final String ThanhNien = "Thanh Niên";
    public static final String TuoiTre = "Tuổi Trẻ";
    public static final String NhanDan = "Nhân Dân";

    public static NewsOutletInfo[] initializeNewsOutlets(){
        HashMap<String, String> VNExpressCategories = new HashMap<>();
        VNExpressCategories.put(CATEGORY.COVID, "https://vnexpress.net/covid-19/tin-tuc");
        VNExpressCategories.put(CATEGORY.POLITICS, "https://vnexpress.net/thoi-su/chinh-tri");
        VNExpressCategories.put(CATEGORY.BUSINESS, "https://vnexpress.net/kinh-doanh");
        VNExpressCategories.put(CATEGORY.TECHNOLOGY, "https://vnexpress.net/khoa-hoc");
        VNExpressCategories.put(CATEGORY.HEALTH, "https://vnexpress.net/suc-khoe");
        VNExpressCategories.put(CATEGORY.SPORTS, "https://vnexpress.net/the-thao");
        VNExpressCategories.put(CATEGORY.ENTERTAINMENT,"https://vnexpress.net/giai-tri");
        VNExpressCategories.put(CATEGORY.WORLD, "https://vnexpress.net/the-gioi");

        HashMap<String, String> ZingCategories = new HashMap<>();
        ZingCategories.put(CATEGORY.COVID, "https://zingnews.vn/tieu-diem/covid-19.html");
        ZingCategories.put(CATEGORY.POLITICS, "https://zingnews.vn/chinh-tri.html");
        ZingCategories.put(CATEGORY.BUSINESS, "https://zingnews.vn/kinh-doanh-tai-chinh.html");
        ZingCategories.put(CATEGORY.TECHNOLOGY, "https://zingnews.vn/cong-nghe.html");
        ZingCategories.put(CATEGORY.HEALTH, "https://zingnews.vn/suc-khoe.html");
        ZingCategories.put(CATEGORY.SPORTS, "https://zingnews.vn/the-thao.html");
        ZingCategories.put(CATEGORY.ENTERTAINMENT, "https://zingnews.vn/giai-tri.html");
        ZingCategories.put(CATEGORY.WORLD, "https://zingnews.vn/the-gioi.html");

        HashMap<String, String> TuoitreCategories = new HashMap<>();
        TuoitreCategories.put(CATEGORY.COVID,"https://tuoitre.vn/covid-19.html");
        TuoitreCategories.put(CATEGORY.POLITICS,"https://tuoitre.vn/thoi-su.htm");
        TuoitreCategories.put(CATEGORY.BUSINESS,"https://tuoitre.vn/kinh-doanh.htm");
        TuoitreCategories.put(CATEGORY.TECHNOLOGY,"https://tuoitre.vn/khoa-hoc.htm");
        TuoitreCategories.put(CATEGORY.HEALTH,"https://tuoitre.vn/suc-khoe.htm");
        TuoitreCategories.put(CATEGORY.SPORTS,"https://tuoitre.vn/the-thao.htm");
        TuoitreCategories.put(CATEGORY.ENTERTAINMENT,"https://tuoitre.vn/giai-tri.htm");
        TuoitreCategories.put(CATEGORY.WORLD,"https://tuoitre.vn/the-gioi.htm");

        HashMap<String, String> ThanhNienCategories = new HashMap<>();
        ThanhNienCategories.put(CATEGORY.COVID, "https://thanhnien.vn/covid-19/");
        ThanhNienCategories.put(CATEGORY.POLITICS, "https://thanhnien.vn/thoi-su/");
        ThanhNienCategories.put(CATEGORY.BUSINESS, "https://thanhnien.vn/tai-chinh-kinh-doanh/");
        ThanhNienCategories.put(CATEGORY.TECHNOLOGY,"https://thanhnien.vn/cong-nghe/");
        ThanhNienCategories.put(CATEGORY.HEALTH,"https://thanhnien.vn/suc-khoe/");
        ThanhNienCategories.put(CATEGORY.SPORTS,"https://thanhnien.vn/the-thao/");
        ThanhNienCategories.put(CATEGORY.ENTERTAINMENT,"https://thanhnien.vn/giai-tri/");
        ThanhNienCategories.put(CATEGORY.WORLD,"https://thanhnien.vn/the-gioi/");

        HashMap<String, String> NhanDanCategories = new HashMap<>();
        NhanDanCategories.put(CATEGORY.COVID,"https://nhandan.vn/tieu-diem");
        NhanDanCategories.put(CATEGORY.POLITICS,"https://nhandan.vn/chinhtri");
        NhanDanCategories.put(CATEGORY.BUSINESS,"https://nhandan.vn/kinhte");
        NhanDanCategories.put(CATEGORY.TECHNOLOGY,"https://nhandan.vn/khoahoc-congnghe");
        NhanDanCategories.put(CATEGORY.HEALTH,"https://nhandan.vn/y-te");
        NhanDanCategories.put(CATEGORY.SPORTS,"https://nhandan.vn/thethao"); // NhanDanCategories.put("Entertainment", new URL("??"));
        NhanDanCategories.put(CATEGORY.WORLD,"https://nhandan.vn/thegioi");

        NewsOutletInfo VNExpressInfo = new NewsOutletInfo(NewsOutletInfo.VNExpress);
        VNExpressInfo.setBaseUrl("https://vnexpress.net/");
        VNExpressInfo.setTitleLinkCssClass("title-news");
        VNExpressInfo.setTitleCssClass("title-detail");
        VNExpressInfo.setDescriptionCssClass("description");
        VNExpressInfo.setContentBodyCssClass("fck_detail");
        VNExpressInfo.setDateTimeCssClass("datePublished");
        VNExpressInfo.setPictureCssClass("fig-picture");
        VNExpressInfo.setDefaultThumbNailUrl("https://s1.vnecdn.net/vnexpress/restruct/i/v395/logo_default.jpg");
        VNExpressInfo.setCategories(VNExpressCategories);
        VNExpressInfo.setSanitizer(new VNExpressSanitizer());
        VNExpressInfo.setScrapingDateTimeBehavior(new ScrapeInMetaTag());

        NewsOutletInfo ZingNewsInfo = new NewsOutletInfo(NewsOutletInfo.ZingNews);
        ZingNewsInfo.setBaseUrl("https://zingnews.vn/");
        ZingNewsInfo.setTitleLinkCssClass("article-title");
        ZingNewsInfo.setTitleCssClass("the-article-title");
        ZingNewsInfo.setDescriptionCssClass("the-article-summary");
        ZingNewsInfo.setContentBodyCssClass("the-article-body");
        ZingNewsInfo.setDateTimeCssClass("article:published_time");
        ZingNewsInfo.setPictureCssClass("pic");
        ZingNewsInfo.setDefaultThumbNailUrl("https://static-znews.zadn.vn/images/logo-zing-home.svg");
        ZingNewsInfo.setCategories(ZingCategories);
        ZingNewsInfo.setSanitizer(new ZingNewsSanitizer());
        ZingNewsInfo.setScrapingDateTimeBehavior(new ScrapeInMetaTag());

        // TODO: fix this pls, cant use "lightbox-content" (class of img) to scrape img
        NewsOutletInfo TuoiTreInfo = new NewsOutletInfo(NewsOutletInfo.TuoiTre);
        TuoiTreInfo.setBaseUrl("https://tuoitre.vn/");
        TuoiTreInfo.setTitleLinkCssClass("title-news");
        TuoiTreInfo.setTitleCssClass("article-title");
        TuoiTreInfo.setDescriptionCssClass("sapo");
        TuoiTreInfo.setContentBodyCssClass("fck");
        TuoiTreInfo.setDateTimeCssClass("article:published_time");
        TuoiTreInfo.setPictureCssClass("VCSortableInPreviewMode");
        TuoiTreInfo.setDefaultThumbNailUrl("https://dangkyxettuyennghe.tuoitre.vn/img/logo-tt.png");
        TuoiTreInfo.setCategories(TuoitreCategories);
        TuoiTreInfo.setSanitizer(new TuoiTreSanitizer());
        TuoiTreInfo.setScrapingDateTimeBehavior(new ScrapeInMetaTag());

        NewsOutletInfo ThanhNienInfo = new NewsOutletInfo(NewsOutletInfo.ThanhNien);
        ThanhNienInfo.setBaseUrl("https://thanhnien.vn/");
        ThanhNienInfo.setTitleLinkCssClass("story__thumb");
        ThanhNienInfo.setTitleCssClass("details__headline");
        ThanhNienInfo.setDescriptionCssClass("sapo");
        ThanhNienInfo.setContentBodyCssClass("details__content");
        ThanhNienInfo.setDateTimeCssClass("article:published_time");
        ThanhNienInfo.setPictureCssClass("pswp-content__image");
        ThanhNienInfo.setDefaultThumbNailUrl("https://static.thanhnien.vn/v2/App_Themes/images/logo-tn-2.png");
        ThanhNienInfo.setCategories(ThanhNienCategories);
        ThanhNienInfo.setSanitizer(new ThanhNienSanitizer());
        ThanhNienInfo.setScrapingDateTimeBehavior(new ScrapeInMetaTag());

        NewsOutletInfo NhanDanInfo = new NewsOutletInfo(NewsOutletInfo.NhanDan);
        NhanDanInfo.setBaseUrl("https://nhandan.vn/");
        NhanDanInfo.setTitleLinkCssClass("box-title");
        NhanDanInfo.setTitleCssClass("box-title-detail");
        NhanDanInfo.setDescriptionCssClass("box-des-detail");
        NhanDanInfo.setContentBodyCssClass("detail-content-body");
        NhanDanInfo.setDateTimeCssClass("box-date pull-left");
        NhanDanInfo.setPictureCssClass("box-detail-thumb");
        NhanDanInfo.setDefaultThumbNailUrl("https://www.nhandan-printing.vn/datafiles_D_D/setmulti/nhandan_copy.jpg");
        NhanDanInfo.setCategories(NhanDanCategories);
        NhanDanInfo.setSanitizer(new NhanDanSanitizer());
        NhanDanInfo.setScrapingDateTimeBehavior(new ScrapeInBodyTag());

        return new NewsOutletInfo[]{VNExpressInfo, ZingNewsInfo, TuoiTreInfo, ThanhNienInfo, NhanDanInfo};
    }

    public String name;
    public String baseUrl;
    public String titleLinkCssClass;
    public String titleCssClass;
    public String descriptionCssClass;
    public String contentBodyCssClass;
    public String dateTimeCssClass;
    public String pictureCssClass;
    public String defaultThumbNailUrl;
    public HashMap<String, String> categories;

    // behaviors
    public HtmlSanitizer sanitizer;
    public ScrapingDateTimeBehavior scrapingDateTimeBehavior;

    public NewsOutletInfo(String name){
        this.name = name;
    }

    // setters
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void setCategories(HashMap<String, String> categories) {
        this.categories = categories;
    }

    public void setTitleLinkCssClass(String titleLinkCssClass) {
        this.titleLinkCssClass = titleLinkCssClass;
    }

    public void setDescriptionCssClass(String descriptionCssClass) {
        this.descriptionCssClass = descriptionCssClass;
    }

    public void setContentBodyCssClass(String contentBodyCssClass) {
        this.contentBodyCssClass = contentBodyCssClass;
    }

    public void setPictureCssClass(String pictureCssClass) {
        this.pictureCssClass = pictureCssClass;
    }

    public void setDateTimeCssClass(String dateTimeCssClass) {
        this.dateTimeCssClass = dateTimeCssClass;
    }

    public void setSanitizer(HtmlSanitizer sanitizer) {
        this.sanitizer = sanitizer;
    }

    public void setScrapingDateTimeBehavior(ScrapingDateTimeBehavior scrapingDateTimeBehavior) {
        this.scrapingDateTimeBehavior = scrapingDateTimeBehavior;
    }

    public void setTitleCssClass(String titleCssClass) {
        this.titleCssClass = titleCssClass;
    }

    public void setDefaultThumbNailUrl(String url){
        this.defaultThumbNailUrl = url;
    }

}
