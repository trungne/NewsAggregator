package Application;
import business.GetArticleListService;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
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
    List<GridPane> articleGridPanes = new ArrayList<>();
    GetArticleListService service;

    Button currentCategoryButton;
    Button currentPageButton;
    static final int MAX_PREVIEWS_PER_PAGE = 10;

    public void initialize(){
        for (int i = 0; i < MAX_PREVIEWS_PER_PAGE; i++){
            GridPane grid = new GridPane();
            grid.addEventHandler(MouseEvent.MOUSE_RELEASED, new Helper.OpenArticle());
            grid.addEventHandler(MouseEvent.MOUSE_ENTERED, new Helper.UnderlineText());
            grid.addEventHandler(MouseEvent.MOUSE_EXITED, new Helper.UndoUnderlineText());
            articleGridPanes.add(grid);
            previewBox.getChildren().add(grid);
        }
        // select new category as a default category when open app
        newCategory.fire();
    }
    public void displayNews(ActionEvent e){
        Object o = e.getSource();
        if (o instanceof Button){
            Button b = (Button) o;

            if(service != null){
                service.cancel();
            }

            if(currentCategoryButton != null){
                // do nothing if the same category is clicked
                if(currentCategoryButton.getText().equals(b.getText())){
                    return;
                }
                currentCategoryButton.setStyle(null);
            }

            currentCategoryButton = b;
            currentCategoryButton.setStyle(
                    "-fx-background-color: rgb(255,255,102);"
            );
            String category = currentCategoryButton.getText();
            displayPreviews(category);
        }
    }

    private void displayPreviews(String category){
        disableAllPageButtons();
        disableAllCategoryButtons();

        loadArticles(category);
    }

    public void changePageBtn(ActionEvent e){
        if(currentPageButton != null){
            currentPageButton.setStyle(null);
        }
        currentPageButton = (Button) e.getSource();
        currentPageButton.getText();
        int pageNum = Integer.parseInt(currentPageButton.getText());
        changePage(pageNum);
    }

    private void changePage(int pageNum){
        currentPageButton.setStyle(
                "-fx-background-color: rgb(255,255,102);"
        );
        int upperBound = MAX_PREVIEWS_PER_PAGE * pageNum;
        int lowerBound = (pageNum - 1) * MAX_PREVIEWS_PER_PAGE;

        List<Article> currentArticles = new ArrayList<>();
        for(int i = lowerBound; i < upperBound; i++){
            if (articles.get(i) == null){
                break;
            }
            currentArticles.add(articles.get(i));
        }

        clearAllArticleGrids();

        for(int i = 0; i < currentArticles.size(); i++){
            displayArticleInGrid(currentArticles.get(i), articleGridPanes.get(i));
        }

        mainArea.setContent(previewBox);
    }


    private void loadArticles(String category){
        service = new GetArticleListService(category);

        service.setOnSucceeded(e -> {
            // store 50 articles
            articles = (List<Article>) e.getSource().getValue();
            if (currentPageButton != null){
                currentPageButton.setStyle(null);
            }
            currentPageButton = page1;
            changePage(1);
            enableAllPageButtons();
            enableAllCategoryButtons();
        });

        ProgressBar p = new ProgressBar();
        p.setPrefSize(500, 50);
        p.setStyle(" -fx-progress-color: orange;");
        p.progressProperty().bind(service.progressProperty());

        p.visibleProperty().bind(service.runningProperty());

        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(p);

        mainArea.setContent(stackPane);
        service.start();
    }

    private void clearAllArticleGrids(){
        for(GridPane grid: articleGridPanes){
            if (!grid.getChildren().isEmpty()){
                grid.getChildren().clear();
            }
        }
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



