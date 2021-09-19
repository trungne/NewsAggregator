module JavaFxApplication {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires javafx.graphics;

    requires org.jsoup;
    requires org.apache.commons.lang3;

    opens Application to javafx.fxml;
    opens Application.Model to javafx.fxml;
    opens Application.Controller to javafx.fxml;

    exports Business.Scraper.Sanitizer;
    exports Business;
    exports Application.Controller;
    exports Application;
    exports Business.News;
    exports Application.Model;
    exports Business.Scraper.ArticleCrawler;
    exports Business.Scraper.LinksCrawler;
}