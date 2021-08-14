package Application;
import business.GetArticleListService;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import business.News.Article;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

import java.util.*;

import static Application.Helper.displayArticleInGrid;

public class Controller {
    @FXML private Pane previewBox;
    @FXML private ScrollPane mainArea;

    // change page buttons
    @FXML private Button page1;
    @FXML private Button page2;
    @FXML private Button page3;
    @FXML private Button page4;
    @FXML private Button page5;

    private final GridPane article1 = new GridPane();
    private final GridPane article2 = new GridPane();
    private final GridPane article3 = new GridPane();
    private final GridPane article4 = new GridPane();
    private final GridPane article5 = new GridPane();
    private final GridPane article6 = new GridPane();
    private final GridPane article7 = new GridPane();
    private final GridPane article8 = new GridPane();
    private final GridPane article9 = new GridPane();
    private final GridPane article10 = new GridPane();
    GridPane[] articleGridPanes = new GridPane[10];

    // category buttons
    @FXML private Button newCategory;
    @FXML private Button covidCategory;
    @FXML private Button politicsCategory;
    @FXML private Button businessCategory;
    @FXML private Button technologyCategory;
    @FXML private Button healthCategory;
    @FXML private Button sportsCategory;
    @FXML private Button entertainmentCategory;
    @FXML private Button worldCategory;
    @FXML private Button othersCategory;

    List<Article> articles; // keep reference to change pages
    GetArticleListService service;
    Button currentClickedButton;

    public void initialize(){
        disableAllPageButtons();
        articleGridPanes = new GridPane[]{article1, article2, article3, article4,
                article5, article6, article7, article8, article9, article10};
        for (GridPane pane: articleGridPanes){
            previewBox.getChildren().add(pane);
        }
    }
    public void displayNews(ActionEvent e){
        Object o = e.getSource();
        if (o instanceof Button){
            currentClickedButton = (Button) o;
            if(service != null){
                service.cancel();
            }
            currentClickedButton.setStyle(
                    "-fx-background-color: rgb(255,255,102);"
            );
            String category = currentClickedButton.getText();
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
        int size = articleGridPanes.length;
        int upperBound = size * pageNum;
        int lowerBound = (pageNum - 1) * size;

        List<Article> currentArticles = new ArrayList<>();
        for(int i = lowerBound; i < upperBound; i++){
            currentArticles.add(articles.get(i));
        }

        for(int i = 0; i < currentArticles.size(); i++){
            displayArticleInGrid(currentArticles.get(i), articleGridPanes[i]);
        }

        mainArea.setContent(previewBox);
    }


    private void loadArticles(String category){
        service = new GetArticleListService(category);

        disableAllPageButtons();
        disableAllCategoryButtons();

        service.setOnSucceeded(e -> {
            // store 50 articles
            articles = (List<Article>) e.getSource().getValue();
            changePage(1);
            enableAllPageButtons();
            enableAllCategoryButtons();
            currentClickedButton.setStyle(null);
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

    private void disableAllCategoryButtons(){
        newCategory.setDisable(true);
        covidCategory.setDisable(true);
        politicsCategory.setDisable(true);
        businessCategory.setDisable(true);
        technologyCategory.setDisable(true);
        healthCategory.setDisable(true);
        sportsCategory.setDisable(true);
        entertainmentCategory.setDisable(true);
        worldCategory.setDisable(true);
        othersCategory.setDisable(true);
    }

    private void enableAllCategoryButtons(){
        newCategory.setDisable(false);
        covidCategory.setDisable(false);
        politicsCategory.setDisable(false);
        businessCategory.setDisable(false);
        technologyCategory.setDisable(false);
        healthCategory.setDisable(false);
        sportsCategory.setDisable(false);
        entertainmentCategory.setDisable(false);
        worldCategory.setDisable(false);
        othersCategory.setDisable(false);
    }

    private void disableAllPageButtons(){
        page1.setDisable(true);
        page2.setDisable(true);
        page3.setDisable(true);
        page4.setDisable(true);
        page5.setDisable(true);
    }

    private void enableAllPageButtons(){
        page1.setDisable(false);
        page2.setDisable(false);
        page3.setDisable(false);
        page4.setDisable(false);
        page5.setDisable(false);
    }

}



