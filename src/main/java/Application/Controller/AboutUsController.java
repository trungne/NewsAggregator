/*
  RMIT University Vietnam
  Course: INTE2512 Object-Oriented Programming
  Semester: 2021B
  Assessment: Final Project
  Created  date: dd/mm/yyyy
  Author: Student name, Student ID
  Last modified date: dd/mm/yyyy
  Author: Student name, Student ID
  Acknowledgement: Thanks and give credits to the resources that you used in this file
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
