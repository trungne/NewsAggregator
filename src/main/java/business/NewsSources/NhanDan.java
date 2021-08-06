package business.NewsSources;
import org.apache.commons.lang3.StringUtils;

import business.Helper.CATEGORY;
import business.Helper.CSS;
import business.Sanitizer.HtmlSanitizer;
import business.Sanitizer.NhanDanSanitizer;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.time.LocalDateTime;
import java.util.*;

import static business.Helper.Scraper.createCleanImgTag;
import static business.Helper.Scraper.scrapeFirstElementByClass;

public class NhanDan extends NewsOutlet{
    private static final Category COVID = new SubCategory(CATEGORY.COVID, "https://nhandan.vn/tieu-diem", CSS.NHANDAN_TITLE_LINK);
    private static final Category POLITICS = new MainCategory(CATEGORY.POLITICS, "https://nhandan.vn/chinhtri", CSS.NHANDAN_TITLE_LINK);
    static {
        POLITICS.addSub("https://nhandan.vn/tin-tuc-su-kien");
        POLITICS.addSub("https://nhandan.vn/xa-luan");
        POLITICS.addSub("https://nhandan.vn/cung-suy-ngam");
        POLITICS.addSub("https://nhandan.vn/binh-luan-phe-phan");
        POLITICS.addSub("https://nhandan.vn/nguoi-viet-xa-xu");
        POLITICS.addSub("https://nhandan.vn/dang-va-cuoc-song");
        POLITICS.addSub("https://nhandan.vn/dan-toc-mien-nui");
    }

    private static final Category BUSINESS = new MainCategory(CATEGORY.BUSINESS, "https://nhandan.vn/kinhte", CSS.NHANDAN_TITLE_LINK);
    static {
        BUSINESS.addSub("https://nhandan.vn/tin-tuc-kinh-te");
        BUSINESS.addSub("https://nhandan.vn/nhan-dinh");
        BUSINESS.addSub("https://nhandan.vn/chuyen-lam-an");
        BUSINESS.addSub("https://nhandan.vn/chungkhoan");
        BUSINESS.addSub("https://nhandan.vn/hanggiahangthat");
    }

    private static final Category TECHNOLOGY = new MainCategory(CATEGORY.TECHNOLOGY, "https://nhandan.vn/khoahoc-congnghe", CSS.NHANDAN_TITLE_LINK);
    static {
        TECHNOLOGY.addSub("https://nhandan.vn/khoa-hoc");
        TECHNOLOGY.addSub("https://nhandan.vn/vi-moi-truong-xanh");
        TECHNOLOGY.addSub("https://nhandan.vn/thong-tin-so");
    }

    private static final Category HEALTH = new MainCategory(CATEGORY.HEALTH, "https://nhandan.vn/y-te", CSS.NHANDAN_TITLE_LINK);
    static {
        HEALTH.addSub("https://nhandan.vn/benh-thuong-gap");
        HEALTH.addSub("https://nhandan.vn/goc-tu-van");
        HEALTH.addSub("https://nhandan.vn/tin-tuc-y-te");
    }

    private static final Category SPORTS = new MainCategory(CATEGORY.SPORTS, "https://nhandan.vn/thethao", CSS.NHANDAN_TITLE_LINK);
    static {
        SPORTS.addSub("https://nhandan.vn/nhip-song-the-thao");
        SPORTS.addSub("https://nhandan.vn/guong-mat");
        SPORTS.addSub("https://nhandan.vn/bong-da-viet-nam");
        SPORTS.addSub("https://nhandan.vn/bong-da-quoc-te");
    }

    private static final Category ENTERTAINMENT = new MainCategory(CATEGORY.ENTERTAINMENT, "https://nhandan.vn/vanhoa", CSS.NHANDAN_TITLE_LINK);
    static {
        ENTERTAINMENT.addSub("https://nhandan.vn/dong-chay");
        ENTERTAINMENT.addSub("https://nhandan.vn/dien-dan");
        ENTERTAINMENT.addSub("https://nhandan.vn/nghe-doc-xem");
        ENTERTAINMENT.addSub("https://nhandan.vn/di-san");
        ENTERTAINMENT.addSub("https://nhandan.vn/chan-dung");
    }

    private static final Category WORLD = new MainCategory(CATEGORY.WORLD, "https://nhandan.vn/thegioi", CSS.NHANDAN_TITLE_LINK);
    static {
        WORLD.addSub("https://nhandan.vn/cua-so-the-gioi");
        WORLD.addSub("https://nhandan.vn/cong-dong-asean");
        WORLD.addSub("https://nhandan.vn/binh-luan-quoc-te");
        WORLD.addSub("https://nhandan.vn/ho-so-tu-lieu");
        WORLD.addSub("https://nhandan.vn/chuyen-thoi-su");
        WORLD.addSub("https://nhandan.vn/tin-tuc-the-gioi");
    }

    private static final Category OTHERS = new MainCategory(CATEGORY.OTHERS, "", CSS.NHANDAN_TITLE_LINK);
    static {
        OTHERS.addSub("https://nhandan.vn/phapluat");
        OTHERS.addSub("https://nhandan.vn/du-lich");
        OTHERS.addSub("https://nhandan.vn/giaoduc");
        OTHERS.addSub("https://nhandan.vn/bandoc");
    }

