package News;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchSessionException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;


public class CovidInfo {
    static String source = "https://vnexpress.net/covid-19/covid-19-viet-nam";
    static String stableChromeVersion = "92.0.4515.43";
    static WebDriver driver;

    // a boolean to keep track of if the data has been loaded
    static boolean updated = false;
    // stats
    static CovidCategory infected;
    static CovidCategory recovered;
    static CovidCategory death;
    static CovidCategory beingTreated;

    static String CASE_BY_DAY_ID = "case_by_day";
    static String CASE_TOTAL_ID = "case_total_day";
    static String caseByDayGraph = "";
    static String caseTotalGraph = "";

    public static String getCovidInfo(){
        if (!updated) loadInfo();

        Element containerForCategories = new Element("div");
        containerForCategories.attr("style", "display: flex; justify-content: space-around;");
        containerForCategories.addClass("covid-info");

        containerForCategories.appendChild(infected.getDiv());
        containerForCategories.appendChild(recovered.getDiv());
        containerForCategories.appendChild(death.getDiv());
        containerForCategories.appendChild(beingTreated.getDiv());

        Element blockquote = new Element("blockquote");

        blockquote.appendChild(containerForCategories);
        blockquote.append("<p>* Số ca nhiễm bao gồm cả trong nước và nhập cảnh</p>");


        if(!caseByDayGraph.isEmpty() || !caseTotalGraph.isEmpty()){
            blockquote.append(caseByDayGraph);
            blockquote.append(caseTotalGraph);

        }

        return blockquote.outerHtml();
    }

    private static void loadInfo(){
        if (driver == null){
            activateWebDriver();
        }

        String infectedToday = getStats("item-nhiem","today-item");
        String infectedTotal = getStats("item-nhiem","number-item");
        infected = new CovidCategory("Nhiễm*", "covid-infected", infectedToday, infectedTotal);

        String recoveredToday = getStats("item-khoi","today-item");
        String recoveredTotal = getStats("item-khoi","number-item");
        recovered = new CovidCategory("Khỏi", "covid-recovered", recoveredToday, recoveredTotal);

        String deathToday = getStats("item-tuvong","today-item");
        String deathTotal = getStats("item-tuvong","number-item");
        death = new CovidCategory("Tử vong", "covid-death", deathToday, deathTotal);

        String beingTreatedToday = getStats("item-dangdieutri","today-item");
        String beingTreatedTotal = getStats("item-dangdieutri", "number-item");
        beingTreated = new CovidCategory("Đang điều trị", "covid-being-treated", beingTreatedToday, beingTreatedTotal);

        caseByDayGraph = loadGraphByID(CASE_BY_DAY_ID);
        caseTotalGraph = loadGraphByID(CASE_TOTAL_ID);
        updated = true;
        deactivateWebDriver();
    }

    private static String getStats(String parentClass, String childCss){
        try{
            return driver
                    .findElement(By.className(parentClass))
                    .findElement(By.className(childCss))
                    .getText();
        } catch (NoSuchElementException e){
            return "";
        }
    }

    private static String loadGraphByID(String graphID){
        try{
            return driver
                    .findElement(By.id(graphID))
                    .getAttribute("outerHTML");
        } catch (NoSuchSessionException e){
            return "";
        }
    }


    private static void activateWebDriver(){
        if (driver == null){
            WebDriverManager.chromedriver().browserVersion(stableChromeVersion).setup();
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless", "--disable-gpu", "--window-size=1920,1200","--ignore-certificate-errors");
            driver = new ChromeDriver(options);
            driver.get(source);
        }
    }

    private static void deactivateWebDriver(){
        if (driver != null){
            driver.close();
        }
    }
}


final class CovidCategory {
    String description;
    String cssClass;
    String todayCount;
    String totalCount;

    public CovidCategory(String description, String cssClass, String todayCount, String totalCount){
        this.description = description;
        this.cssClass = cssClass;
        this.todayCount = todayCount;
        this.totalCount = totalCount;
    }

    public Element getDiv(){
        Element container = new Element("div");
        container.id(cssClass);
        container.addClass("covid-count");
        container.attr("style", "display: flex; flex-direction: column;");


        Element category = new Element("div");
        category.addClass("covid-category");
        category.text(description);

        Element today = new Element("span");
        today.addClass("covid-today-count");
        today.text(todayCount);

        Element total = new Element("span");
        total.addClass("covid-total-count");
        total.text(totalCount);

        container.appendChild(category);
        container.appendChild(total);
        container.appendChild(today);

        return container;
    }


}