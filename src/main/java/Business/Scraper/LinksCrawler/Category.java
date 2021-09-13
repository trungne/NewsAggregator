/*
  RMIT University Vietnam
  Course: INTE2512 Object-Oriented Programming
  Semester: 2021B
  Assessment: Final Project
  Created  date: dd/mm/yyyy
  Author: Student name, Student ID
  Last modified date: dd/mm/yyyy
  Author: Student name, Student ID
  Acknowledgement: Thanks and give credits to the resources that you used in this file
*/

package Business.Scraper.LinksCrawler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

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
    public static final HashSet<String> MAIN_CATEGORIES = new HashSet<>(
            List.of(NEW, COVID, POLITICS, BUSINESS, TECHNOLOGY, HEALTH, SPORTS, ENTERTAINMENT, WORLD));

    public static boolean isMainCategory(String category){
        return MAIN_CATEGORIES.contains(category);
    }


    public static final String SOCIETY = "Society";
    public static final String EDUCATION = "Education";
    public static final String LIFE = "Life";
    public static final String TOURISM = "Tourism";
    public static final String CAR = "Car";

    public static final String OTHERS = "Others";

    // map Vietnamese names to English
    private static final HashMap<String, HashSet<String>> dictionary = new HashMap<>();
    static {
        dictionary.put(NEW, new HashSet<>(List.of("mới nhất")));
        dictionary.put(COVID, new HashSet<>(List.of("covid", "covid-19")));
        dictionary.put(POLITICS, new HashSet<>(List.of("chính trị", "pháp luật", "luật pháp")));
        dictionary.put(BUSINESS, new HashSet<>(List.of("kinh doanh", "tài chính - kinh doanh", "kinh tế")));
        dictionary.put(TECHNOLOGY, new HashSet<>(List.of("khoa học - công nghệ", "công nghệ", "khoa học", "Khoa học - Công nghệ")));
        dictionary.put(HEALTH, new HashSet<>(List.of("y tế", "sức khỏe")));
        dictionary.put(SPORTS, new HashSet<>(List.of("thể thao")));
        dictionary.put(ENTERTAINMENT, new HashSet<>(List.of("văn hóa", "giải trí")));
        dictionary.put(WORLD, new HashSet<>(List.of("thế giới")));
        dictionary.put(SOCIETY, new HashSet<>(List.of("xã hội", "thời sự")));
        dictionary.put(EDUCATION, new HashSet<>(List.of("giáo dục")));
        dictionary.put(LIFE, new HashSet<>(List.of("đời sống", "nhịp sống trẻ")));
        dictionary.put(TOURISM, new HashSet<>(List.of("du lịch", "du lịch - ẩm thực")));
        dictionary.put(CAR, new HashSet<>(List.of("xe")));
    }

    /** Convert a category name from Vietnamese to English
     * @param category - name of the category in Vietnamese
     * @return name of the category in English, returns "Others" when no matches are found
     * */
    public static String translateToEnglish(String category) {
        String cate = category.trim().toLowerCase(Locale.ROOT);
        for (String english : dictionary.keySet()) {
            if(dictionary.get(english).contains(cate)){
                return english;
            }
        }
        return OTHERS;
    }
}
