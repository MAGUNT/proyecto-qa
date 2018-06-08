import com.cenfotec.proyectoqa.api.Date;
import com.cenfotec.proyectoqa.api.Month;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

public class MonthTest {

    private static final int MONTH_IN_YEAR = 12;
    private static final int[] ACCUMULATED_DAYS = {
            0, 31, 59, 90,
            120, 151, 181,
            212, 243, 273,
            304, 334, 365
    };
    @ParameterizedTest
    @EnumSource(Month.class)
    void accumulatedDaysTest(Month month) {
        Assertions.assertEquals(month.getAccumulatedDays(),
                ACCUMULATED_DAYS[month.ordinal()]);

    }

   @Test
    void monthNextTest() {
        Assertions.assertEquals(Month.DECEMBER.next(),
                Month.JANUARY);

        Assertions.assertEquals(Month.JANUARY.next(),
                Month.FEBRUARY);
    }

    @Test
    void monthPreviousTest() {
        Assertions.assertEquals(Month.JANUARY.previous(),
                Month.DECEMBER);

        Assertions.assertEquals(Month.DECEMBER.previous(),
                Month.NOVEMBER);
    }

    @Test
    void fromNumberTest() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> Month.fromNumber(0));
        Assertions.assertEquals(Month.fromNumber(1), Month.JANUARY);

        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> Month.fromNumber(13));
        Assertions.assertEquals(Month.fromNumber(12), Month.DECEMBER);
    }

    @Test
    void offsetTest() {
        Assertions.assertEquals(
                Month.DECEMBER.offset(MONTH_IN_YEAR),
                Month.DECEMBER);
    }
}
