package Scraper;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.time.LocalDateTime;

public class RetrieveInMetaTag implements DateTimeRetrievable{

    @Override
    public String getDateTimeString(Document doc, String propertyContainsDateTimeInfo) {
        String dateTimeStr;
        Elements dateTimeTag = doc.getElementsByAttributeValue("property", propertyContainsDateTimeInfo);
        // this is for VNExpress with their unconventional naming!
        if (dateTimeTag.outerHtml().isEmpty()){
            dateTimeTag = doc.getElementsByAttributeValue("itemprop", propertyContainsDateTimeInfo);
        }
        return dateTimeTag.attr("content");
    }

    @Override
    public LocalDateTime getLocalDateTime(String dateTimeStr) {
        if (dateTimeStr.isEmpty() || dateTimeStr.isBlank()){
            return LocalDateTime.now();
        }

        // example dateTimeStr:
        // 2 0 2 1 - 0 7 - 1 1 T  1  6  :  4  4  :  0  0 +07:00
        // 0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18
        int year = Integer.parseInt(dateTimeStr.substring(0, 4));
        int month = Integer.parseInt(dateTimeStr.substring(5, 7));
        int day = Integer.parseInt(dateTimeStr.substring(8, 10));
        int hours = Integer.parseInt(dateTimeStr.substring(11, 13));
        int minutes = Integer.parseInt(dateTimeStr.substring(14, 16));
        return (LocalDateTime.of(year, month, day, hours, minutes));
    }

}