package info.mta.bustime;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

public class TimeUtils {
    public static String getTimeDiff(ZonedDateTime dateTime) {
        ZonedDateTime now = ZonedDateTime.now();
        long diff = Math.abs(ChronoUnit.SECONDS.between(now, dateTime));
        if (diff < 59) {
            return String.format("%2s seconds", diff);
        } else {
            diff = Math.abs(ChronoUnit.MINUTES.between(now, dateTime));
            return String.format("%2s minutes", diff);
        }
    }
}
