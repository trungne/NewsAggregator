package News;

import News.Content.ContentFactory;
import Scraper.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class NewsOutlet implements Serializable {
    public static void main(String[] args) {
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
        NhanDanCategories.put("Sports","https://nhandan.vn/thethao");
//            NhanDanCategories.put("Entertainment", new URL("??"));
        NhanDanCategories.put("World","https://nhandan.vn/thegioi");


        NewsOutlet VNExpress = new NewsOutlet("https://vnexpress.net/", "title-news", "title-detail", "description", "fck_detail", "datePublished","fig-picture", VNExpressCategories);
        NewsOutlet ZingNews = new NewsOutlet("https://zingnews.vn/", "article-title", "the-article-title", "the-article-summary", "the-article-body", "article:published_time", "pic", ZingCategories);

        // wtf? fix this pls, cant use "lightbox-content" (class of img) to scrape img
        NewsOutlet TuoiTre = new NewsOutlet("https://tuoitre.vn/", "title-news", "article-title", "sapo", "content fck","article:published_time","VCSortableInPreviewMode",TuoitreCategories);

        NewsOutlet ThanhNien = new NewsOutlet("https://thanhnien.vn/", "story__thumb", "details__headline", "sapo", "details__content", "article:published_time", "pswp-content__image", ThanhNienCategories);
        NewsOutlet NhanDan = new NewsOutlet("https://nhandan.vn/", "box-title", "box-title-detail", "box-des-detail", "detail-content-body ", "box-date pull-left", "box-detail-thumb", NhanDanCategories);

        ArrayList<NewsOutlet> newsOutlets = new ArrayList<>();
        newsOutlets.add(VNExpress);
        newsOutlets.add(ZingNews);
        newsOutlets.add(TuoiTre);
        newsOutlets.add(ThanhNien);
        newsOutlets.add(NhanDan);
        for (NewsOutlet newsOutlet: newsOutlets){
            try{
                FileOutputStream fileOut = new FileOutputStream(NewsOutlet.SER_FILE_PATH + newsOutlet.getName() + ".ser");
                ObjectOutputStream out = new ObjectOutputStream(fileOut);
                out.writeObject(newsOutlet);
                out.close();
                fileOut.close();
            }
            catch (IOException i){
                i.printStackTrace();
            }
        }


        Scraper VNExpressScrapper = new Scraper(VNExpress, new ContentFactory(), new RetrieveInMetaTag());
        Scraper ZingNewsScrapper = new Scraper(ZingNews, new ContentFactory(), new RetrieveInMetaTag());
        Scraper TuoiTreScrapper = new Scraper(TuoiTre, new ContentFactory(), new RetrieveInMetaTag());
        Scraper ThanhNienScrapper = new Scraper(ThanhNien, new ContentFactory(), new RetrieveInMetaTag());
        Scraper NhanDanScrapper = new Scraper(NhanDan, new ContentFactory(), new RetrieveInBodyTag());

//        final long startTime = System.currentTimeMillis();
//
//
//
//        HashSet<Article> allArticles = new HashSet<>();
//        allArticles.addAll(ZingNewsScrapper.getArticlesFromCategories());
//        allArticles.addAll(VNExpressScrapper.getArticlesFromCategories());
//        allArticles.addAll(TuoiTreScrapper.getArticlesFromCategories());
//        allArticles.addAll(ThanhNienScrapper.getArticlesFromCategories());
//        allArticles.addAll(NhanDanScrapper.getArticlesFromCategories());
//
//        System.out.println(allArticles.size());
//
//        for (Article article: allArticles){
//            article.displayTitleAndCategory();
//            System.out.println("---- time: " + article.getDateTime());
//        }
//
//        final long endTime = System.currentTimeMillis();
//        System.out.println("Total execution time: " + (endTime - startTime)/1000);

    }

    static final String SER_FILE_PATH = "./src/News/data/";

    public String baseUrl;
    public String titleLinkClass;
    public String titleClass;
    public String descriptionClass;
    public String contentBodyClass;
    public String dateTimeClass;
    public String pictureClass;
    public HashMap<String, String> categories;

    private String name;

    public NewsOutlet(String baseUrl, String titleLinkClass,
                      String titleClass, String descriptionClass,
                      String contentBodyClass, String dateTimeClass,
                      String pictureClass, HashMap<String, String> categories){
        this.baseUrl = baseUrl;
        this.titleLinkClass = titleLinkClass;
        this.titleClass = titleClass;
        this.descriptionClass = descriptionClass;
        this.contentBodyClass = contentBodyClass;
        this.dateTimeClass = dateTimeClass;
        this.pictureClass = pictureClass;
        this.categories = categories;
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
}
