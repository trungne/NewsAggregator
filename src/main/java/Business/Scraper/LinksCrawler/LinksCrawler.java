package Business.Scraper.LinksCrawler;

import Business.Scraper.Helper.ScrapingUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private Document doc;
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
        this.doc = getDocumentFromFile();

        if (this.doc == null){
            this.doc = getDocumentFromURL(url);
        }

        if (this.doc == null) {
            throw new IOException();
        }
    }

    /** Get urls of articles in a category
     * @param name name of the category
     * @return set of URLs of articles in the category
     * */
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
            URL link = getCovidLink();
            if (link != null){
                links.add(link);
            }
        }
        else if (Category.isMainCategory(name)){
            links.addAll(getLinksInNavBar(name));
        }
        else{
            links.addAll(getOtherCategories());
        }
        return links;
    }



    /** @return the link to the Covid-19 section
     * */
    private URL getCovidLink(){
        for(String covidSite: COVID_CATEGORY_LINKS){
            if (covidSite.contains(homepageUrl.getHost())){
                try {
                    return new URL(covidSite);
                } catch (MalformedURLException e) {
                    break;
                }
            }
        }
        return null;
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

    private List<URL> getLinksInNavBar(String name){
        Element navBar = doc.selectFirst("." + navBarCssClass); // get navigation bar
        if (navBar == null) {
            return new ArrayList<>(); // return an empty list instead of null to prevent error
        }

        List<URL> links = new ArrayList<>();
        // main categories are stored in <li>
        for (Element menu: navBar.getElementsByTag("li")) {
            Elements aTags = menu.getElementsByTag("a");
            for (Element a: aTags){
                // a tag contains the name of the category BUT in Vietnamese
                String vietnameseName = a.ownText();

                if(StringUtils.isEmpty(vietnameseName)
                        || a.attr("href").contains("video")){
                    continue;
                }

                String categoryName = Category.translateToEnglish(vietnameseName);

                // compare the provided name with the name in a tag
                if (categoryName.equals(name)) {
                    // get all other a tags if this is the main category (index = 0)
                    if (aTags.indexOf(a) == 0)
                        links.addAll(extractAllLinksFromTag(menu));
                    else
                        links.add(extractLinkFromTag(a));
                }
            }
        }
        return links;
    }

    private URL extractLinkFromTag(Element tag){
        Element first = tag.getElementsByTag("a").first();
        if (first != null) {
            try{
                if (!first.attr("href").contains("video")){
                    return new URL(homepageUrl, first.attr("href"));
                }
            } catch (MalformedURLException ignored) {}
        }
        return null;
    }

    private List<URL> extractAllLinksFromTag(Element tag){
        List<URL> links = new ArrayList<>();
        for (Element link: tag.getElementsByTag("a")){
            URL url = extractLinkFromTag(link);
            if (url != null){
                links.add(url);
            }
        }
        return links;
    }

    private Document getDocumentFromFile(){
        // Rename filename from example.com to example.html
        String htmlFilename = homepageUrl.getHost().replaceFirst("\\.(.+)",".html");
        Path path = Paths.get("src", "main", "resources", "homepages", htmlFilename);
        File file = new File(path.toAbsolutePath().toString());
        try {
            return Jsoup.parse(file, "UTF-8", homepageUrl.toString());
        } catch (IOException ignored) {
            return null;
        }
    }

    private Document getDocumentFromURL(String url) {
        return ScrapingUtils.getDocumentAndDeleteCookies(url);
    }
}
