package business.Scraper.LinksCrawler;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class WebDriverGenerator implements DocGenerator{
    // TODO: Trung comments this
    @Override
    public Document getDocument(String url) {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless", "--disable-gpu", "--window-size=1920,1080", "--ignore-certificate-errors");
        WebDriver driver = new ChromeDriver(options);
        driver.get("https://vnexpress.net/");
        String source = driver.getPageSource();
        driver.quit();
        return Jsoup.parse(driver.getPageSource());
    }
}
