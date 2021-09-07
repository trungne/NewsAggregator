package Application.View;

import javafx.beans.property.DoubleProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class PreviewGrid extends GridPane {
    private static final Font titleFont = new Font("Helvetica", 20);
    private static final Font descriptionFont = new Font("Helvetica", 12);

    private boolean isUnderlined = false;
    private final ImageView imageView = new ImageView();
    private final Text titleText = new Text();
    private final Text descriptionText = new Text();
    private final Text publishedTimeText = new Text();
    private final Text newsSourceText = new Text();

    // method to get wrapping property of titleText
    public DoubleProperty titleWrappingWidthProperty() {
        return titleText.wrappingWidthProperty();
    }

    public PreviewGrid(){
        titleText.setFont(titleFont);
        descriptionText.setFont(descriptionFont);
        descriptionText.wrappingWidthProperty().bind(titleText.wrappingWidthProperty().subtract(100));

        // arrange components in grid to create layout for preview components
        this.add(imageView, 1, 1, 1, 2);
        this.add(titleText, 2, 1);
        this.add(descriptionText, 2, 2);
        this.add(publishedTimeText, 2, 3);
        this.add(newsSourceText, 1, 3);
    }
    public void setPreviewToGrid(String thumbnail,
                                 String title,
                                 String description,
                                 String publishedTime,
                                 String source) {
        try {
            Image image = new Image(thumbnail,
                    160,
                    90,
                    false,
                    false,
                    true);
            this.imageView.setImage(image);
        } catch (IllegalArgumentException e){
            System.out.println(thumbnail);
        }

        this.titleText.setText(title);
        this.descriptionText.setText(description);
        this.publishedTimeText.setText(publishedTime);
        this.newsSourceText.setText(source);
    }

    public void underline(){
        if (!isUnderlined){
            titleText.setUnderline(true);
            descriptionText.setUnderline(true);
            publishedTimeText.setUnderline(true);
            newsSourceText.setUnderline(true);
        }
        else{
            titleText.setUnderline(false);
            descriptionText.setUnderline(false);
            publishedTimeText.setUnderline(false);
            newsSourceText.setUnderline(false);
        }

        isUnderlined = !isUnderlined;
    }
}
