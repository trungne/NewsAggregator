package Application.Controller;

import Application.Model.Model;
import Business.News.Article;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;

public class ArticleViewController {
    @FXML WebView webView;
    @FXML AnchorPane anchorPane;
    @FXML Button nextButton;
    @FXML Button previousButton;
    Stage stage = new Stage();
    Model model;

    public void initialize(){
        stage.setScene(new Scene(anchorPane));
        stage.setOnCloseRequest(e -> webView.getEngine().loadContent(""));
    }

    public void setModel(Model model){
        this.model = model;
    }

    public void show(String title, String html){
        if (StringUtils.isEmpty(html)){
            return;
        }
        webView.getEngine().loadContent(html);
        stage.setTitle(title);
        stage.requestFocus();
        stage.show();

        checkPreviousAndNextButtons();
    }

    public void close(){
        webView.getEngine().loadContent("");
        stage.close();
    }

    public void next(){
        Article a = model.nextArticle();
        if (a == null){
            return;
        }
        show(a.getTitle(), a.getHtml());
    }

    public void previous(){
        Article a = model.previousArticle();
        if (a == null){
            return;
        }
        show(a.getTitle(), a.getHtml());
    }

    private void checkPreviousAndNextButtons(){
        nextButton.setDisable(model.hasNoNextArticle());
        previousButton.setDisable(model.hasNoPreviousArticle());
    }
}
