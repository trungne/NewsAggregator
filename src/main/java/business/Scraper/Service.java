package business.Scraper;

import business.Scraper.ArticleCrawler.*;

import java.util.ArrayList;
import java.util.List;

public class Service {
    // needs 50 articles
    public static int MAX_ARTICLES_PER_SOURCE;

    public static List<Scraper> initScrapers(){
        List<Scraper> scrapers = createScrapers();
        MAX_ARTICLES_PER_SOURCE = (int) Math.ceil((50 * 1.0)/scrapers.size());
        return scrapers;
    }

    private static List<Scraper> createScrapers(){
        Scraper[] scrapes = new Scraper[]{
                VNExpressScraper.init(),
                ZingNewsScraper.init(),
                ThanhNienScraper.init(),
                TuoiTreScraper.init(),
                NhanDanScraper.init(),
        };
        List<Scraper> validScrapers = new ArrayList<>();
        for (Scraper scraper: scrapes){
            if(scraper != null){
                validScrapers.add(scraper);
            }
        }
        return validScrapers;
    }


}
