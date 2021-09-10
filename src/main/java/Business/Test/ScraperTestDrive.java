package Business.Test;

import Business.Scraper.Helper.ScrapingUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

public class ScraperTestDrive {
    public static void main(String[] args) throws IOException {
        String url = "https://zingnews.vn/cong-suat-tiem-vaccine-covid-19-tai-ha-noi-cao-nhat-tu-truoc-den-nay-post1261287.html";
        Document doc = Jsoup.connect(url).get();
        System.out.println(doc);
    }
}