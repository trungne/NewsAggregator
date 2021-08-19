package Application;

import Application.Model.Model;
import Application.View.PreviewGrid;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;

import business.News.Article;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.util.*;


public class Controller {
    @FXML private VBox previewBox;
    @FXML private ScrollPane mainArea;
    @FXML private HBox pageBox;
    @FXML private VBox categoryBox;
    private final Model model;
    public static final int MAX_PREVIEWS_PER_PAGE = 10;

    List<PreviewGrid> previewGrids = new ArrayList<>();
    ProgressBar progressBar = new ProgressBar();
    private final WebView browser = new WebView();
    private final Pane articlePane = new Pane();
    private final Scene articleScene = new Scene(articlePane);
    private final Stage articleStage = new Stage();

    private Button currentCategoryButton;
    private Button currentPageButton;

    public Controller(){
        this.model = new Model(this);
        progressBar.progressProperty().bind(this.model.getService().progressProperty());
        progressBar.visibleProperty().bind(this.model.getService().runningProperty());
    }
    public void initialize(){
        for (int i = 0; i < MAX_PREVIEWS_PER_PAGE; i++){
            PreviewGrid grid = new PreviewGrid();
            this.previewGrids.add(grid);
            this.previewBox.getChildren().add(grid);
        }
        articlePane.getChildren().add(browser);
        articleStage.setScene(articleScene);
        articleStage.setOnCloseRequest(event -> {
            browser.getEngine().load(null);
        });

        progressBar.setPrefSize(500, 50);
        progressBar.setStyle(" -fx-progress-color: orange;");

    }
    public void displayNews(ActionEvent e){
        Object o = e.getSource();
        if (o instanceof Button){
            Button b = (Button) o;
            currentCategoryButton = b;
            String category = b.getText();
            getPreviews(category);
        }
    }

    public void clearAllArticleGrids(){
        for(GridPane grid: previewGrids){
            if (!grid.getChildren().isEmpty()){
                grid.getChildren().clear();
            }
        }
    }

    public void changePage(ActionEvent e){
        Button b = (Button) e.getSource();
        int pageNum = Integer.parseInt(b.getText());
        currentPageButton = b;
        displayPreviews(pageNum);
    }

    public void displayProgressBar(){
        mainArea.setContent(progressBar);
    }

    private void getPreviews(String category){
        this.model.loadArticles(category);
        disableAllPageButtons();
        disableAllCategoryButtons();
        displayProgressBar();
    }

    public void displayPreviews(int pageNum){
        List<Article> articles = model.getArticles(pageNum);
        setCurrentPageButton(pageNum);

        // clear all previous displayed previews
        clearAllArticleGrids();

        // display article to each grid in view
        for (int i = 0; i < MAX_PREVIEWS_PER_PAGE; i++){
            Article a = articles.get(i);
            String thumbnail = a.getThumbNail();
            String title = a.getTitle();
            String description = a.getDescription();
            String publishedTime = a.getRelativeTime();
            String source = a.getNewsSource();
            String articleHtml = a.getHtml();
            previewGrids.get(i).setPreviewToGrid(thumbnail, title, description, publishedTime, source, articleHtml);
        }

        displayPreviews();
    }

    public void displayArticle(String html){
        articlePane.getChildren().clear();
        browser.getEngine().loadContent(html);
        articleStage.show();
    }
    public void displayPreviews(){
        mainArea.setContent(previewBox);
    }
    public void setCurrentCategoryButton(Button b){
        if (currentCategoryButton != null){
            currentCategoryButton.setStyle(null);
        }
        currentCategoryButton = b;
        currentCategoryButton.setStyle("-fx-background-color: rgb(255,255,102);");
    }
    public void setCurrentPageButton(int page){
        if (page <= 0 || page > 5){
            throw new IllegalArgumentException();
        }
        if(currentPageButton != null){
            currentPageButton.setStyle(null);
        }
        currentPageButton = (Button) pageBox.getChildren().get(page);
        currentPageButton.setStyle("-fx-background-color: rgb(255,255,102);");
    }
    public void disableAllCategoryButtons(){
        for(Node node: categoryBox.getChildren()){
            node.setDisable(false);
        }
    }

    public void enableAllCategoryButtons(){
        for(Node node: categoryBox.getChildren()){
            node.setDisable(true);
        }
    }

    public void disableAllPageButtons(){
        for(Node node: pageBox.getChildren()){
            node.setDisable(false);
        }
    }

    public void enableAllPageButtons(){
        for(Node node: pageBox.getChildren()){
            node.setDisable(true);
        }
    }
}



