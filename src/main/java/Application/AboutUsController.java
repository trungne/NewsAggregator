package Application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;



public class AboutUsController {
    @FXML private AnchorPane aboutUsPane;
    private final Stage stage = new Stage();

    public void initialize(){
        Scene scene = new Scene(aboutUsPane);
        stage.setTitle("About Us");
        stage.setScene(scene);
        stage.setResizable(false);
    }
    public void show() {
        stage.show();
    }
    public void close(ActionEvent e){
        Node node = (Node) e.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.hide();
    }
}
