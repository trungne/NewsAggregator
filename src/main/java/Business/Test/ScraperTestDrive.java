package Business.Test;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class ScraperTestDrive {
    public static void main(String[] args) throws IOException {
        String url = "https://tuoitre.vn/thai-trinh-nho-sai-gon-ket-xe-trieu-cuong-voi-saigon-lullaby-20210909210719816.htm";
        Document doc = Jsoup.connect(url).get();
        System.out.println(doc);
    }
}