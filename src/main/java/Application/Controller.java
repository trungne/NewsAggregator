package Application;
import business.GetArticleListService;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

import business.News.Article;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.util.*;

import static Application.Helper.createPreviewPane;

public class Controller {
    @FXML
    private Pane previewBox;
    @FXML
    private ScrollPane mainArea;

    @FXML
    private Button page1;
    @FXML
    private Button page2;
    @FXML
    private Button page3;
    @FXML
    private Button page4;
    @FXML
    private Button page5;

    List<Article> articles; // keep reference to change pages
    GetArticleListService service;
    private static final int PREVIEWS_PER_PAGE = 10;
    public void initialize(){
        page1.setDisable(true);
        page2.setDisable(true);
        page3.setDisable(true);
        page4.setDisable(true);
        page5.setDisable(true);

        System.out.println("system initialized!");
    }
    public void displayNews(ActionEvent e){
        Object o = e.getSource();
        if (o instanceof Button){
            Button b = (Button) o;
            if(service != null){
                service.cancel();
            }
            String category = b.getText();
            displayPreviews(category);
        }
    }

    private void displayPreviews(String category){
        loadArticles(category);
    }

    public void changePageBtn(ActionEvent e){
        Button b = (Button) e.getSource();
        b.getText();
        int pageNum = Integer.parseInt(b.getText());
        changePage(pageNum);
    }






    private void changePage(int pageNum){
        previewBox.getChildren().clear();
        int upperBound = PREVIEWS_PER_PAGE * pageNum;

        // TODO: fix cases when articles < 50
        if (upperBound < 50 && pageNum == 5){
            System.out.println(upperBound);
            upperBound = articles.size();
        }

        int lowerBound = (pageNum - 1) * PREVIEWS_PER_PAGE;
        // TODO: lag
        for(int i = lowerBound; i < upperBound; i++){
            previewBox.getChildren().add(createPreviewPane(articles.get(i)));
        }
        mainArea.setContent(previewBox);
    }


    private void loadArticles(String category){
        service = new GetArticleListService(category);

        // disable all page buttons;
        page1.setDisable(true);
        page2.setDisable(true);
        page3.setDisable(true);
        page4.setDisable(true);
        page5.setDisable(true);


        service.setOnSucceeded(e -> {
            // store 50 articles
            articles = (List<Article>) e.getSource().getValue();
            changePage(1);
            page1.setDisable(false);
            page2.setDisable(false);
            page3.setDisable(false);
            page4.setDisable(false);
            page5.setDisable(false);

        });


        ProgressIndicator p = new ProgressIndicator();
        p.setMaxSize(140, 140);
        p.setStyle(" -fx-progress-color: orange;");
        p.progressProperty().bind(service.progressProperty());
        p.visibleProperty().bind(service.runningProperty());

        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(p);

        mainArea.setContent(stackPane);
        service.start();
    }
}



