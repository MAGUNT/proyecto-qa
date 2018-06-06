import com.cenfotec.proyectoqa.api.Date;
import com.cenfotec.proyectoqa.api.DayOfWeek;
import com.cenfotec.proyectoqa.api.Month;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class DateTest {

    @Test
    void addDaysOverflowYearTest() {
        final long offset = 366;
        final int day     = 1;
        final Month month = Month.FEBRUARY;

        Assertions.assertThrows(ArithmeticException.class, () ->
                Date.of(Long.MAX_VALUE, month, day).addDays(offset));
    }

    @Test
    void addDaysOverflowOffsetTest() {
        final long year   = 1900;
        final int day     = 1;
        final Month month = Month.FEBRUARY;

        Assertions.assertThrows(ArithmeticException.class,
                () -> Date.of(year, month, day).addDays(Long.MAX_VALUE));
    }

    @Test
    void dayOfTheWeekMonthNoValidBoundaryTest() {
        final int invalidLeftBoundary = 0;
        final int invalidRightBoundary = 13;
        final long year = 2000;
        final int day = 15;

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> Date.of(year, invalidLeftBoundary, day));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> Date.of(year, invalidRightBoundary, day));
    }

    @ParameterizedTest
    @MethodSource("validBoundaryProvider")
    void dayOfTheWeekMonthValidBoundaryTest(long year, int month, int day, DayOfWeek controlResult) {
        Assertions.assertEquals(Date.of(year, month, day).getDayOfWeek(), controlResult);
    }
    static Stream<Arguments> validBoundaryProvider() {
        final long leapYear        = 2000;
        final long noLeapYear      = 2001;
        final int leftBoundary     = 1;
        final int rightBoundary    = 12;
        final int februaryBoundary = 2;
        final int day = 15;
        return Stream.of(
                Arguments.of(leapYear, rightBoundary, day,  DayOfWeek.FRIDAY),
                Arguments.of(noLeapYear, rightBoundary, day, DayOfWeek.SATURDAY),

                Arguments.of(noLeapYear, leftBoundary, day, DayOfWeek.MONDAY),
                Arguments.of(leapYear, leftBoundary, day,  DayOfWeek.SATURDAY),

                Arguments.of(noLeapYear, februaryBoundary, day,  DayOfWeek.THURSDAY),
                Arguments.of(leapYear, februaryBoundary, day,  DayOfWeek.TUESDAY),

                Arguments.of(noLeapYear, februaryBoundary + 1, day,  DayOfWeek.THURSDAY),
                Arguments.of(leapYear, februaryBoundary + 1, day,  DayOfWeek.WEDNESDAY)
        );
    }



}
