module JavaFxApplication {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.jsoup;
    requires selenium.chrome.driver;
    requires io.github.bonigarcia.webdrivermanager;
    requires selenium.api;

    opens Application to javafx.fxml;
    exports Application;

}