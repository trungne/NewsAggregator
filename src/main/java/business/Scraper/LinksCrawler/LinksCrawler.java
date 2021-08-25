package business.Scraper.LinksCrawler;

import business.Helper.ScrapingUtils;
import business.Scraper.Category;
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
    private static final String[] COVID_CATEGORY_LINKS = new String[]{
            "https://vnexpress.net/covid-19/tin-tuc",
            "https://thanhnien.vn/covid/",
            "https://zingnews.vn/tieu-diem/covid-19.html",
            "https://tuoitre.vn/covid-19.html",
            "https://nhandan.vn/tieu-diem"
    };
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
                        String targetClass,
                        DocGenerator docGenerator) throws IOException {
        this.homepageUrl = new URL(url);
        this.navBarCssClass = navBarClass;
        this.targetCssClass = targetClass;
        this.doc = docGenerator.getDocument(url);
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
            for (String url: COVID_CATEGORY_LINKS){
                try{
                    URL _url = new URL(url);
                    links.add(_url);
                } catch (MalformedURLException ignored){
                    // do nothing
                }
            }
        }
        else {
            links.addAll(navigateLinksInNavBar(name));
        }
        return links;
    }

    private List<URL> navigateLinksInNavBar(String name){
        Element navBar = doc.selectFirst("." + navBarCssClass); // get navigation bar
        if (navBar == null) {
            return new ArrayList<>(); // return an empty list instead of null to prevent error
        }

        // main categories are stored in <li>
        List<URL> links = new ArrayList<>();
        for (Element menu: navBar.getElementsByTag("li")) {
            Elements aTags = menu.getElementsByTag("a");
            for (int i = 0; i < aTags.size(); i++) {
                Element currentTag = aTags.get(i);
                // a tag contains the name of the category BUT in Vietnames
                String categoryName = Category.convert(currentTag.ownText()); // need to be translated into English

                // compare the provided name with the name in a tag
                if (categoryName.equals(name) && i == 0) {
                    // get all other a tags if this is the main category (index = 0)
                    return extractAllLinksFromTag(menu);
                } else if (categoryName.equals(name)) {
                    // only get this tag if it is the sub category (index =/= 0)
                    try {
                        URL categoryUrl = new URL(homepageUrl, currentTag.attr("href"));
                        return new ArrayList<>(Collections.singleton(categoryUrl));
                    } catch (MalformedURLException ignored) {
                        // do thing
                    }
                }
            }
        }
        return links;
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
