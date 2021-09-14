/*
  RMIT University Vietnam
  Course: INTE2512 Object-Oriented Programming
  Semester: 2021B
  Assessment: Final Project
  Created  date: 27/08/2021
  Author: Nguyen Bao Khang, s3817970
  Last modified date: 14/09/2021
  Acknowledgements:
*/

package Application.Controller;

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
