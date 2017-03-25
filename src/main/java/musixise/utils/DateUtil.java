package musixise.utils;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Created by zhaowei on 17/3/23.
 */
public class DateUtil {
    public static String asDate(LocalDateTime localDateTime) {
        if (localDateTime != null) {
            Date dd = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
            String dateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dd);
            return dateStr;
        }

        return "";
    }

    public static LocalDateTime asLocalDateTime(String date) {
        if (date != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return LocalDateTime.parse(date, formatter);
        } else {
            return null;
        }
    }
}
