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

import Application.Model.Model;
import Business.News.Article;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;

public class ArticleViewController {
    @FXML private WebView webView;
    @FXML private AnchorPane anchorPane;
    @FXML private Button nextButton;
    @FXML private Button previousButton;
    private final Stage stage = new Stage();
    private Model model;

    /** Initialize with a new scene displaying web view
     */
    public void initialize(){
        stage.setScene(new Scene(anchorPane));
        stage.setOnCloseRequest(e -> webView.getEngine().loadContent(""));
    }

    /** Inject selected article's information
     */
    public void setModel(Model model){
        this.model = model;
    }

    /** Show article's content
     * @param title: article's title
     * @param html: article's html content
     */
    public void show(String title, String html){
        if (StringUtils.isEmpty(html)){
            return;
        }
        webView.getEngine().loadContent(html, "text/html");
        stage.setTitle(title);
        stage.requestFocus();
        stage.show();

        checkPreviousAndNextButtons();
    }

    /** Close current article view
     */
    public void close(){
        webView.getEngine().loadContent("");
        stage.close();
    }

    /** Jump to next article view if exists
     */
    public void next(){
        Article a = model.nextArticle();
        if (a == null){
            return;
        }
        show(a.getTitle(), a.getHtml());
    }

    /** Jump to last article view if exists
     */
    public void previous(){
        Article a = model.previousArticle();
        if (a == null){
            return;
        }
        show(a.getTitle(), a.getHtml());
    }

    /** Change next and previous button status depending on the existence of previous or ahead articles
     */
    private void checkPreviousAndNextButtons(){
        nextButton.setDisable(model.hasNoNextArticle());
        previousButton.setDisable(model.hasNoPreviousArticle());
    }
}
