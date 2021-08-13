package Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
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

import static Application.Helper.createPreviewPane;

public class Controller {
    @FXML
    private Pane previewBox;
    @FXML
    private ScrollPane mainArea;

    List<Article> articles;

    public void initialize(){
        System.out.println("system initialized!");
    }
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

    private void loadArticles(String category){
        final GetArticleService service = new GetArticleService();
        ProgressBar progressBar = new ProgressBar();

        progressBar.progressProperty().bind(service.progressProperty());
        progressBar.visibleProperty().bind(service.runningProperty());

    }


}



