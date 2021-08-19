package Application;

import Application.Model.Model;
import Application.View.PreviewGrid;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;

import business.News.Article;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
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
        progressBar.progressProperty().bind(model.getService().progressProperty());
        progressBar.visibleProperty().bind(model.getService().runningProperty());
    }

    public void initialize(){
        for (int i = 0; i < MAX_PREVIEWS_PER_PAGE; i++){
            PreviewGrid grid = new PreviewGrid();

            grid.setOnMouseClicked(event -> {
                Node node = (Node) event.getSource();
                displayArticle( (String) node.getUserData());
            });
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

        Button newCategory = (Button) categoryBox.getChildren().get(0);
        newCategory.fire();
    }

    public void displayNews(ActionEvent e){
        Object o = e.getSource();
        if (o instanceof Button){
            Button b = (Button) o;
            setCurrentCategoryButton(b);
            String category = b.getText();
            getPreviews(category);
        }
    }

    private void getPreviews(String category){
        disableAllPageButtons();
        disableAllCategoryButtons();
        displayProgressBar();
        model.loadArticles(category);
    }

    public void displayPreviews(int pageNum){
        enableAllPageButtons();
        enableAllCategoryButtons();
        List<Article> articles = model.getArticles(pageNum);
        clearAllArticleGrids();
        populatePreviewGrids(articles);
        mainArea.setContent(previewBox);
    }

    private void populatePreviewGrids(List<Article> articles){
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
        setCurrentPageButton(pageNum);
        displayPreviews(pageNum);
    }

    public void displayProgressBar(){
        mainArea.setContent(progressBar);
    }

    public void displayArticle(String html){
        articlePane.getChildren().clear();
        browser.getEngine().loadContent(html);
        articleStage.show();
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
        currentPageButton = (Button) pageBox.getChildren().get(page-1);
        currentPageButton.setStyle("-fx-background-color: rgb(255,255,102);");
    }
    public void disableAllCategoryButtons(){
        for(Node node: categoryBox.getChildren()){
            node.setDisable(true);
        }
    }

    public void enableAllCategoryButtons(){
        for(Node node: categoryBox.getChildren()){
            node.setDisable(false);
        }
    }

    public void disableAllPageButtons(){
        for(Node node: pageBox.getChildren()){
            node.setDisable(true);
        }
    }

    public void enableAllPageButtons(){
        for(Node node: pageBox.getChildren()){
            node.setDisable(false);
        }
    }
}