    public static NewsOutlet init(){
        HashMap<String, Category> categories = new HashMap<>();
        categories.put(CATEGORY.COVID,COVID);
        categories.put(CATEGORY.POLITICS,POLITICS);
        categories.put(CATEGORY.BUSINESS,BUSINESS);
        categories.put(CATEGORY.TECHNOLOGY,TECHNOLOGY);
        categories.put(CATEGORY.HEALTH,HEALTH);
        categories.put(CATEGORY.SPORTS,SPORTS);
        categories.put(CATEGORY.ENTERTAINMENT,ENTERTAINMENT);
        categories.put(CATEGORY.WORLD,WORLD);
        categories.put(CATEGORY.OTHERS,OTHERS);

        CssConfiguration NhanDanCssConfig = new CssConfiguration(
                "https://nhandan.vn/",
                CSS.NHANDAN_TITLE,
                CSS.NHANDAN_DESCRIPTION,
                CSS.NHANDAN_BODY,
                CSS.NHANDAN_TIME,
                CSS.NHANDAN_PIC);
        return new NhanDan("Nhan Dan",
                "https://www.nhandan-printing.vn/datafiles_D_D/setmulti/nhandan_copy.jpg",
                categories,
                NhanDanCssConfig,
                CSS.NHANDAN_THUMBNAIL,
                new NhanDanSanitizer());
    }

    private String thumbnailCss = "";
    public NhanDan(String name,
                   String defaultThumbnail,
                   HashMap<String, Category> categories,
                   CssConfiguration cssConfiguration,
                   String thumbnailCss,
                   HtmlSanitizer sanitizer) {
        super(name, defaultThumbnail, categories, cssConfiguration, sanitizer);
        this.thumbnailCss = thumbnailCss;
    }

    @Override
    public LocalDateTime getPublishedTime(Document doc) {
        Elements dateTimeTags = doc.select("." + cssConfiguration.publishedTime);
        Element dateTimeTag = dateTimeTags.last();
        if (dateTimeTag == null)
            return LocalDateTime.now();

        String dateTimeStr = getDateTimeSubString(dateTimeTag.text());
        if (StringUtils.isEmpty(dateTimeStr)){
            return LocalDateTime.now();
        }
        // time first
        // 0 7 : 3 0 0 4 - 0 6 -  2  0  2  1
        // 0 1 2 3 4 5 6 7 8 9 10 11 12 13 14
        if (dateTimeStr.substring(0, 5).contains(":")){
            try{
                int hours = Integer.parseInt(dateTimeStr.substring(0,2));
                int minutes = Integer.parseInt(dateTimeStr.substring(3, 5));
                int day = Integer.parseInt(dateTimeStr.substring(5,7));
                int month = Integer.parseInt(dateTimeStr.substring(8,10));
                int year = Integer.parseInt(dateTimeStr.substring(11));
                return (LocalDateTime.of(year, month, day, hours, minutes));
            } catch (NumberFormatException e){
                e.printStackTrace();
                System.out.println(doc);
                return LocalDateTime.now();
            }
        }


        // date first
        // 1 0 - 0 7 - 2 0 2 1 0  8  :  4  6
        // 0 1 2 3 4 5 6 7 8 9 10 11 12 13 14
        try{
            int day = Integer.parseInt(dateTimeStr.substring(0,2));
            int month = Integer.parseInt(dateTimeStr.substring(3,5));
            int year = Integer.parseInt(dateTimeStr.substring(6,10));
            int hours = Integer.parseInt(dateTimeStr.substring(10,12));
            int minutes = Integer.parseInt(dateTimeStr.substring(13));
            return (LocalDateTime.of(year, month, day, hours, minutes));
        } catch (NumberFormatException e){
            e.printStackTrace();
            System.out.println(doc);
            return LocalDateTime.now();
        }

    }

    @Override
    public Element getThumbnail(Document doc) {
        try{
            Element elementContainsImgs = scrapeFirstElementByClass(doc, thumbnailCss);
            Element thumbnail = elementContainsImgs.getElementsByTag("img").first();
            thumbnail = createCleanImgTag(thumbnail);
            thumbnail = sanitizer.sanitizeThumbNail(thumbnail);
            return thumbnail;
        } catch (NullPointerException e){
            return getDefaultThumbnail();
        }
    }

    @Override
    public List<String> getCategoryNames(Document doc) {
        Elements tags = doc.getElementsByClass("bc-item");
        List<String> categoryList = new ArrayList<>();
        if (!tags.isEmpty()){
            for (Element e: tags){
                String category = e.text();
                category = CATEGORY.convert(category);
                if (!categoryList.contains(category)){
                    categoryList.add(category);
                }

            }
        }
        else{
            categoryList.add(CATEGORY.OTHERS);
        }

        return categoryList;


    }

    // remove day of the week from the datetime string.
    // Example Chủ Nhật, 10-07-2021, 08:45 into
    // 10-07-2021, 08:45
    private String getDateTimeSubString(String str){
        str = str.trim();
        str = str.replaceAll(",", "");
        str = str.replaceAll("\\s+", "");
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < str.length(); i++){
            char ch = str.charAt(i);
            // get the substring from the first digit onwards
            if (Character.isDigit(ch) ||
                ch == '-' ||
                ch == ':'){
                builder.append(ch);
            }
        }
        return "";
    }
}
