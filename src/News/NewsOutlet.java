package News;

import News.Sanitizer.*;
import Scraper.*;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class NewsOutlet implements Serializable {
    public static final String VNExpress = "VNExpress";
    public static final String ZingNews = "ZingNews";
    public static final String ThanhNien = "ThanhNien";
    public static final String TuoiTre = "TuoiTre";
    public static final String NhanDan = "NhanDan";

    public static NewsOutlet[] initializeNewsOutlets(){
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

        NewsOutlet VNExpress = new NewsOutlet("VNExpress","https://vnexpress.net/", "title-news", "title-detail", "description", "fck_detail", "datePublished","fig-picture", VNExpressCategories, new VNExpressSanitizer(), new ScrapeInMetaTag());
        VNExpress.setDefaultThumbNailUrl("https://s1.vnecdn.net/vnexpress/restruct/i/v395/logo_default.jpg");
        VNExpress.setName(NewsOutlet.VNExpress);

        NewsOutlet ZingNews = new NewsOutlet("ZingNews", "https://zingnews.vn/", "article-title", "the-article-title", "the-article-summary", "the-article-body", "article:published_time", "pic", ZingCategories, new ZingNewsSanitizer(), new ScrapeInMetaTag());
        ZingNews.setDefaultThumbNailUrl("https://static-znews.zadn.vn/images/logo-zing-home.svg");
        ZingNews.setName(NewsOutlet.ZingNews);

        // TODO: fix this pls, cant use "lightbox-content" (class of img) to scrape img
        NewsOutlet TuoiTre = new NewsOutlet("TuoiTre","https://tuoitre.vn/", "title-news", "article-title", "sapo", "content fck","article:published_time","VCSortableInPreviewMode",TuoitreCategories, new TuoiTreSanitizer(), new ScrapeInMetaTag());
        TuoiTre.setDefaultThumbNailUrl("https://dangkyxettuyennghe.tuoitre.vn/img/logo-tt.png");
        TuoiTre.setName(NewsOutlet.TuoiTre);

        NewsOutlet ThanhNien = new NewsOutlet("ThanhNien","https://thanhnien.vn/", "story__thumb", "details__headline", "sapo", "details__content", "article:published_time", "pswp-content__image", ThanhNienCategories, new ThanhNienSanitizer(), new ScrapeInMetaTag());
        ThanhNien.setDefaultThumbNailUrl("https://static.thanhnien.vn/v2/App_Themes/images/logo-tn-2.png");
        ThanhNien.setName(NewsOutlet.ThanhNien);

        NewsOutlet NhanDan = new NewsOutlet("NhanDan", "https://nhandan.vn/", "box-title", "box-title-detail", "box-des-detail", "detail-content-body ", "box-date pull-left", "box-detail-thumb", NhanDanCategories, new NhanDanSanitizer(), new ScrapeInBodyTag());
        NhanDan.setDefaultThumbNailUrl("https://www.nhandan-printing.vn/datafiles_D_D/setmulti/nhandan_copy.jpg");
        NhanDan.setName(NewsOutlet.NhanDan);

        // NewsOutlet[]{VNExpress, ZingNews, TuoiTre, ThanhNien, NhanDan};
        return new NewsOutlet[]{VNExpress};

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

    public NewsOutlet(String name,
                      String baseUrl, String titleLinkClass,
                      String titleCssClass, String descriptionCssClass,
                      String contentBodyCssClass, String dateTimeClass,
                      String pictureClass, HashMap<String, String> categories,
                      HtmlSanitizer htmlSanitizer, ScrapingDateTimeBehavior scrapingDateTimeBehavior){
        this.name = name;
        this.baseUrl = baseUrl;
        this.titleLinkCssClass = titleLinkClass;
        this.titleCssClass = titleCssClass;
        this.descriptionCssClass = descriptionCssClass;
        this.contentBodyCssClass = contentBodyCssClass;
        this.dateTimeCssClass = dateTimeClass;
        this.pictureCssClass = pictureClass;
        this.categories = categories;
        this.sanitizer = htmlSanitizer;
        this.scrapingDateTimeBehavior = scrapingDateTimeBehavior;
    }

    public void setDefaultThumbNailUrl(String url){
        this.defaultThumbNailUrl = url;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName(){
        URL url;
        try {
            url = new URL(this.baseUrl);
            String host = url.getHost();
            for (int i = 0; i < host.length(); i++){
                if (host.charAt(i) == '.'){
                    return host.substring(0, i);
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getBaseUrl() {
        return baseUrl;
    }
}
