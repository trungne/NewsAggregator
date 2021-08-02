package News;

import Scraper.Scraper;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

public class Categories {
    HashMap<String, String> categories;

    public Categories(HashMap<String, String> categories){
        this.categories = categories;
    }

    public Collection<URL> getLinksFromCategory(String category, String css) {
        if (categories.containsKey(category)) {
            try {
                URL categoryUrl = new URL(categories.get(category));
                return new HashSet<>(Scraper.scrapeLinksByClass(categoryUrl, css));
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }
        }
        else
            return null;
    }
    public String toString(){
        return this.categories.toString();
    }
}
