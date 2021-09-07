package Application;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class AboutUsController {
    @FXML private AnchorPane aboutUsPane;
    private final Stage stage = new Stage();

    public void initialize(){
        stage.setTitle("About Us");
        stage.setScene(new Scene(aboutUsPane));
        stage.setResizable(false);
    }
    public void show() {
        stage.show();
    }
    public void close(){
        stage.close();
    }
}
