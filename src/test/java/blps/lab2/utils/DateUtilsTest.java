package blps.lab2.utils;

import org.apache.commons.validator.GenericValidator;
import org.junit.jupiter.api.Test;

import javax.validation.constraints.AssertTrue;
import java.text.DateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class DateUtilsTest {

    @Test
    void currentUTCSeconds() {
        System.out.println(new Date().getTime());
    }
}