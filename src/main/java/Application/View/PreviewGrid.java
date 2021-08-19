package Application.View;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class PreviewGrid extends GridPane {
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
        Text t = new Text(title);
        Font titleFont = new Font("Helvetica", 20);
        t.setFont(titleFont);

        // description
        Text desp = new Text(description);
        Font descriptionFont = new Font("Helvetica", 12);
        desp.setWrappingWidth(500);
        desp.setFont(descriptionFont);

        // published time and newsource
        Text time = new Text(publishedTime);
        Text newsSource = new Text(source);

        // arrange components in grid to make a preview
        this.add(iv, 1, 1, 1, 2);
        this.add(t, 2, 1);
        this.add(desp, 2, 2);
        this.add(time, 2, 3);
        this.add(newsSource, 1, 3);
        this.setUserData(article);
    }
}
