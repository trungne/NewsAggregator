package business.NewsSources;

import org.apache.commons.lang3.StringUtils;

import java.net.URL;
import java.util.*;

public class MainCategory extends Category{
    Set<Category> subCategories = new HashSet<>();

    public MainCategory(String name, String url, String css) {
        super(name, url, css);
    }

    @Override
    public void add(Category category) {
        subCategories.add(category);
    }

    @Override
    public void addSub(String url){
        SubCategory subCategory = new SubCategory(url, cssForScraping);
        subCategories.add(subCategory);
    }

    @Override
    public void remove(Category category) {
        subCategories.remove(category);
    }

    @Override
    public Set<URL> getLinks() {
        Set<URL> urls = new HashSet<>();
        if (!StringUtils.isEmpty(url)){
            urls.addAll(super.getLinks());
        }

        for (Category category: subCategories){
            urls.addAll(category.getLinks());
        }
        return urls;
    }


}
