package Application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;

public class View {
    @FXML
    private Pane previewBox;
    @FXML private ScrollPane mainArea;

    // change page buttons
    @FXML private Button page1;
    @FXML private Button page2;
    @FXML private Button page3;
    @FXML private Button page4;
    @FXML private Button page5;

    // category buttons
    @FXML private Button newCategory;
    @FXML private Button covidCategory;
    @FXML private Button politicsCategory;
    @FXML private Button businessCategory;
    @FXML private Button technologyCategory;
    @FXML private Button healthCategory;
    @FXML private Button sportsCategory;
    @FXML private Button entertainmentCategory;
    @FXML private Button worldCategory;
    @FXML private Button othersCategory;


}
