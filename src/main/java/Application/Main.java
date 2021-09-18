/*
  RMIT University Vietnam
  Course: INTE2512 Object-Oriented Programming
  Semester: 2021B
  Assessment: Final Project
  Created  date: 10/08/2021
  Author: Nguyen Quoc Hoang Trung, S3818328
  Last modified date: 18/09/2021
  Author: Nguyen Quoc Hoang Trung, S3818328
  Acknowledgement: No external knowledge
*/

package Application;

import Application.Controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("MainMenu-view.fxml"));
        Parent root = fxmlLoader.load();
        MainController mainController = fxmlLoader.getController();

        // create new scene in new variable
        Scene scene = new Scene(root, 1000, 800);
        mainController.setColumnConstraints(scene);

        primaryStage.setTitle("News Aggregator");
        primaryStage.setScene(scene);

        // close all stages when the main stage is closed
        primaryStage.setOnCloseRequest(e -> System.exit(0));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
