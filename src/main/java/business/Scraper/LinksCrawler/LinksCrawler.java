package business.Scraper.LinksCrawler;

import business.Scraper.Helper.ScrapingUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class LinksCrawler {
    // TODO: Trung comments this
    private static final String[] COVID_CATEGORY_LINKS = new String[]{
            "https://vnexpress.net/covid-19/tin-tuc",
            "https://thanhnien.vn/covid/",
            "https://zingnews.vn/tieu-diem/covid-19.html",
            "https://tuoitre.vn/covid-19.html",
            "https://nhandan.vn/tieu-diem"
    };
//    private static final ChromeOptions OPTIONS = new ChromeOptions()
//            .addArguments("--headless",
//                    "--disable-gpu",
//                    "--window-size=1,1",
//                    "--ignore-certificate-errors",
//                    "--disable-popup-blocking");
//    static {
//        WebDriverManager.chromedriver().setup();
//    }

    private final URL homepageUrl;
    private final String navBarCssClass;
    private final String targetCssClass;
    private final Document doc;

    /** @param url url to the news outlet's homepage
     *  @param navBarClass a css class used to target the navigation bar
     *  @param targetClass a css class used to target the links
     * */
    public LinksCrawler(String url,
                        String navBarClass,
                        String targetClass) throws IOException {
        this.homepageUrl = new URL(url);
        this.navBarCssClass = navBarClass;
        this.targetCssClass = targetClass;
        this.doc = Jsoup
                .connect(url)
                .timeout(10000)
                .get();
    }

    public Set<URL> getArticleLinks(String name){
        List<URL> categoryLinks = getCategoryLinks(name);
        Set<URL> articleLinks = new HashSet<>();
        for (URL link: categoryLinks){
            articleLinks.addAll(ScrapingUtils.scrapeLinksByClass(link, targetCssClass));
        }
        return articleLinks;
    }

    /** @param name of the category in English
     * @return URL(s) of the provided category.
     * */
    private List<URL> getCategoryLinks(String name) {
        List<URL> links = new ArrayList<>();
        // scrape the front page is provided category is NEW
        if(name.equals(Category.NEW)){
            links.add(homepageUrl);
        }
        else if (name.equals(Category.COVID)){
            links.addAll(getCovidCategoryLinks());
        }
        else if (Category.isMainCategory(name)){
            links.addAll(navigateLinksInNavBar(name));
        }
        else{
            links.addAll(getOtherCategories());
        }
        return links;
    }

    private List<URL> getCovidCategoryLinks(){
        List<URL> links = new ArrayList<>();
        for (String url: COVID_CATEGORY_LINKS){
            try{
                URL _url = new URL(url);
                links.add(_url);
            } catch (MalformedURLException ignored){
                // do nothing
            }
        }
        return links;
    }
    private List<URL> getOtherCategories(){
        Element navBar = doc.selectFirst("." + navBarCssClass); // get navigation bar
        if (navBar == null){
            return new ArrayList<>();
        }
        List<URL> links = new ArrayList<>();

        for (Element menu: navBar.getElementsByTag("li")){
            Element firstATag = menu.getElementsByTag("a").first();
            if (firstATag == null || StringUtils.isEmpty(firstATag.ownText())){
                continue;
            }
            String vietnameseName = firstATag.ownText();
            String englishName = Category.translateToEnglish(vietnameseName);
            if (!Category.isMainCategory(englishName)){
                try {
                    URL url = new URL(homepageUrl, firstATag.attr("href"));
                    links.add(url);
                } catch (MalformedURLException ignored) {}
            }
        }
        return links;

    }

    private List<URL> navigateLinksInNavBar(String name){
        Element navBar = doc.selectFirst("." + navBarCssClass); // get navigation bar
        if (navBar == null) {
            return new ArrayList<>(); // return an empty list instead of null to prevent error
        }

        // main categories are stored in <li>
        for (Element menu: navBar.getElementsByTag("li")) {
            Elements aTags = menu.getElementsByTag("a");
            for (int i = 0; i < aTags.size(); i++) {
                Element currentTag = aTags.get(i);
                // a tag contains the name of the category BUT in Vietnamese
                String vietnameseName = currentTag.ownText();

                if(StringUtils.isEmpty(vietnameseName)){
                    continue;
                }

                String categoryName = Category.translateToEnglish(vietnameseName);

                // compare the provided name with the name in a tag
                if (categoryName.equals(name)) {
                    // get all other a tags if this is the main category (index = 0)
                    if (i == 0){
                        System.out.println(extractAllLinksFromTag(menu));
                        return extractAllLinksFromTag(menu);
                    }
                    else{
                        System.out.println(extractOneLinkFromTag(currentTag));
                        return List.of(Objects.requireNonNull(extractOneLinkFromTag(currentTag)));
                    }
                }
            }
        }

//        System.out.println("In LinksCrawler: "
//                + name
//                + "(" + ")"
//                + " - " + homepageUrl);
        return new ArrayList<>();
    }
    private URL extractOneLinkFromTag(Element tag){
        Element first = tag.getElementsByTag("a").first();
        if (first != null) {
            try{
                return new URL(homepageUrl, first.attr("href"));
            } catch (MalformedURLException ignored) {

            }
        }
        return null;
    }
    private List<URL> extractAllLinksFromTag(Element tag){
        List<URL> links = new ArrayList<>();
        for (Element link: tag.getElementsByTag("a")){
            try{
                URL url = new URL(homepageUrl, link.attr("href"));
                links.add(url);
            }
            catch (MalformedURLException ignored){
                // do nothing
            }
        }
        return links;
    }


}
