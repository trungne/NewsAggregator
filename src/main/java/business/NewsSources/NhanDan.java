package business.NewsSources;
import org.apache.commons.lang3.StringUtils;

import business.Helper.CATEGORY;
import business.Helper.CSS;
import business.Sanitizer.HtmlSanitizer;
import business.Sanitizer.NhanDanSanitizer;
import business.Helper.Scraper;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.time.LocalDateTime;
import java.util.HashMap;

public class NhanDan extends NewsOutlet{
    private static final String NHANDAN_COVID = "https://nhandan.vn/tieu-diem";
    private static final String NHANDAN_POLITICS = "https://nhandan.vn/chinhtri";
    private static final String NHANDAN_BUSINESS = "https://nhandan.vn/kinhte";
    private static final String NHANDAN_TECHNOLOGY = "https://nhandan.vn/khoahoc-congnghe";
    private static final String NHANDAN_HEALTH = "https://nhandan.vn/y-te";
    private static final String NHANDAN_SPORTS = "https://nhandan.vn/thethao";
    private static final String NHANDAN_ENTERTAINMENT = "https://nhandan.vn/vanhoa";
    private static final String NHANDAN_WORLD = "https://nhandan.vn/thegioi";
    public static NewsOutlet init(){
        HashMap<String, String> categories = new HashMap<>();
        categories.put(CATEGORY.COVID, NHANDAN_COVID);
        categories.put(CATEGORY.POLITICS, NHANDAN_POLITICS);
        categories.put(CATEGORY.BUSINESS, NHANDAN_BUSINESS);
        categories.put(CATEGORY.TECHNOLOGY, NHANDAN_TECHNOLOGY);
        categories.put(CATEGORY.HEALTH, NHANDAN_HEALTH);
        categories.put(CATEGORY.SPORTS, NHANDAN_SPORTS);
        categories.put(CATEGORY.ENTERTAINMENT, NHANDAN_ENTERTAINMENT);
        categories.put(CATEGORY.WORLD, NHANDAN_WORLD);
        Categories NhanDanCategories = new Categories(categories);
        CssConfiguration NhanDanCssConfig = new CssConfiguration(
                "https://nhandan.vn/",
                CSS.NHANDAN_TITLE_LINK,
                CSS.NHANDAN_TITLE,
                CSS.NHANDAN_DESCRIPTION,
                CSS.NHANDAN_BODY,
                CSS.NHANDAN_TIME,
                CSS.NHANDAN_PIC);
        return new NhanDan("Nhan Dan",
                "https://www.nhandan-printing.vn/datafiles_D_D/setmulti/nhandan_copy.jpg",
                NhanDanCategories,
                NhanDanCssConfig,
                CSS.NHANDAN_THUMBNAIL,
                new NhanDanSanitizer());
    }

    private String thumbnailCss = "";
    public NhanDan(String name,
                   String defaultThumbnail,
                   Categories categories,
                   CssConfiguration cssConfiguration,
                   String thumbnailCss,
                   HtmlSanitizer sanitizer) {
        super(name, defaultThumbnail, categories, cssConfiguration, sanitizer);
        this.thumbnailCss = thumbnailCss;
    }

    @Override
    public LocalDateTime getPublishedTime(Document doc) {
        Element dateTimeTag = Scraper.scrapeFirstElementByClass(doc, cssConfiguration.publishedTime);
        if (dateTimeTag == null)
            return LocalDateTime.now();

        String dateTimeStr = getDateTimeSubString(dateTimeTag.text());

        // 1 0 - 0 7 - 2 0 2 1 , [space] 0  8  :  4  6
        // 0 1 2 3 4 5 6 7 8 9 10 11     12 13 14 15 16
        int day = Integer.parseInt(dateTimeStr.substring(0,2));
        int month = Integer.parseInt(dateTimeStr.substring(3,5));
        int year = Integer.parseInt(dateTimeStr.substring(6,10));
        int hours = Integer.parseInt(dateTimeStr.substring(12,14));
        int minutes = Integer.parseInt(dateTimeStr.substring(15));
        return (LocalDateTime.of(year, month, day, hours, minutes));
    }

    @Override
    public String getCategory(Document doc) {
        Element tag = doc.selectFirst(".bc-item");
        if (tag == null)
            return CATEGORY.OTHERS;

        if (StringUtils.isEmpty(tag.text()))
            return CATEGORY.OTHERS;

        return tag.text();
    }

    // remove day of the week from the datetime string.
    // Example Chủ Nhật, 10-07-2021, 08:45 into
    // 10-07-2021, 08:45
    private String getDateTimeSubString(String str){
        for(int i = 0; i < str.length(); i++){
            // get the substring from the first digit onwards
            if (Character.isDigit(str.charAt(i))){
                return str.substring(i);
            }
        }
        return "";
    }
}
