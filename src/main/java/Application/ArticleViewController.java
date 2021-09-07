package Application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;

public class ArticleViewController {
    @FXML WebView webView;
    @FXML AnchorPane anchorPane;
    Stage stage = new Stage();

    public void initialize(){
        Scene scene = new Scene(anchorPane);
        stage.setScene(scene);
        // TODO: still plays sound when closing windows
        stage.setOnCloseRequest(e -> webView.getEngine().load(null));
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

    public void close(ActionEvent e){
        Node node = (Node) e.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.setOnCloseRequest(event -> webView.getEngine().load(null));
        stage.close();
    }



}
