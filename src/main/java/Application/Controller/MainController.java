package Application.Controller;

import Application.Main;
import Application.Model.Model;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;


public class MainController {
    private static final int MAX_PREVIEWS_PER_PAGE = 10;

    @FXML private GridPane mainGridPane;
    @FXML private VBox previewBox;
    @FXML private ScrollPane mainArea;
    @FXML private AnchorPane anchorPane;
    @FXML private HBox pageBox;
    @FXML private VBox categoryBox;
    @FXML private ProgressBar progressBar;

    // controllers
    private AboutUsController aboutUsController;
    private ArticleViewController articleViewController;

    private final Model model;

    private Button currentCategoryButton;
    private Button currentPageButton;

    /** MainController constructor
     */
    public MainController(){
        this.model = new Model(this);
    }

    private void loadArticleView(){
        FXMLLoader articleView = new FXMLLoader(Main.class.getResource("Article-view.fxml"));
        try {
            articleView.load();
            articleViewController = articleView.getController();
        } catch (IOException | IllegalStateException e) {
            displayPopUpError("Error! Cannot load 'Article-view.fxml'!");
        }
    }
    private void loadAboutUsView(){
        FXMLLoader aboutUsViewLoader = new FXMLLoader(Main.class.getResource("AboutUs-view.fxml"));
        try {
            aboutUsViewLoader.load();
            aboutUsController = aboutUsViewLoader.getController();
        } catch (IOException | IllegalStateException e) {
            displayPopUpError("Error! Cannot load 'AboutUs-view.fxml' view!");
        }
    }

    private void displayPopUpError(String msg){
        Label label = new Label(msg);
        Scene scene = new Scene(label);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }


    /** Generate general layout and inject contents, with necessary event handlers and sizing bindings
     */
    public void initialize() {
        // dynamically create grid pane inside scroll pane
        createPreviewGrids(this.previewBox);

        progressBar.progressProperty().bind(model.getService().progressProperty());
        progressBar.visibleProperty().bind(model.getService().runningProperty());

        progressBar.setPrefSize(anchorPane.getPrefWidth(), 30);

        loadAboutUsView();
        loadArticleView();
        Button newCategory = (Button) categoryBox.getChildren().get(0);
        newCategory.fire();
    }

    /** Handle the event when a category is clicked
     * @param e: on-click event
     */
    public void selectCategory(ActionEvent e){
        Button b = (Button) e.getSource();
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

    public void refresh(){
        this.model.refresh(currentCategoryButton.getText());
        currentCategoryButton.fire();
    }

    public void refreshAll(){
        this.model.refresh();
        // automatically redirect to new category when refresh all
        ((Button) categoryBox.getChildren().get(0)).fire();
    }

    public void close(){
        System.exit(0);
    }

    public void aboutUs() {
        if (aboutUsController != null){
            aboutUsController.show();
        }
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
        // By default, page 1 is selected after scraping finishes
        updatePreviewsPane(1);
    }

    /** Update preview pane's content when pagination is used
     * @param pageNum: pagination index
     */
    private void updatePreviewsPane(int pageNum){
        enableAllChildButtons(categoryBox);
        enableAllChildButtons(pageBox);

        highlightPage(pageNum);
        placePreviewsOnGrids(pageNum);
        mainArea.setContent(previewBox);
    }


    /** Put each article's contents in each of the created grid panes
     * @param page: current page clicked
     */
    private void placePreviewsOnGrids(int page){
        int startIndex = (page - 1) * MAX_PREVIEWS_PER_PAGE;
        String category = currentCategoryButton.getText();
        for (int i = 0; i < MAX_PREVIEWS_PER_PAGE; i++){
            int articleIndex = startIndex + i;

            String thumbnail = model.getArticleThumbnail(category, articleIndex);
            String title = model.getArticleTitle(category, articleIndex);
            String description = model.getArticleDescription(category, articleIndex);
            String time = model.getArticleTime(category, articleIndex);
            String source = model.getArticleSource(category, articleIndex);

            PreviewGrid previewGrid = (PreviewGrid) previewBox.getChildren().get(i);
            previewGrid.setPreviewToGrid(thumbnail, title, description, time, source);
        }
    }

    /** Open chosen article as a pop-up
     * @param index: index of the selected article in the system
     */
    private void openArticleInNewStage(int index){
        String title = model.getArticleTitle(currentCategoryButton.getText(), index);
        String html = model.getArticleHtml(currentCategoryButton.getText(), index);
//        System.out.println(html);

        articleViewController.show(title, html);
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

    private void createPreviewGrids(Pane pane){
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
            pane.getChildren().add(grid);
        }
    }

    static class PreviewGrid extends GridPane {
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
}



