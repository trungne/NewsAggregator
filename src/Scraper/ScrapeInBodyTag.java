package Scraper;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.time.LocalDateTime;

public class ScrapeInBodyTag implements ScrapingDateTimeBehavior {
    @Override
    public LocalDateTime getLocalDateTime(Document doc, String propertyContainsDateTimeInfo) {
        Element dateTimeTag = doc.selectFirst(propertyContainsDateTimeInfo);
        String dateTimeStr;

        if (dateTimeTag != null){
            System.out.println(dateTimeTag.html());
            dateTimeStr = getDateTimeSubString(dateTimeTag.text());
        }
        else{
            return LocalDateTime.now();
        }

        // 1 0 - 0 7 - 2 0 2 1 , [space] 0  8  :  4  6
        // 0 1 2 3 4 5 6 7 8 9 10 11     12 13 14 15 16
        int day = Integer.parseInt(dateTimeStr.substring(0,2));
        int month = Integer.parseInt(dateTimeStr.substring(3,5));
        int year = Integer.parseInt(dateTimeStr.substring(6,10));
        int hours = Integer.parseInt(dateTimeStr.substring(12,14));
        int minutes = Integer.parseInt(dateTimeStr.substring(15));
        return (LocalDateTime.of(year, month, day, hours, minutes));
    }

    // remove day of the week from the datetime string.
    // Example Chủ Nhật, 10-07-2021, 08:45 into
    // 10-07-2021, 08:45
    private String getDateTimeSubString(String str){
        for(int i = 0; i < str.length(); i++){
            // get the substring from the first digit onwards
            if (Character.isDigit(str.charAt(i))){
                return str.substring(i);
            }
        }
        return "";
    }
}
