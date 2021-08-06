package business.NewsSources;

import java.net.URL;
import java.util.*;

public class MainCategory extends Category{
    ArrayList<Category> subCategories = new ArrayList<>();

    public MainCategory(String name, String url, String css) {
        super(name, url, css);
    }

    @Override
    public void add(Category category) {
        subCategories.add(category);
    }

    @Override
    public void remove(Category category) {
        subCategories.remove(category);
    }

    @Override
    public Category getChild(int i) {
        return subCategories.get(i);
    }

    @Override
    public Set<URL> getLinks() {
        HashSet<URL> urls = new HashSet<>(super.getLinks());
        for (Category subCategory: subCategories){
            urls.addAll(subCategory.getLinks());
        }
        return urls;
    }


}
