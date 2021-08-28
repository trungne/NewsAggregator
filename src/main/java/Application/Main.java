package Application;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.Objects;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root;
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/MainMenu.fxml")));

        // create new scene in new variable
        Scene scene = new Scene(root, 1000, 800);
        // grab mainGridPane out of elements tree
        GridPane gp = (GridPane) root.getChildrenUnmodifiable().get(0);
        // bind mainGridPane prefWidth property with scene width property
        // and subtract by 200 (of the category buttons)
        gp.getColumnConstraints().get(1).prefWidthProperty().bind(scene.widthProperty().subtract(200));

        primaryStage.setTitle("News Aggregator");
        primaryStage.setScene(scene);

//         close all stages when the main stage is closed
        primaryStage.setOnCloseRequest(e ->{
            Platform.exit();
        });
        primaryStage.show();
    }

    @Override
    public void stop(){
        Platform.exit();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
