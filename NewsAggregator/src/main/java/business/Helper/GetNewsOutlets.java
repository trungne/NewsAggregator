package business.Helper;


import business.NewsSources.*;

import java.util.HashMap;

public class GetNewsOutlets {
    public static final HashMap<String, NewsOutlet> newsOutlets = new HashMap<>();
    static {
        newsOutlets.put("VNExpress", VNExpress.init());
        newsOutlets.put("ZingNews", ZingNews.init());
        newsOutlets.put("ThanhNien", ThanhNien.init());
        newsOutlets.put("TuoiTre", TuoiTre.init());
        newsOutlets.put("NhanDan", NhanDan.init());
    }

}
