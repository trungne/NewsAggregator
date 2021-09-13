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

package Application.Model;

import Business.News.Article;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.List;

public class GetArticleListService extends Service<List<Article>> {
    String category;

    public GetArticleListService(){

    }

    public void setCategory(String category) {
        this.category = category;
    }
    public String getCategory(){
        return this.category;
    }

    @Override
    protected Task<List<Article>> createTask() {
        return new GetArticleListTask(category);
    }
}
