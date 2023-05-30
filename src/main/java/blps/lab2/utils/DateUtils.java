package blps.lab2.utils;

import java.time.*;

public class DateUtils {
    public static Long currentUTCSeconds() {
        return OffsetDateTime.now(ZoneOffset.UTC).toEpochSecond();
    }
}
