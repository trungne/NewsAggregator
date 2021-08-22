package business.Helper;

public class CSS {
    // CSS for main content in articles
    // TODO: add news source to article html
    public static final String SOURCE = "source";
    public static final String ARTICLE_HEADER = "article-header";
    public static final String ARTICLE_CATEGORY = "article-category";
    public static final String ARTICLE_CONTENT = "article-content";
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String MAIN_CONTENT = "main-content";
    public static final String LOCATION = "location";


    // CSS Classes for content in main content
    public static final String PARAGRAPH = "content-paragraph";
    public static final String FIGURE = "content-pic";
    public static final String QUOTE = "content-quote";
    public static final String VIDEO = "content-video";
    public static final String AUTHOR = "author";
    public static final String PUBLISHED_TIME = "published-time";

    // CSS for VNExpressScraper
    public static final String VNEXPRESS_TITLE = "title-detail";
    public static final String VNEXPRESS_DESCRIPTION = "description";
    public static final String VNEXPRESS_BODY = "fck_detail";

    // These are css of tags we need to keep in VNExpressScraper article body
    public static final String VNEXPRESS_PARAGRAPH = "Normal";
    public static final String VNEXPRESS_TIME = "datePublished";
    public static final String VNEXPRESS_PIC = "fig-picture";
    public static final String VNEXPRESS_TITLE_LINK = "title-news";

    // CSS for ZingNewsScraper
    public static final String ZING_TITLE_LINK = "article-title";
    public static final String ZING_TITLE = "the-article-title";
    public static final String ZING_DESCRIPTION = "the-article-summary";
    public static final String ZING_BODY = "the-article-body";
    public static final String ZING_TIME = "article:published_time";
    public static final String ZING_PIC = "pic";

    // CSS for TuoiTreScraper
    public static final String TUOITRE_TITLE_LINK = "title-news";
    public static final String TUOITRE_TITLE = "article-title";
    public static final String TUOITRE_DESCRIPTION = "sapo";
    public static final String TUOITRE_BODY = "fck";
    public static final String TUOITRE_TIME = "article:published_time";
    public static final String TUOITRE_PIC = "VCSortableInPreviewMode";

    // CSS for ThanhNienScraper
    public static final String THANHNIEN_TITLE_LINK = "story__thumb";
    public static final String THANHNIEN_TITLE = "details__headline";
    public static final String THANHNIEN_DESCRIPTION = "sapo";
    // TODO: switch to pswp-content for thanh nien body
    public static final String THANHNIEN_BODY = "details__content";
    public static final String THANHNIEN_TIME = "article:published_time";
    public static final String THANHNIEN_PIC = "pswp-content__image";

    // CSS for NhanDanScraper
    public static final String NHANDAN_TITLE_LINK = "box-title";
    public static final String NHANDAN_TITLE = "box-title-detail";
    public static final String NHANDAN_DESCRIPTION = "box-des-detail";
    public static final String NHANDAN_BODY = "box-content-detail";
    public static final String NHANDAN_TIME = "box-date";
    public static final String NHANDAN_PIC = "box-detail-thumb";
    public static final String NHANDAN_THUMBNAIL = "box-detail-thumb";
    public static final String NHANDAN_AUTHOR = "box-author";
}
