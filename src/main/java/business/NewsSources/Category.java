package business.NewsSources;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import static business.Helper.Scraper.scrapeLinksByClass;

public class Category {
    public static final String NEW = "New";
    public static final String COVID = "Covid";
    public static final String POLITICS = "Politics";
    public static final String BUSINESS = "Business";
    public static final String TECHNOLOGY = "Technology";
    public static final String HEALTH = "Health";
    public static final String SPORTS = "Sports";
    public static final String ENTERTAINMENT = "Entertainment";
    public static final String WORLD = "World";
    public static final String OTHERS = "Others";

    public static final String SOCIETY = "Society";
    public static final String LAWS = "Laws";
    public static final String EDUCATION = "Education";
    public static final String LIFE = "Life";
    public static final String TOURISM = "Tourism";
    public static final String CAR = "Car";

    // map Vietnamese names to English
    private static final HashMap<String, String[]> CategoriesMapping = new HashMap<>();
    static {
//        CategoriesMapping.put(COVID, new String[]{""});
        CategoriesMapping.put(POLITICS, new String[]{"chính trị"});
        CategoriesMapping.put(BUSINESS, new String[]{"kinh doanh", "tài chính - kinh doanh", "kinh tế"});
        CategoriesMapping.put(TECHNOLOGY, new String[]{"khoa học - công nghệ", "công nghệ", "khoa học"});
        CategoriesMapping.put(HEALTH, new String[]{"y tế", "sức khỏe"});
        CategoriesMapping.put(SPORTS, new String[]{"thể thao"});
        CategoriesMapping.put(ENTERTAINMENT, new String[]{"văn hóa", "giải trí"});
        CategoriesMapping.put(WORLD, new String[]{"thế giới"});
        CategoriesMapping.put(SOCIETY, new String[]{"xã hội", "thời sự"});
        CategoriesMapping.put(LAWS, new String[]{"pháp luật", "luật pháp"});
        CategoriesMapping.put(EDUCATION, new String[]{"giáo dục"});
        CategoriesMapping.put(LIFE, new String[]{"đời sống", "nhịp sống trẻ"});
        CategoriesMapping.put(TOURISM, new String[]{"du lịch", "du lịch - ẩm thực"});
        CategoriesMapping.put(CAR, new String[]{"xe"});
    }

    public static String convert(String category) {
        for (String english: CategoriesMapping.keySet()){
            for(String vietnamse: CategoriesMapping.get(english)){
                if (category.toLowerCase(Locale.ROOT).equals(vietnamse)){
                    return english;
                }
            }
        }
        return "";
    }



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
