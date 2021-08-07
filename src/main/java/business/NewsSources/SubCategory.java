package business.NewsSources;

public class SubCategory extends Category {
    public SubCategory(String url, String css) {
        this.url = url;
        this.cssForScraping = css;
    }

    public SubCategory(String name, String url, String css) {
        super(name, url, css);
    }
}
