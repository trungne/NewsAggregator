package News.Content;

import org.jsoup.nodes.Element;

public class HtmlElementFactory extends DetailFactory {

    @Override
    protected Detail createDetail(Element html) {
        Detail detail;
        // TODO: create matching detail for each html element
        // if pic create figure. check extension (.jpg, .png)
        if (html.text().endsWith(".jpg")){
            detail = new Figure(html.text());
        }
        else if (html.text().equals("caption")){
            detail = new Caption(html.text());
        }
        else{
            detail = new Paragraph(html.text());
        }
        // else create paragraph
        return detail;
    }
}
