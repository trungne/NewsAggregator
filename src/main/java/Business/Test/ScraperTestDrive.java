package Business.Test;

import Business.Scraper.Helper.ScrapingUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class ScraperTestDrive {
    public static void main(String[] args) {
        Element p1 = new Element("p").addClass("p1");
        Element p2 = new Element("p").addClass("p2");

        p1.appendChild(p2);
        Element e = new Element("div");
        e.appendChild(p1);
        System.out.println(e);
        for (Element p : e.getElementsByTag("p")){
            System.out.println(p);
        }

    }
}