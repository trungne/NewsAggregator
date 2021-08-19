module JavaFxApplication {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires javafx.graphics;

    requires org.jsoup;
    requires selenium.chrome.driver;
    requires io.github.bonigarcia.webdrivermanager;
    requires selenium.api;
    requires org.apache.commons.lang3;

    exports Application;
    exports business.News;

    opens Application to javafx.fxml;
    opens Application.View to javafx.fxml;
    opens Application.Model to javafx.fxml;

    exports Application.View;
    exports Application.Model;

}