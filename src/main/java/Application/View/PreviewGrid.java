package Application.View;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class PreviewGrid extends GridPane {
    private Text title;
    private Text description;
    private Text publishedTime;
    private Text newsSource;
    private boolean isUnderlined = false;
    public void setPreviewToGrid(String thumbnail,
                                 String title,
                                 String description,
                                 String publishedTime,
                                 String source,
                                 String article) {
        // thumbnail
        Image image = new Image(thumbnail, 160, 90, false, false);
        ImageView iv = new ImageView(image);

        // title
        this.title = new Text(title);
        Font titleFont = new Font("Helvetica", 20);
        this.title.setFont(titleFont);

        // description
        this.description = new Text(description);
        Font descriptionFont = new Font("Helvetica", 12);
        this.description.setWrappingWidth(500);
        this.description.setFont(descriptionFont);

        // published time and newsource
        this.publishedTime = new Text(publishedTime);
        this.newsSource = new Text(source);

        // arrange components in grid to make a preview
        this.add(iv, 1, 1, 1, 2);
        this.add(this.title, 2, 1);
        this.add(this.description, 2, 2);
        this.add(this.publishedTime, 2, 3);
        this.add(this.newsSource, 1, 3);
        this.setUserData(article);
    }

    public void underline(){
        if (!isUnderlined){
            title.setUnderline(true);
            description.setUnderline(true);
            publishedTime.setUnderline(true);
            newsSource.setUnderline(true);
        }
        else{
            title.setUnderline(false);
            description.setUnderline(false);
            publishedTime.setUnderline(false);
            newsSource.setUnderline(false);
        }

        isUnderlined = !isUnderlined;
    }
}
