package business.Scraper;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import static business.Helper.ScrapingUtils.scrapeLinksByClass;

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
    private static final HashMap<String, String[]> dictionary = new HashMap<>();

    static {
//        CategoriesMapping.put(COVID, new String[]{""});
        dictionary.put(POLITICS, new String[]{"chính trị"});
        dictionary.put(BUSINESS, new String[]{"kinh doanh", "tài chính - kinh doanh", "kinh tế"});
        dictionary.put(TECHNOLOGY, new String[]{"khoa học - công nghệ", "công nghệ", "khoa học"});
        dictionary.put(HEALTH, new String[]{"y tế", "sức khỏe"});
        dictionary.put(SPORTS, new String[]{"thể thao"});
        dictionary.put(ENTERTAINMENT, new String[]{"văn hóa", "giải trí"});
        dictionary.put(WORLD, new String[]{"thế giới"});
        dictionary.put(SOCIETY, new String[]{"xã hội", "thời sự"});
        dictionary.put(LAWS, new String[]{"pháp luật", "luật pháp"});
        dictionary.put(EDUCATION, new String[]{"giáo dục"});
        dictionary.put(LIFE, new String[]{"đời sống", "nhịp sống trẻ"});
        dictionary.put(TOURISM, new String[]{"du lịch", "du lịch - ẩm thực"});
        dictionary.put(CAR, new String[]{"xe"});
    }

    // convert a category name from Vietnamese to English
    public static String convert(String category) {
        for (String english : dictionary.keySet()) {
            for (String vietnamse : dictionary.get(english)) {
                if (category.toLowerCase(Locale.ROOT).contains(vietnamse)) {
                    return english;
                }
            }
        }
        return OTHERS;
    }
}
