package Application;

import Application.Model.Model;
import Application.View.PreviewGrid;
import business.News.Article;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.util.*;


public class Controller {
    // TODO: Andrew comments this
    @FXML public GridPane mainGridPane;
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
    public Button currentPageButton;

    /** Controller constructor
     */
    public Controller(){
        this.model = new Model(this);
        progressBar.progressProperty().bind(model.getService().progressProperty());
        progressBar.visibleProperty().bind(model.getService().runningProperty());
    }

    /** Generate general layout and inject contents, with necessary event handlers and sizing bindings
     */
    public void initialize() {
        // dynamically create grid pane inside scroll pane
        for (int i = 0; i < MAX_PREVIEWS_PER_PAGE; i++){
            PreviewGrid grid = new PreviewGrid();
            grid.setOnMouseEntered(e -> grid.underline());
            grid.setOnMouseExited(e -> grid.underline());
            grid.setOnMouseClicked(e -> {
                // do nothing if no category or page has been selected
                if (currentCategoryButton == null || currentPageButton == null){
                    return;
                }

                GridPane node = (GridPane) e.getSource();
                int index = (Integer.parseInt(currentPageButton.getText()) - 1) * 10
                        + previewBox.getChildren().indexOf(node);
                openArticleInNewStage(index);
            });

            // bind title's wrapping property in each grid with mainGridPane prefWidth property
            // and subtract 160px ~ 200px (of the thumbnail in each grid)
            grid.titleWrappingWidthProperty().bind(
                    mainGridPane.getColumnConstraints().get(1).prefWidthProperty().subtract(200)
            );
            this.previewGrids.add(grid);
            this.previewBox.getChildren().add(grid);
        }

        articlePane.getChildren().add(browser);
        articleStage.setScene(articleScene);
        articleStage.setOnCloseRequest(event ->
                browser.getEngine().load(null));

        progressBar.setPrefSize(500, 30);

        Button newCategory = (Button) categoryBox.getChildren().get(0);
        newCategory.fire();
    }

    /** Handle the event when a category is clicked
     * @param e: on-click event
     */
    public void selectCategory(ActionEvent e){
        Button b = (Button) e.getSource();
        // do nothing if the category has already been selected
        if (b == currentCategoryButton) {
            return;
        }
        highlightCategory(b);
        requestPreviews(b.getText());
    }

    /** Handle the event when a page is clicked
     * @param e: on-click event
     */
    public void changePage(ActionEvent e){
        Button b = (Button) e.getSource();
        if (b == currentPageButton){
            return;
        }

        int pageNum = Integer.parseInt(b.getText());
        updatePreviewsPane(pageNum);
    }

    /** Send request of scrapping a particular category
     * @param category: category's name
     */
    private void requestPreviews(String category){
        disableAllChildButtons(categoryBox);
        disableAllChildButtons(pageBox);
        mainArea.setContent(this.progressBar);

        // this will trigger model to scrape articles
        // when finished, the model will trigger controller to display previews
        model.loadArticles(category);
    }

    /** Update the experience on preview pane after scrapping is finished
     */
    public void updatePreviewsPane(){
        enableAllChildButtons(categoryBox);
        enableAllChildButtons(pageBox);

        // By default, page 1 is selected after scraping finishes
        updatePreviewsPane(1);
    }

    /** Update preview pane's content when pagination is used
     * @param pageNum: pagination index
     */
    private void updatePreviewsPane(int pageNum){
        highlightPage(pageNum);
        List<Article> articles = getArticleSublist(pageNum);
        placePreviewsOnGrids(articles);
        mainArea.setContent(previewBox);
    }

    /** Extract a block of articles according to pagination index
     * @param page: pagination index
     */
    private List<Article> getArticleSublist(int page){
        // generate the start and end indices
        int startIndex = (page - 1) * MAX_PREVIEWS_PER_PAGE;
        int endIndex = page * MAX_PREVIEWS_PER_PAGE;
        return model.getArticleSublist(currentCategoryButton.getText(), startIndex, endIndex);
    }

    /** Put each article's contents in each of the created grid panes
     * @param articles: list of scrapped articles
     */
    private void placePreviewsOnGrids(List<Article> articles){
        for (int i = 0; i < MAX_PREVIEWS_PER_PAGE; i++){
            Article a = articles.get(i);
            String thumbnail = a.getThumbNail();
            String title = a.getTitle();
            String description = a.getDescription();
            String publishedTime = a.getRelativeTime();
            String source = a.getNewsSource();

            // this is used to retrieve article in model later when a preview is clicked
            previewGrids.get(i).setPreviewToGrid(thumbnail, title,
                                                description, publishedTime,
                                                source);
        }
    }

    /** Open chosen article as a pop-up
     * @param index: index of the selected article in the system
     */
    private void openArticleInNewStage(int index){
        Article content = model.getArticleContent(currentCategoryButton.getText(), index);
        // TODO: turn this off when done testing
        System.out.println(content.getHtml());
        browser.getEngine().loadContent(content.getHtml());
        articleStage.setTitle(content.getTitle());

        articleStage.show();
        articleStage.requestFocus();
    }

    /** Update the currentCategoryButton and highlight only the current category user is on
     * @param b: the category button
     */
    private void highlightCategory(Button b){
        if (currentCategoryButton != null){
            currentCategoryButton.setStyle(null);
        }
        currentCategoryButton = b;
        currentCategoryButton.setStyle("-fx-background-color: rgb(255,255,102);");
    }

    /** Update the currentPageButton and highlight only the current page user is on
     * @param page: the index of the page
     */
    private void highlightPage(int page){
        if (page <= 0 || page > pageBox.getChildren().size()){
            throw new IllegalArgumentException();
        }
        if(currentPageButton != null){
            currentPageButton.setStyle(null);
        }
        currentPageButton = (Button) pageBox.getChildren().get(page-1);
        currentPageButton.setStyle("-fx-background-color: rgb(255,255,102);");
    }

    /**
     * Target the container pane and make all buttons NOT clickable while still loading
     * @param parent: the container pane
     * */
    private void disableAllChildButtons(Pane parent){
        for (Node node: parent.getChildren())
            node.setDisable(true);
    }

    /**
     * Target the container pane and make all buttons clickable when finish loading
     * @param parent: the container pane
     * */
    private void enableAllChildButtons(Pane parent){
        for (Node node: parent.getChildren())
            node.setDisable(false);
    }
}



