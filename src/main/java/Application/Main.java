package Application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root;
        // TODO: add loading screen here
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/MainMenu.fxml")));

        primaryStage.setTitle("News Aggregator");
        primaryStage.setScene(new Scene(root, 720, 600));

        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
