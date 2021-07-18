package News.Content;

import org.jsoup.nodes.Element;

public class HtmlElementFactory extends DetailFactory {

    @Override
    protected Element createCustomTag(Element e, String type) {

        if (type.equals(TITLE_CSS_CLASS)){

        }
        else if (type.equals(DESCRIPTION_CSS_CLASS)){

        }
        else if (type.equals(MAIN_CONTENT_CSS_CLASS)){

        }
        else if (type.equals(THUMBNAIL_CSS_CLASS)){

        }
        else{

        }

        return e;
    }
}
