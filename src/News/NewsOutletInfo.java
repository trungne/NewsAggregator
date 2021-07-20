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
    public static final String ThanhNien = "ThanhNien";
    public static final String TuoiTre = "TuoiTre";
    public static final String NhanDan = "NhanDan";

    public static NewsOutletInfo[] initializeNewsOutlets(){
        HashMap<String, String> VNExpressCategories = new HashMap<>();
        VNExpressCategories.put("Covid", "https://vnexpress.net/covid-19/tin-tuc");
        VNExpressCategories.put("Politics", "https://vnexpress.net/thoi-su/chinh-tri");
        VNExpressCategories.put("Business", "https://vnexpress.net/kinh-doanh");
        VNExpressCategories.put("Technology", "https://vnexpress.net/khoa-hoc");
        VNExpressCategories.put("Health", "https://vnexpress.net/suc-khoe");
        VNExpressCategories.put("Sports", "https://vnexpress.net/the-thao");
        VNExpressCategories.put("Entertainment","https://vnexpress.net/giai-tri");
        VNExpressCategories.put("World", "https://vnexpress.net/the-gioi");

        HashMap<String, String> ZingCategories = new HashMap<>();
        ZingCategories.put("Covid", "https://zingnews.vn/tieu-diem/covid-19.html");
        ZingCategories.put("Politics", "https://zingnews.vn/chinh-tri.html");
        ZingCategories.put("Business", "https://zingnews.vn/kinh-doanh-tai-chinh.html");
        ZingCategories.put("Technology", "https://zingnews.vn/cong-nghe.html");
        ZingCategories.put("Health", "https://zingnews.vn/suc-khoe.html");
        ZingCategories.put("Sports", "https://zingnews.vn/the-thao.html");
        ZingCategories.put("Entertainment", "https://zingnews.vn/giai-tri.html");
        ZingCategories.put("World", "https://zingnews.vn/the-gioi.html");

        HashMap<String, String> TuoitreCategories = new HashMap<>();
        TuoitreCategories.put("Covid","https://tuoitre.vn/covid-19.html");
        TuoitreCategories.put("Politics","https://tuoitre.vn/thoi-su.htm");
        TuoitreCategories.put("Business","https://tuoitre.vn/kinh-doanh.htm");
        TuoitreCategories.put("Technology","https://tuoitre.vn/khoa-hoc.htm");
        TuoitreCategories.put("Health","https://tuoitre.vn/suc-khoe.htm");
        TuoitreCategories.put("Sports","https://tuoitre.vn/the-thao.htm");
        TuoitreCategories.put("Entertainment","https://tuoitre.vn/giai-tri.htm");
        TuoitreCategories.put("World","https://tuoitre.vn/the-gioi.htm");

        HashMap<String, String> ThanhNienCategories = new HashMap<>();
        ThanhNienCategories.put("Covid", "https://thanhnien.vn/covid-19/");
        ThanhNienCategories.put("Politics", "https://thanhnien.vn/thoi-su/");
        ThanhNienCategories.put("Business", "https://thanhnien.vn/tai-chinh-kinh-doanh/");
        ThanhNienCategories.put("Technology","https://thanhnien.vn/cong-nghe/");
        ThanhNienCategories.put("Health","https://thanhnien.vn/suc-khoe/");
        ThanhNienCategories.put("Sports","https://thanhnien.vn/the-thao/");
        ThanhNienCategories.put("Entertainment","https://thanhnien.vn/giai-tri/");
        ThanhNienCategories.put("World","https://thanhnien.vn/the-gioi/");

        HashMap<String, String> NhanDanCategories = new HashMap<>();
        NhanDanCategories.put("Covid","https://nhandan.vn/tieu-diem");
        NhanDanCategories.put("Politics","https://nhandan.vn/chinhtri");
        NhanDanCategories.put("Business","https://nhandan.vn/kinhte");
        NhanDanCategories.put("Technology","https://nhandan.vn/khoahoc-congnghe");
        NhanDanCategories.put("Health","https://nhandan.vn/y-te");
        NhanDanCategories.put("Sports","https://nhandan.vn/thethao"); // NhanDanCategories.put("Entertainment", new URL("??"));
        NhanDanCategories.put("World","https://nhandan.vn/thegioi");

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
        TuoiTreInfo.setContentBodyCssClass("content fck");
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
