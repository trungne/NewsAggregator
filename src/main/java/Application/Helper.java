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

public class Helper {
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

    static class OpenArticle implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent mouseEvent) {
            Node p = (Node) mouseEvent.getSource();
            Article a = (Article) p.getUserData();

            WebView browser = new WebView();
            WebEngine webEngine = browser.getEngine();
            //TODO: add css style
            String css = "html {width: 100%;height: 100%;margin: 0 auto;overflow-x: hidden;}\n" +
                    "body {margin: 30px;}\n" +
                    ".article-header{display: flex; font-style: italic;}\n" +
                    ".article-category{margin-right: .75rem;color: #007bff;}\n" +
                    ".published-time{color:#6c757d;}\n" +
                    ".article-content h1 + p{font-weight: bold;}\n" +
                    ".article-content p {text-align: justify;}\n" +
                    "img {width: 350px;height:250px;} article {padding: 0 4em 2em 4em;margin-right: auto;margin-left: auto;}\n" +
                    ".content-pic {text-align: center; margin-top: 1em; margin-bottom: 1em;}\n" +
                    "figcaption em {color:#6c757d;font-style: italic;font-size: 14px;}\n" +
                    ".content-video {width: 600px;height:400px;}\n";
            a.setStyle(css);
            String content = a.getHtml();
            System.out.println(content);
            webEngine.loadContent(content);
            Scene articleScene = new Scene(browser);
            Stage stage = new Stage();
            stage.setScene(articleScene);
            stage.show();
        }
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
