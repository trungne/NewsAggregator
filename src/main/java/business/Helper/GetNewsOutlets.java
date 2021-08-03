package business.Helper;


import business.NewsSources.*;

import java.util.HashMap;

public class GetNewsOutlets {
    public static final HashMap<String, NewsOutlet> newsOutlets = initializeNewsOutlets();
    public static HashMap<String, NewsOutlet> initializeNewsOutlets(){
        HashMap<String, NewsOutlet> newsList = new HashMap<>();
        newsList.put("VNExpress", VNExpress.init());
        newsList.put("ZingNews", ZingNews.init());
        newsList.put("ThanhNien", ThanhNien.init());
        newsList.put("TuoiTre", TuoiTre.init());
        newsList.put("NhanDan", NhanDan.init());
        return newsList;
    }
}
