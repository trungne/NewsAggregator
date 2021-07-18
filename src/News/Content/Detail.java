package News.Content;

import org.jsoup.nodes.Element;

public abstract class Detail extends Element {
    String text;

    public Detail(String tag) {
        super(tag);
    }

    public String text(){
        return this.text;
    }
}
