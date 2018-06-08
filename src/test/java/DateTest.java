import com.cenfotec.proyectoqa.api.Date;
import com.cenfotec.proyectoqa.api.DayOfWeek;
import com.cenfotec.proyectoqa.api.Month;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class DateTest {

    private static final long LEAP_CENTURY_INTERVAL = 400;
    private static final int DAY_IN_WEEK = 7;

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


    @Test
    void equalsTest() {
        final Month month     = Month.AUGUST;
        final long year       = 1645;
        final int day         = 25;

        Date date1 = Date.of(year, month, day);
        Date date2 = Date.of(year, month, day);
        Date date3 = Date.of(year, month, day);
        Date date4 = Date.of(year + 1, month, day);

        //reflexivity
        Assertions.assertEquals(date1, date1);

        //transitivity
        Assertions.assertEquals(date1, date2);
        Assertions.assertEquals(date2, date3);
        Assertions.assertEquals(date1, date3);

        //no nullity
        Assertions.assertNotEquals(date2, null);

        //symmetry
        Assertions.assertEquals(date2, date3);
        Assertions.assertEquals(date3, date2);

        Assertions.assertNotEquals(date4, date1);

    }

    @Test
    void cycleTest() {
        final Month month     = Month.FEBRUARY;
        final long year       = 1600;
        final int day         = 15;
        final long iteration  = 2;
        final long yearOffset = year
                + (iteration * LEAP_CENTURY_INTERVAL);

        Date date = Date.of(year, month, day);
        Date offset = Date.of(yearOffset, month, day);
        Assertions.assertEquals(date.getDayOfWeek(),
                offset.getDayOfWeek());
    }

    @Test
    void dayOffsetTest() {
        final Month month     = Month.JANUARY;
        final long year       = 1600;
        final int day         = 15;
        final int dayOffset  = day + 1;

        Date date = Date.of(year, month, day);
        Date offset = Date.of(year, month, dayOffset);

        int transformDayOfWeek = (date.getDayOfWeek().ordinal() + 1)
                % DAY_IN_WEEK;
        Assertions.assertEquals(transformDayOfWeek,
                offset.getDayOfWeek().ordinal());
    }



    @ParameterizedTest
    @EnumSource(value = Month.class)
    void validDateDayLowBoundaryTest(Month month) {
        final long year = 1877;
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> Date.of(year, month, 0));
        Assertions.assertDoesNotThrow(
                () -> Date.of(year, month, 1));
    }


    @ParameterizedTest
    @EnumSource(value = Month.class,
            names = { "JANUARY", "MARCH",
                    "MAY", "JULY", "AUGUST", "OCTOBER", "DECEMBER"})
    void validDateDayBoundaryTestForMonthsWith31Days(Month month) {
        final long year = 1777;
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> Date.of(year, month, 32));
        Assertions.assertDoesNotThrow(
                () -> Date.of(year, month, 31));
    }


    @ParameterizedTest
    @EnumSource(value = Month.class,
            names = { "APRIL", "JUNE", "SEPTEMBER", "NOVEMBER"})
    void validDateDayBoundaryTestForMonthsWith30Days(Month month) {
        final long year = 1781;
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> Date.of(year, month, 31));
        Assertions.assertDoesNotThrow(
                () -> Date.of(year, month, 30));
    }

    @Test
    void validDateDayBoundaryTestForFebruaryNoLeap() {
        final long year   = 1721;
        final Month month = Month.FEBRUARY;
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> Date.of(year, month, 29));
        Assertions.assertDoesNotThrow(
                () -> Date.of(year, month, 28));
    }

    @Test
    void validDateDayBoundaryTestForFebruaryLeap() {
        final long leapYear   = 1724;
        final Month month = Month.FEBRUARY;
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> Date.of(leapYear, month, 30));
        Assertions.assertDoesNotThrow(
                () -> Date.of(leapYear, month, 29));
    }

    @Test
    void validDateMonthBoundaryTest() {
        final int day    =  15;
        final long year  = 1721;

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> Date.of(year, 0, day));
        Assertions.assertDoesNotThrow(
                () -> Date.of(year, 1, day));

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> Date.of(year, 13, day));
        Assertions.assertDoesNotThrow(
                () -> Date.of(year, 12, day));

    }

    @Test
    void validDateYearBoundaryTest() {
        final int day      =  15;
        final Month month  = Month.FEBRUARY;
        final long boundaryYear = 1583;

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> Date.of(boundaryYear - 1, month, day));
        Assertions.assertDoesNotThrow(
                () -> Date.of(boundaryYear, month, day));

    }

    @ParameterizedTest
    @EnumSource(value = Month.class)
    void nextDayLowBoundaryTest(Month month) {
        final long year = 1877;
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> Date.of(year, month, 0).nextDay());

        Assertions.assertEquals(Date.of(year, month, 1).nextDay(),
                Date.of(year, month, 2));
    }



    @ParameterizedTest
    @EnumSource(value = Month.class,
            names = { "JANUARY", "MARCH", "MAY",
                    "JULY", "AUGUST", "OCTOBER", "DECEMBER"})
    void nextDayBoundaryTestForMonthsWith31Days(Month month) {
        final long year = 1877;
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> Date.of(year, month, 32).nextDay());

        long nextYear = year + (month == Month.DECEMBER ? 1 : 0);
        Assertions.assertEquals(Date.of(year, month, 31).nextDay(),
                Date.of(nextYear, month.next(), 1));
    }


    @ParameterizedTest
    @EnumSource(value = Month.class,
            names = { "APRIL", "JUNE", "SEPTEMBER", "NOVEMBER"})
    void nextDayBoundaryTestForMonthsWith30Days(Month month) {
        final long year = 1877;
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> Date.of(year, month, 31).nextDay());

        Assertions.assertEquals(Date.of(year, month, 30).nextDay(),
                Date.of(year, month.next(), 1));
    }


    @Test
    void nextDayBoundaryTestForFebruaryNoLeap() {
        final long year   = 1721;
        final Month month = Month.FEBRUARY;
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> Date.of(year, month, 29).nextDay());

        Assertions.assertEquals(Date.of(year, month, 28).nextDay(),
                Date.of(year, month.next(), 1));
    }

    @Test
    void nextDayBoundaryTestForFebruaryLeap() {
        final long leapYear   = 1724;
        final Month month = Month.FEBRUARY;
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> Date.of(leapYear, month, 30).nextDay());
        Assertions.assertEquals(Date.of(leapYear, month, 29).nextDay(),
                Date.of(leapYear, month.next(), 1));
    }
    @Test
    void nextDayMonthBoundaryTest() {
        final int day    =  15;
        final long year  = 1721;

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> Date.of(year, 0, day).nextDay());
        Assertions.assertEquals(
                Date.of(year, 1, day).nextDay(),
                Date.of(year, 1, day + 1) );

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> Date.of(year, 13, day).nextDay());
        Assertions.assertEquals(
                Date.of(year, 12, day).nextDay(),
                Date.of(year, 12, day + 1) );

    }

    @Test
    void nextDayYearBoundaryTest() {
        final int day      =  15;
        final Month month  = Month.FEBRUARY;
        final long boundaryYear = 1583;

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> Date.of(boundaryYear - 1, month, day).nextDay());

        Assertions.assertEquals(
                Date.of(boundaryYear, month, day).nextDay(),
                Date.of(boundaryYear, month, day + 1) );
    }



}
