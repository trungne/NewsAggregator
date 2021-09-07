package Business.Scraper.Helper;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;

import java.time.LocalDateTime;

public class LocalDateTimeParser {
    // example dateTimeStr:
    // 2 0 2 1 - 0 7 - 1 1 T  1  6  :  4  4  :  0  0 +07:00
    // 0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18
    public static LocalDateTime parse(String time) {
        try {
            int year = Integer.parseInt(time.substring(0, 4));
            int month = Integer.parseInt(time.substring(5, 7));
            int day = Integer.parseInt(time.substring(8, 10));
            int hours = Integer.parseInt(time.substring(11, 13));
            int minutes = Integer.parseInt(time.substring(14, 16));
            return (LocalDateTime.of(year, month, day, hours, minutes));
        } catch (NumberFormatException e){
            return null;
        }
    }

    public static LocalDateTime parse(Element e){
        String dateTimeStr = getDateTimeSubString(e.text());
        if (StringUtils.isEmpty(dateTimeStr)){
            return null;
        }

        // time first
        // 0 7 : 3 0 0 4 - 0 6 -  2  0  2  1
        // 0 1 2 3 4 5 6 7 8 9 10 11 12 13 14
        if (dateTimeStr.substring(0, 5).contains(":")) {
            try {
                int hours = Integer.parseInt(dateTimeStr.substring(0, 2));
                int minutes = Integer.parseInt(dateTimeStr.substring(3, 5));
                int day = Integer.parseInt(dateTimeStr.substring(5, 7));
                int month = Integer.parseInt(dateTimeStr.substring(8, 10));
                int year = Integer.parseInt(dateTimeStr.substring(11));
                return (LocalDateTime.of(year, month, day, hours, minutes));
            } catch (NumberFormatException er) {
                return null;
            }
        }

        // date first
        // 1 0 - 0 7 - 2 0 2 1 0  8  :  4  6
        // 0 1 2 3 4 5 6 7 8 9 10 11 12 13 14
        try {
            int day = Integer.parseInt(dateTimeStr.substring(0, 2));
            int month = Integer.parseInt(dateTimeStr.substring(3, 5));
            int year = Integer.parseInt(dateTimeStr.substring(6, 10));
            int hours = Integer.parseInt(dateTimeStr.substring(10, 12));
            int minutes = Integer.parseInt(dateTimeStr.substring(13));
            return (LocalDateTime.of(year, month, day, hours, minutes));
        } catch (NumberFormatException err) {
            return null;
        }
    }

    // remove day of the week from the datetime string.
    // Example Chủ Nhật, 10-07-2021, 08:45 into
    // 10-07-2021, 08:45
    public static String getDateTimeSubString(String str) {
        str = str.trim();
        str = str.replaceAll(",", "");
        str = str.replaceAll("\\s+", "");
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            // get the substring from the first digit onwards
            if (Character.isDigit(ch) ||
                    ch == '-' ||
                    ch == ':') {
                builder.append(ch);
            }
        }
        return builder.toString();
    }


}
