package News;

public class CssConfiguration {
    public String baseUrl;
    public String titleInCategoryPage;
    public String title;
    public String description;
    public String mainContent;
    public String publishedTime;
    public String picture;

    public CssConfiguration(String baseUrl,
                            String titleInCategoryPage,
                            String title,
                            String description,
                            String mainContent,
                            String publishedTime,
                            String picture){
        this.baseUrl = baseUrl;
        this.titleInCategoryPage = titleInCategoryPage;
        this.title = title;
        this.description = description;
        this.mainContent = mainContent;
        this.publishedTime = publishedTime;
        this.picture = picture;
    }

}
