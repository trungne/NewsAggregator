package Application.Controller;

import Application.Model.Model;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;

public class ArticleViewController {
    @FXML WebView webView;
    @FXML AnchorPane anchorPane;
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
    }

    public void close(){
        webView.getEngine().loadContent("");
        stage.close();
    }



}
