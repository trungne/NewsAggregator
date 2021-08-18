package business.NewsSources;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static business.Helper.Scraper.scrapeLinksByClass;

public class Category {
    String name;
    String url;
    String cssForScraping;
    List<Category> subCategories = new ArrayList<>();

    protected Category(String name, String url, String cssForScraping) {
        this.name = name;
        this.url = url;
        this.cssForScraping = cssForScraping;
    }

    public void add(String name, String url) {
        Category newCategory = new Category(name, url, cssForScraping);

        for (Category category: subCategories){
            // update category if the category already exists in subcategory list
            if (category.getUrl().equals(url)){
                subCategories.set(subCategories.indexOf(category), newCategory);
                return;
            }
        }
        subCategories.add(newCategory);
    }

    public void add(String url) {
        add("", url);
    }
    public Category find(String name) {
        if (this.name.equals(name)){
            return this;
        }

        for (Category category: subCategories){
            Category matched = category.find(name);
            if (matched != null){
                return matched;
            }
        }

        return null;
    }

    public String getUrl() {return this.url;}
    public Set<URL> getLinks() {
        Set<URL> urls = new HashSet<>();
        try {
            URL link = new URL(this.url);
            urls = scrapeLinksByClass(link, cssForScraping);
        } catch (MalformedURLException ignored){}

        for (Category category: subCategories){
            urls.addAll(category.getLinks());
        }

        return urls;
    }

    @Override
    public String toString() {
        return "Category{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", cssForScraping='" + cssForScraping + '\'' +
                '}';
    }
}
