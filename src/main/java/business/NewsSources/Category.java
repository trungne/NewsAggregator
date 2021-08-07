package business.NewsSources;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;

import static business.Helper.Scraper.scrapeLinksByClass;

// the design is inspired by composite pattern: https://refactoring.guru/design-patterns/composite
public abstract class Category {
    String name;
    String url;
    String cssForScraping;

    protected Category(String name, String url, String cssForScraping) {
        this.name = name;
        this.url = url;
        this.cssForScraping = cssForScraping;
    }

    protected Category() {
    }

    // composite methods
    public void add(Category category) {
        throw new UnsupportedOperationException();
    }

    public void addSub(String url) {
        throw new UnsupportedOperationException();
    }

    public void remove(Category category) {
        throw new UnsupportedOperationException();
    }

    // operation methods used by both composite and child class
    public String getName() {
        return this.name;
    }

    public Set<URL> getLinks() {
        try {
            URL link = new URL(this.url);
            return scrapeLinksByClass(link, cssForScraping);
        } catch (MalformedURLException e) {
            return null;
        }
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
