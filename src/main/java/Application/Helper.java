package Application;

import business.News.Article;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Helper {
    private static final WebView browser = new WebView();
    private static final WebEngine webEngine = browser.getEngine();
    private static final Pane articlePane = new Pane();
    private static final Scene articleScene = new Scene(articlePane);
    private static final Stage articleStage = new Stage();
    static {
        articleStage.setScene(articleScene);
    }

    static class OpenArticle implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent mouseEvent) {
            Node p = (Node) mouseEvent.getSource();
            Article a = (Article) p.getUserData();
            String content = a.getHtml();

            articlePane.getChildren().clear();
            articlePane.getChildren().add(browser);

            webEngine.loadContent(content);
            articleStage.setOnCloseRequest(event -> {
                browser.getEngine().load(null);
            });
            articleStage.show();
        }
    }
    static void displayArticleInGrid(Article article, GridPane grid){
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

class ArticleDisplayer{

}