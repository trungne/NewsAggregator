/*
  RMIT University Vietnam
  Course: INTE2512 Object-Oriented Programming
  Semester: 2021B
  Assessment: Final Project
  Created  date: dd/mm/yyyy
  Author: Student name, Student ID
  Last modified date: dd/mm/yyyy
  Author: Student name, Student ID
  Acknowledgement: Thanks and give credits to the resources that you used in this file
*/

package Application.Controller;

import Application.Main;
import Application.Model.Model;
import Business.News.Article;
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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class MainController {
    private static final int MAX_PREVIEWS_PER_PAGE = 10;

    @FXML private GridPane mainGridPane;
    @FXML private VBox previewBox;
    @FXML private ScrollPane mainArea;
    @FXML private HBox pageBox;
    @FXML private ProgressBar progressBar;
    @FXML private Label progressLabel;

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

    private final List<Node> CATEGORY_BUTTONS = new ArrayList<>();


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

    public void setColumnConstraints(Scene scene){
        // bind mainGridPane prefWidth property with scene width property
        // and subtract by 200 (of the category buttons)
        mainGridPane.getColumnConstraints().get(1).prefWidthProperty().bind(scene.widthProperty().subtract(200));
    }

    private void loadArticleView(){
        FXMLLoader articleView = new FXMLLoader(Main.class.getResource("Article-view.fxml"));
        try {
            articleView.load();
            articleViewController = articleView.getController();
            articleViewController.setModel(model);
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

        loadAboutUsView();
        loadArticleView();

        CATEGORY_BUTTONS.addAll(List.of(newCategory, covidCategory, politicsCategory, businessCategory, technologyCategory,
                healthCategory, sportsCategory, entertainmentCategory, worldCategory, othersCategory));

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
        articleViewController.close();
        currentCategoryButton.fire();
    }

    public void refreshAll(){
        this.model.refresh();
        // automatically redirect to new category when refresh all
        articleViewController.close();
        newCategory.fire();
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
        disableAllChildButtons(CATEGORY_BUTTONS);
        disableAllChildButtons(pageBox.getChildren());
        enableIndicator();
        mainArea.setContent(null); // disable preview pane

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
        enableAllChildButtons(CATEGORY_BUTTONS);
        enableAllChildButtons(pageBox.getChildren());
        highlightPage(pageNum);
        disableIndicator();
        mainArea.setContent(previewBox);
        placePreviewsOnGrids(pageNum);
    }

    private void enableIndicator(){
        progressBar.setVisible(true);
        progressBar.progressProperty().bind(model.getService().progressProperty());
        progressBar.visibleProperty().bind(model.getService().runningProperty());
        progressLabel.textProperty().bind(model.getService().messageProperty());
        progressLabel.setVisible(true);
    }

    private void disableIndicator(){
        // unbind so that progress bar can be set invisible
        progressBar.progressProperty().unbind();
        progressBar.visibleProperty().unbind();
        progressBar.setVisible(false);
        progressLabel.textProperty().unbind();
        progressLabel.setVisible(false);
    }


    /** Put each article's contents in each of the created grid panes
     * @param page: current page clicked
     */
    private void placePreviewsOnGrids(int page){
        int startIndex = (page - 1) * MAX_PREVIEWS_PER_PAGE;
        String category = currentCategoryButton.getText();
        for (int i = 0; i < MAX_PREVIEWS_PER_PAGE; i++){

            int articleIndex = startIndex + i;

            Article a = model.getArticle(category, articleIndex);
            if (a == null) {
                continue;
            }
            String thumbnail = a.getThumbnail();
            String title = a.getTitle();
            String description = a.getDescription();
            String time = a.getRelativeTime();
            String source = a.getNewsSource();

            PreviewGrid previewGrid = (PreviewGrid) previewBox.getChildren().get(i);
            previewGrid.setPreviewToGrid(thumbnail, title, description, time, source);
        }
    }

    /** Open chosen article as a pop-up
     * @param index: index of the selected article in the system
     */
    private void openArticleInNewStage(int index){
        Article a = model.getArticleAndStore(currentCategoryButton.getText(), index);
        String title = a.getTitle();
        String html = a.getHtml();
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
     * @param nodes: the container pane
     * */
    private void disableAllChildButtons(Collection<Node> nodes){
        for (Node node: nodes){
            node.setDisable(true);
        }
    }

    /**
     * Target the container pane and make all buttons clickable when finish loading
     * @param nodes: the container pane
     * */
    private void enableAllChildButtons(Collection<Node> nodes){
        for (Node node: nodes){
            node.setDisable(false);
        }
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

                // calculate the actual index of a grid.
                // Ex: The first grid on page 1 is:
                // (1 - 1) * 10 + 0 = 0
                // Ex: The second grid on page 2:
                // (2 - 1) * 10 + 1 = 11
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
        private static final Font TITLE_FONT = new Font("Helvetica", 20);
        private static final Font DESCRIPTION_FONT = new Font("Helvetica", 12);

        private boolean isUnderlined = false;
        private final ImageView IMAGE_VIEW = new ImageView();
        private final Text TITLE_TEXT = new Text();
        private final Text DESCRIPTION_TEXT = new Text();
        private final Text PUBLISHED_TIME_TEXT = new Text();
        private final Text NEWS_SOURCE_TEXT = new Text();

        // method to get wrapping property of titleText
        public DoubleProperty titleWrappingWidthProperty() {
            return TITLE_TEXT.wrappingWidthProperty();
        }

        public PreviewGrid(){
            TITLE_TEXT.setFont(TITLE_FONT);
            DESCRIPTION_TEXT.setFont(DESCRIPTION_FONT);
            DESCRIPTION_TEXT.wrappingWidthProperty().bind(TITLE_TEXT.wrappingWidthProperty().subtract(100));

            // arrange components in grid to create layout for preview components
            this.add(IMAGE_VIEW, 1, 1, 1, 2);
            this.add(TITLE_TEXT, 2, 1);
            this.add(DESCRIPTION_TEXT, 2, 2);
            this.add(PUBLISHED_TIME_TEXT, 2, 3);
            this.add(NEWS_SOURCE_TEXT, 1, 3);
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
                this.IMAGE_VIEW.setImage(image);
            } catch (IllegalArgumentException e){
                System.err.println(thumbnail);
            }

            this.TITLE_TEXT.setText(title);
            this.DESCRIPTION_TEXT.setText(description);
            this.PUBLISHED_TIME_TEXT.setText(publishedTime);
            this.NEWS_SOURCE_TEXT.setText(source);
        }

        public void underline(){
            if (!isUnderlined){
                TITLE_TEXT.setUnderline(true);
                DESCRIPTION_TEXT.setUnderline(true);
                PUBLISHED_TIME_TEXT.setUnderline(true);
                NEWS_SOURCE_TEXT.setUnderline(true);
            }
            else{
                TITLE_TEXT.setUnderline(false);
                DESCRIPTION_TEXT.setUnderline(false);
                PUBLISHED_TIME_TEXT.setUnderline(false);
                NEWS_SOURCE_TEXT.setUnderline(false);
            }

            isUnderlined = !isUnderlined;
        }
    }
}



