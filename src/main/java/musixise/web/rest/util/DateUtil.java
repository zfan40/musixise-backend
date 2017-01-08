package musixise.web.rest.util;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * Created by zhaowei on 17/1/7.
 */
public class DateUtil {

    public static String locatDateTimeToString(LocalDateTime localDateTime) {
        Date dd = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        String dateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dd);

        return dateStr;
    }
}
