package business.NewsSources;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static business.Helper.Scraper.scrapeLinksByClass;

public class Category {
    String name = "";
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
            if (category.getName().equals(name)){
                subCategories.set(subCategories.indexOf(category), newCategory);
                return;
            }
        }

        subCategories.add(newCategory);
    }

    public void addSub(String url) {
        add("", url);
    }

    public void find(String name) {
        throw new UnsupportedOperationException();
    }

    // operation methods used by both composite and child class
    public String getName() {
        return this.name;
    }

    public Set<URL> getLinks() {
        Set<URL> urls = new HashSet<>();
        // base case
        if (subCategories.isEmpty()){
            try {
                URL link = new URL(this.url);
                return scrapeLinksByClass(link, cssForScraping);
            } catch (MalformedURLException e) {
                return new HashSet<>();
            }
        }
        else{
            for (Category category: subCategories){
                urls.addAll(category.getLinks());
            }
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
