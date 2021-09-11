package Business.Test;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URL;

public class ScraperTestDrive {
    public static void main(String[] args) throws IOException {
        String url = "https://thanhnien.vn/video/phong-su/nguoi-can-tho-duoc-lay-mau-tan-nha-bang-xe-xet-nghiem-covid-19-tu-che-166261v.html";
        URL _url = new URL(url);
        System.out.println(_url.getPath());
        Document doc = Jsoup.connect(url).get();
//        System.out.println(doc);
    }
}