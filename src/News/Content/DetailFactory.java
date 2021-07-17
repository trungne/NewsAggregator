package News.Content;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public abstract class DetailFactory {

    public ArrayList<Detail> createDetailList(Elements contentElements){
        ArrayList<Detail> details = new ArrayList<>();

        // TODO: clear all css classes of elements

        for(Element e: contentElements){
            // TODO: add specific css classes to each detail
            details.add(createDetail(e));
        }

        return details;
    }

    protected abstract Detail createDetail(Element e);
}
