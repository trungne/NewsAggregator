package Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import business.ArticleCollection;
import business.News.Article;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.*;

public class Controller {
    @FXML
    private Pane previewBox;
    @FXML
    private ScrollPane mainArea;

    List<Article> articles;
    public void displayNews(ActionEvent e){
        Object o = e.getSource();
        if (o instanceof Button){
            Button b = (Button) o;
            displayPreviews(b.getText());
        }
    }

    public void displayPreviews(String category){

        previewBox.getChildren().clear();

        articles = ArticleCollection.getArticlesByCategory(category);

        for (int i = 0; i < 10; i++){
            previewBox.getChildren().add(createPreviewPane(articles.get(i)));
        }

        mainArea.setContent(previewBox);
    }

    private GridPane createPreviewPane(Article article){
        GridPane grid = new GridPane();

        // thumbnail
        Image image = new Image(article.getThumbNail(), 160, 90, false, false);
        ImageView thumbnail = new ImageView(image);

        // title
        Text title = new Text(article.getTitle());
        Font titleFont = new Font("Helvetica", 20);
        title.setFont(titleFont);

        // description
        Text description = new Text(article.getDescription());
        Font descriptionFont = new Font("Helvetica", 12);
        description.setWrappingWidth(500);
        description.setFont(descriptionFont);

        // published time
        Text publishedTime = new Text(article.getRelativeTime());
        Text newsSource = new Text(article.getNewsSource());

        // arrange components in grid to make a preview
        grid.add(thumbnail,1,1,1,2);
        grid.add(title,2,1);
        grid.add(description,2,2);
        grid.add(publishedTime,2,3);
        grid.add(newsSource,1,3);
        grid.setUserData(article);

        // add events to grid
        grid.addEventHandler(MouseEvent.MOUSE_RELEASED, new OpenArticle());
        grid.addEventHandler(MouseEvent.MOUSE_ENTERED, new UnderlineText());
        grid.addEventHandler(MouseEvent.MOUSE_EXITED, new UndoUnderlineText());

        return grid;
    }

    class OpenArticle implements EventHandler<MouseEvent>{
        @Override
        public void handle(MouseEvent mouseEvent) {
            Node p = (Node) mouseEvent.getSource();
            Article a = (Article) p.getUserData();
            WebView browser = new WebView();
            WebEngine webEngine = browser.getEngine();
            webEngine.loadContent(a.getHtml());
            Scene articleScene = new Scene(browser);
            Stage stage = new Stage();
            stage.setScene(articleScene);
            stage.show();
//            mainArea.setContent(browser);
        }
    }

    static class UnderlineText implements EventHandler<MouseEvent>{
        @Override
        public void handle(MouseEvent mouseEvent) {
            Pane g = (Pane) mouseEvent.getSource();
            for(Node node: g.getChildren()){
                if(node instanceof Text){
                    Text t = (Text) node;
                    t.setUnderline(true);
                }
            }
        }
    }

    static class UndoUnderlineText implements EventHandler<MouseEvent>{
        @Override
        public void handle(MouseEvent mouseEvent) {
            Pane g = (Pane) mouseEvent.getSource();
            for(Node node: g.getChildren()){
                if(node instanceof Text){
                    Text t = (Text) node;
                    t.setUnderline(false);
                }
            }
        }
    }
}



