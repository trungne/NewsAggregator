package business.Helper;

import java.time.LocalDateTime;

public class LocalDateTimeParser {
    // example dateTimeStr:
    // 2 0 2 1 - 0 7 - 1 1 T  1  6  :  4  4  :  0  0 +07:00
    // 0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18
    public static LocalDateTime parse(String time){
        int year = Integer.parseInt(time.substring(0, 4));
        int month = Integer.parseInt(time.substring(5, 7));
        int day = Integer.parseInt(time.substring(8, 10));
        int hours = Integer.parseInt(time.substring(11, 13));
        int minutes = Integer.parseInt(time.substring(14, 16));
        return (LocalDateTime.of(year, month, day, hours, minutes));
    }
}
