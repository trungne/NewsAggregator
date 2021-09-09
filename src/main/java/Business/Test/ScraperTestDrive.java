package Business.Test;

import Business.Scraper.Helper.ScrapingUtils;
import org.jsoup.nodes.Document;

public class ScraperTestDrive {
    public static void main(String[] args) {
        Document doc = ScrapingUtils.getDocumentAndDeleteCookies("https://thanhnien.vn/the-thao/bong-da-quoc-te/mu-giup-cdv-tranh-nguy-co-bi-cam-cua-o-tran-ronaldo-ra-mat-139920t.html");
        System.out.println(doc);

    }
}