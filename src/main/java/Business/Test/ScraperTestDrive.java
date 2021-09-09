package Business.Test;

import Business.Scraper.Helper.ScrapingUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

public class ScraperTestDrive {
    public static void main(String[] args) throws IOException {
        String url = "https://nhandan.vn/culture_photo/ben-nom-dep-ngo-ngang-mua-nuoc-can-663912/";
        Document doc = Jsoup.connect(url).get();
        System.out.println(doc);
    }
}