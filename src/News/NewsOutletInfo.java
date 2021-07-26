package News;

import News.Sanitizer.*;
import Scraper.*;

import java.io.*;
import java.util.HashMap;

public class NewsOutletInfo implements Serializable {
    public static final String VNExpress = "VNExpress";
    public static final String ZingNews = "ZingNews";
    public static final String ThanhNien = "Thanh Niên";
    public static final String TuoiTre = "Tuổi Trẻ";
    public static final String NhanDan = "Nhân Dân";

    public static NewsOutletInfo[] initializeNewsOutlets(){
        HashMap<String, String> VNExpressCategories = new HashMap<>();

        VNExpressCategories.put(CATEGORY.COVID, CATEGORY.VNEXPRESS_COVID);
        VNExpressCategories.put(CATEGORY.POLITICS, CATEGORY.VNEXPRESS_POLITICS);
        VNExpressCategories.put(CATEGORY.BUSINESS, CATEGORY.VNEXPRESS_BUSINESS);
        VNExpressCategories.put(CATEGORY.TECHNOLOGY, CATEGORY.VNEXPRESS_TECHNOLOGY);
        VNExpressCategories.put(CATEGORY.HEALTH, CATEGORY.VNEXPRESS_HEALTH);
        VNExpressCategories.put(CATEGORY.SPORTS, CATEGORY.VNEXPRESS_SPORTS);
        VNExpressCategories.put(CATEGORY.ENTERTAINMENT, CATEGORY.VNEXPRESS_ENTERTAINMENT);
        VNExpressCategories.put(CATEGORY.WORLD, CATEGORY.VNEXPRESS_WORLD);

        HashMap<String, String> ZingCategories = new HashMap<>();
        ZingCategories.put(CATEGORY.COVID, CATEGORY.ZING_COVID);
        ZingCategories.put(CATEGORY.POLITICS, CATEGORY.ZING_POLITICS);
        ZingCategories.put(CATEGORY.BUSINESS, CATEGORY.ZING_BUSINESS);
        ZingCategories.put(CATEGORY.TECHNOLOGY, CATEGORY.ZING_TECHNOLOGY);
        ZingCategories.put(CATEGORY.HEALTH, CATEGORY.ZING_HEALTH);
        ZingCategories.put(CATEGORY.SPORTS, CATEGORY.ZING_SPORTS);
        ZingCategories.put(CATEGORY.ENTERTAINMENT, CATEGORY.ZING_ENTERTAINMENT);
        ZingCategories.put(CATEGORY.WORLD, CATEGORY.ZING_WORLD);

        HashMap<String, String> TuoitreCategories = new HashMap<>();
        TuoitreCategories.put(CATEGORY.COVID,CATEGORY.TUOITRE_COVID);
        TuoitreCategories.put(CATEGORY.POLITICS,CATEGORY.TUOITRE_POLITICS);
        TuoitreCategories.put(CATEGORY.BUSINESS,CATEGORY.TUOITRE_BUSINESS);
        TuoitreCategories.put(CATEGORY.TECHNOLOGY,CATEGORY.TUOITRE_TECHNOLOGY);
        TuoitreCategories.put(CATEGORY.HEALTH,CATEGORY.TUOITRE_HEALTH);
        TuoitreCategories.put(CATEGORY.SPORTS,CATEGORY.TUOITRE_SPORTS);
        TuoitreCategories.put(CATEGORY.ENTERTAINMENT,CATEGORY.TUOITRE_ENTERTAINMENT);
        TuoitreCategories.put(CATEGORY.WORLD,CATEGORY.TUOITRE_WORLD);

        HashMap<String, String> ThanhNienCategories = new HashMap<>();
        ThanhNienCategories.put(CATEGORY.COVID,CATEGORY.THANHNIEN_COVID);
        ThanhNienCategories.put(CATEGORY.POLITICS,CATEGORY.THANHNIEN_POLITICS);
        ThanhNienCategories.put(CATEGORY.BUSINESS,CATEGORY.THANHNIEN_BUSINESS);
        ThanhNienCategories.put(CATEGORY.TECHNOLOGY,CATEGORY.THANHNIEN_TECHNOLOGY);
        ThanhNienCategories.put(CATEGORY.HEALTH,CATEGORY.THANHNIEN_HEALTH);
        ThanhNienCategories.put(CATEGORY.SPORTS,CATEGORY.THANHNIEN_SPORTS);
        ThanhNienCategories.put(CATEGORY.ENTERTAINMENT,CATEGORY.THANHNIEN_ENTERTAINMENT);
        ThanhNienCategories.put(CATEGORY.WORLD,CATEGORY.THANHNIEN_WORLD);

        HashMap<String, String> NhanDanCategories = new HashMap<>();
        NhanDanCategories.put(CATEGORY.COVID,CATEGORY.NHANDAN_COVID);
        NhanDanCategories.put(CATEGORY.POLITICS,CATEGORY.NHANDAN_POLITICS);
        NhanDanCategories.put(CATEGORY.BUSINESS,CATEGORY.NHANDAN_BUSINESS);
        NhanDanCategories.put(CATEGORY.TECHNOLOGY,CATEGORY.NHANDAN_TECHNOLOGY);
        NhanDanCategories.put(CATEGORY.HEALTH,CATEGORY.NHANDAN_HEALTH);
        NhanDanCategories.put(CATEGORY.SPORTS,CATEGORY.NHANDAN_SPORTS); // NhanDanCategories.put("Entertainment", new URL("??"));
        NhanDanCategories.put(CATEGORY.ENTERTAINMENT, CATEGORY.NHANDAN_ENTERTAINMENT);
        NhanDanCategories.put(CATEGORY.WORLD,CATEGORY.NHANDAN_WORLD);

        NewsOutletInfo VNExpressInfo = new NewsOutletInfo(NewsOutletInfo.VNExpress);
        VNExpressInfo.setBaseUrl("https://vnexpress.net/");
        VNExpressInfo.setTitleLinkCssClass(CSS.VNEXPRESS_TITLE_LINK);
        VNExpressInfo.setTitleCssClass(CSS.VNEXPRESS_TITLE);
        VNExpressInfo.setDescriptionCssClass(CSS.VNEXPRESS_DESCRIPTION);
        VNExpressInfo.setContentBodyCssClass(CSS.VNEXPRESS_BODY);
        VNExpressInfo.setDateTimeCssClass(CSS.VNEXPRESS_TIME);
        VNExpressInfo.setPictureCssClass(CSS.VNEXPRESS_PIC);
        VNExpressInfo.setDefaultThumbNailUrl("https://s1.vnecdn.net/vnexpress/restruct/i/v395/logo_default.jpg");
        VNExpressInfo.setCategories(VNExpressCategories);
        VNExpressInfo.setSanitizer(new VNExpressSanitizer());
        VNExpressInfo.setScrapingDateTimeBehavior(new ScrapeInMetaTag());

        NewsOutletInfo ZingNewsInfo = new NewsOutletInfo(NewsOutletInfo.ZingNews);
        ZingNewsInfo.setBaseUrl("https://zingnews.vn/");
        ZingNewsInfo.setTitleLinkCssClass(CSS.ZING_TITLE_LINK);
        ZingNewsInfo.setTitleCssClass(CSS.ZING_TITLE);
        ZingNewsInfo.setDescriptionCssClass(CSS.ZING_DESCRIPTION);
        ZingNewsInfo.setContentBodyCssClass(CSS.ZING_BODY);
        ZingNewsInfo.setDateTimeCssClass(CSS.ZING_TIME);
        ZingNewsInfo.setPictureCssClass(CSS.ZING_PIC);
        ZingNewsInfo.setDefaultThumbNailUrl("https://static-znews.zadn.vn/images/logo-zing-home.svg");
        ZingNewsInfo.setCategories(ZingCategories);
        ZingNewsInfo.setSanitizer(new ZingNewsSanitizer());
        ZingNewsInfo.setScrapingDateTimeBehavior(new ScrapeInMetaTag());

        NewsOutletInfo TuoiTreInfo = new NewsOutletInfo(NewsOutletInfo.TuoiTre);
        TuoiTreInfo.setBaseUrl("https://tuoitre.vn/");
        TuoiTreInfo.setTitleLinkCssClass(CSS.TUOITRE_TITLE_LINK);
        TuoiTreInfo.setTitleCssClass(CSS.TUOITRE_TITLE);
        TuoiTreInfo.setDescriptionCssClass(CSS.TUOITRE_DESCRIPTION);
        TuoiTreInfo.setContentBodyCssClass(CSS.TUOITRE_BODY);
        TuoiTreInfo.setDateTimeCssClass(CSS.TUOITRE_TIME);
        TuoiTreInfo.setPictureCssClass(CSS.TUOITRE_PIC);
        TuoiTreInfo.setDefaultThumbNailUrl("https://dangkyxettuyennghe.tuoitre.vn/img/logo-tt.png");
        TuoiTreInfo.setCategories(TuoitreCategories);
        TuoiTreInfo.setSanitizer(new TuoiTreSanitizer());
        TuoiTreInfo.setScrapingDateTimeBehavior(new ScrapeInMetaTag());

        NewsOutletInfo ThanhNienInfo = new NewsOutletInfo(NewsOutletInfo.ThanhNien);
        ThanhNienInfo.setBaseUrl("https://thanhnien.vn/");
        ThanhNienInfo.setTitleLinkCssClass(CSS.THANHNIEN_TITLE_LINK);
        ThanhNienInfo.setTitleCssClass(CSS.THANHNIEN_TITLE);
        ThanhNienInfo.setDescriptionCssClass(CSS.THANHNIEN_DESCRIPTION);
        ThanhNienInfo.setContentBodyCssClass(CSS.THANHNIEN_BODY);
        ThanhNienInfo.setDateTimeCssClass(CSS.THANHNIEN_TIME);
        ThanhNienInfo.setPictureCssClass(CSS.THANHNIEN_PIC);
        ThanhNienInfo.setDefaultThumbNailUrl("https://static.thanhnien.vn/v2/App_Themes/images/logo-tn-2.png");
        ThanhNienInfo.setCategories(ThanhNienCategories);
        ThanhNienInfo.setSanitizer(new ThanhNienSanitizer());
        ThanhNienInfo.setScrapingDateTimeBehavior(new ScrapeInMetaTag());

        NewsOutletInfo NhanDanInfo = new NewsOutletInfo(NewsOutletInfo.NhanDan);
        NhanDanInfo.setBaseUrl("https://nhandan.vn/");
        NhanDanInfo.setTitleLinkCssClass(CSS.NHANDAN_TITLE_LINK);
        NhanDanInfo.setTitleCssClass(CSS.NHANDAN_TITLE);
        NhanDanInfo.setDescriptionCssClass(CSS.NHANDAN_DESCRIPTION);
        NhanDanInfo.setContentBodyCssClass(CSS.NHANDAN_BODY);
        NhanDanInfo.setDateTimeCssClass(CSS.NHANDAN_TIME);
        NhanDanInfo.setPictureCssClass(CSS.NHANDAN_PIC);
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
