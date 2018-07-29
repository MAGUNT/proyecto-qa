import com.cenfotec.proyectoqa.api.Date;
import com.cenfotec.proyectoqa.api.GregorianDate;
import com.cenfotec.proyectoqa.api.Month;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.testng.Assert;

import java.util.stream.Stream;

class DateTestReport3 {


    /**
     * <p>Prueba el metodo fromYearDays</p>
     * @param year año
     * @param days dias
     * @param expected valor esperado
     */

    @ParameterizedTest
    @MethodSource("testFromYearDaysProvider")
    void testFromYearDays(final long year, final int days, Date expected) {
        Date date = GregorianDate.fromYearDays(days, year);
        Assert.assertEquals(date, expected);
    }

    /**
     * <p>Retorna los casos de prueba para testFromYearDays</p>
     * @return casos de prueba
     */
    static Stream<Arguments> testFromYearDaysProvider() {
        return Stream.of(
                Arguments.of(1843L, 365, Date.of(1843, Month.DECEMBER, 31)),
                Arguments.of(1844L, 366, Date.of(1844, Month.DECEMBER, 31)),
                Arguments.of(1853L, 44, Date.of(1853, Month.FEBRUARY, 13))
        );
    }

    /**
     * <p>Prueba el metodo getYearDays</p>
     * @param year año
     * @param month mes
     * @param days dia
     * @param expected valor esperado
     */
    @ParameterizedTest
    @MethodSource("testGetYearDaysProvider")
    void testGetYearDays(final long year,
                         final Month month,
                         final int days,
                         final int expected) {

        Date date = Date.of(year, month, days);
        Assertions.assertEquals(date.getYearDays(), expected);
    }

    /**
     * <p>Retorna los casos de prueba para testGetYearDays</p>
     * @return casos de prueba
     */
    static Stream<Arguments> testGetYearDaysProvider() {
        return Stream.of(
                Arguments.of(1600L, Month.JULY, 15, 197),
                Arguments.of(1843L, Month.FEBRUARY, 15, 46)
        );
    }


    /**
     * <p>Prueba el metodo addDays</p>
     * @param year año
     * @param month mes
     * @param days dia
     * @param offset dias desplazados
     * @param expected valor esperado
     */
    @ParameterizedTest
    @MethodSource("testAddDaysProvider")
    void testAddDays(final long year,
                     final Month month,
                     final int days,
                     final int offset,
                     final Date expected) {

        Date date = Date.of(year, month, days).addDays(offset);
        Assertions.assertEquals(date, expected);
    }

    /**
     * <p>Retorna los casos de prueba para testAddDays</p>
     * @return casos de prueba
     */
    static Stream<Arguments> testAddDaysProvider() {
        final int days = 15;
        return Stream.of(
                Arguments.of(1600L, Month.JULY, days, -600,
                        Date.of(1598, Month.NOVEMBER, 23)),

                Arguments.of(1601L, Month.JULY, days, -600,
                        Date.of(1599, Month.NOVEMBER, 23)),

                Arguments.of(1843L, Month.SEPTEMBER, days, 600,
                        Date.of(1845, Month.MAY, 7)),

                Arguments.of(1844L, Month.SEPTEMBER, days, 600,
                        Date.of(1846,  Month.MAY, 8)),

                Arguments.of(1843L, Month.FEBRUARY, days, 319,
                        Date.of(1843,  Month.DECEMBER, 31))
        );
    }


    /**
     * <p>Prueba el metodo futureDate</p>
     * @param year año
     * @param month mes
     * @param days dia
     * @param offset dias desplazados
     * @param expected valor esperado
     */
    @ParameterizedTest
    @MethodSource("testFutureDateProvider")
    void testFutureDate(final long year, final Month month, final int days, final int offset, final Date expected) {
        Date date = Date.of(year, month, days).futureDate(offset);
        Assertions.assertEquals(date, expected);
    }


    /**
     * <p>Retorna los casos de prueba para testFutureDate</p>
     * @return casos de prueba
     */
    static Stream<Arguments> testFutureDateProvider() {
        final int days = 15;
        return Stream.of(
                Arguments.of(1601L, Month.JULY, days, 0,
                        Date.of(1601, Month.JULY, 15)),

                Arguments.of(1843L, Month.SEPTEMBER, days, 5667,
                        Date.of(1859, Month.MARCH, 22))
        );
    }

    /**
     * <p>Prueba el caso de error cuando offset es negativo</p>
     */
    @Test
    void TestFailFutureTest() {
        Date date = Date.of(1600, Month.JULY, 15);
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> date.futureDate(-1));
    }

    /**
     * <p>Prueba el metodo pastDate</p>
     * @param year año
     * @param month mes
     * @param days dia
     * @param offset dias desplazados
     * @param expected valor esperado
     */
    @ParameterizedTest
    @MethodSource("testPastDateProvider")
    void testPastDate(final long year, final Month month, final int days, final int offset,  final Date expected) {
        Date date = Date.of(year, month, days).pastDate(offset);
        Assertions.assertEquals(date, expected);
    }

    /**
     * <p>Retorna los casos de prueba para testPastDate</p>
     * @return casos de prueba
     */
    static Stream<Arguments> testPastDateProvider() {
        final int days = 15;
        return Stream.of(
                Arguments.of(1601L, Month.JULY, days, 0,
                        Date.of(1601, Month.JULY, 15)),

                Arguments.of(1843L, Month.SEPTEMBER, days, 5667,
                        Date.of(1828, Month.MARCH, 10))
        );
    }

    /**
     * <p>Prueba el caso de error cuando offset es negativo</p>
     */
    @Test
    void TestFailPastTest() {
        Date date = Date.of(1600, Month.JULY, 15);
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> date.pastDate(-1));
    }


    /**
     * <p>Prueba el metodo daysBetween</p>
     * @param date1 fecha 1
     * @param date2 fecha 2
     * @param expectedDays dias esperados
     */
    @ParameterizedTest
    @MethodSource("testDaysBetweenProvider")
    void testDaysBetween(final Date date1, final Date date2, final int expectedDays) {
        Assertions.assertEquals(
                date1.daysBetween(date2),
                date2.daysBetween(date1),
                expectedDays);
    }

    /**
     * <p>Retorna los casos de prueba para testDaysBetween</p>
     * @return casos de prueba
     */
    static Stream<Arguments> testDaysBetweenProvider() {
        final int days = 15;
        return Stream.of(
                Arguments.of(Date.of(1601L, Month.JULY, days),
                        Date.of(3000, Month.JULY, 15), 510974),

                Arguments.of(Date.of(4000, Month.SEPTEMBER, days),
                        Date.of(1583, Month.MARCH, 10), 882981)
        );
    }

}
