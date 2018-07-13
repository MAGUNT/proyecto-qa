import com.cenfotec.proyectoqa.api.Date;
import com.cenfotec.proyectoqa.api.DayOfWeek;
import com.cenfotec.proyectoqa.api.GregorianDate;
import com.cenfotec.proyectoqa.api.Month;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 *<p>Pruebas para la fecha gregoriana.</p>
 */
public class DateTest {
    private static final long LEAP_CENTURY_INTERVAL = 400;
    private static final int DAY_IN_WEEK = 7;
    private static final String FORMAT_REGEX =
            "\\((([1-9]\\d{3,})|(\\d{4}))\\s*,\\s*(1[0-2]" +
            "|0[1-9])\\s*,\\s*([12]\\d|0[1-9]|3[01])\\)";


    /**
     * <p>Prueba para verificar que la fecha falle cuando hay un “overflow” en el año.</p>
     */
    @Test
    void addDaysOverflowYearTest() {
        final long offset = 366;
        final int day     = 1;
        final Month month = Month.FEBRUARY;

        Assertions.assertThrows(ArithmeticException.class, () ->
                Date.of(Long.MAX_VALUE, month, day).addDays(offset));
    }

    /**
     * <p>Prueba para probar que la clase falle cuando hay un “overflow “debido al offset.</p>
     */
    @Test
    void addDaysOverflowOffsetTest() {
        final long year   = 1900;
        final int day     = 1;
        final Month month = Month.FEBRUARY;

        Assertions.assertThrows(ArithmeticException.class,
                () -> Date.of(year, month, day).addDays(Long.MAX_VALUE));
    }

    /**
     * <p>Pruebas de particiones de equivalencia para la variable mes, en la función para buscar el día de la semana.
     * Esta función solo se encarga de los valores límite inválidos. </p>
     */
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

    /**
     * <p>Pruebas parametrizables de particiones de equivalencia para la variable mes,
     *    en la función para buscar el día de la semana.
     *    Esta función solo se encarga de los valores límite válidos. </p>
     *
     * @param year Año
     * @param month Mes
     * @param day Día
     * @param controlResult Resultado esperado.
     */
    @ParameterizedTest
    @MethodSource("validBoundaryProvider")
    void dayOfTheWeekMonthValidBoundaryTest(long year, int month, int day, DayOfWeek controlResult) {
        Assertions.assertEquals(Date.of(year, month, day).getDayOfWeek(), controlResult);
    }

    /**
     * <p>Función que provee los datos de prueba a la función dayOfTheWeekMonthValidBoundaryTest.</p>
     * @return Datos de prueba para la función dayOfTheWeekMonthValidBoundaryTest
     */
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

    /**
     * <p>Test para probar todos los caminos posibles del método equals.</p>
     */
    @Test
    void equalsPaths() {
        final int year    = 1584;
        final Month month = Month.JANUARY;
        final int day     = 15;

        // Primer condicional falla.
        Assertions.assertNotEquals(
                Date.of(year - 1, month, day),
                Date.of(year, month, day));

        // Segundo condicional falla.
        Assertions.assertNotEquals(
                Date.of(year, month, day),
                Date.of(year, month, day + 1));
        // Tercer condicional falla.

        Assertions.assertNotEquals(
                Date.of(year, month.next(), day),
                Date.of(year, month, day));
    }

    /**
     * <p>Test para probar la consistencia de hascode</p>
     */
    @Test
    void hashcodeTest() {
        // Test de predictibilidad.
        final Month month = Month.AUGUST;
        final long year   = 1645;
        final int day     = 12;
        Assertions.assertEquals(
                Date.of(year, month, day).hashCode(),
                Date.of(year, month, day).hashCode());
    }

    /**
     * <p>Pruebas para verificar el contrato de equals.</p>
     */
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

    /**
     * <p>Prueba que verifica que la función getDayOfWeek cumpla con desfase 0 cada 400 años.</p>
     */
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

    /**
     *<p>Verifica el desfase que produce un día en la función getDayOfWeek</p>
     */
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


    /**
     *  <p>Pruebas parametrizables de particiones de equivalencia para la variable dia,
     *   en la función para validar la fecha.
     *  Esta función solo se encarga de los valores límite inferiores en todos los meses. </p>
     * @param month
     */
    @ParameterizedTest
    @EnumSource(value = Month.class)
    void validDateDayLowBoundaryTest(Month month) {
        final long year = 1877;
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> Date.of(year, month, 0));
        Assertions.assertDoesNotThrow(
                () -> Date.of(year, month, 1));
    }


    /**
     *  <p>Pruebas parametrizables de particiones de equivalencia para la variable día ,
     *   en la función para validar la fecha.
     *  Esta función solo se encarga de los valores límite superiores en los meses con 31 días. </p>
     * @param month
     */
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

    /**
     *  <p>Pruebas parametrizables de particiones de equivalencia para la variable día ,
     *   en la función para validar la fecha.
     *  Esta función solo se encarga de los valores límite superiores en los meses con 30 días. </p>
     * @param month
     */
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

    /**
     *  <p>Pruebas de particiones de equivalencia para la variable día ,
     *   en la función para validar la fecha.
     *   Esta función solo se encarga de los valores límite superiores en el mes de febrero,
     *  con el año no bisiesto. </p>
     *
     */
    @Test
    void validDateDayBoundaryTestForFebruaryNoLeap() {
        final long year   = 1721;
        final Month month = Month.FEBRUARY;
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> Date.of(year, month, 29));
        Assertions.assertDoesNotThrow(
                () -> Date.of(year, month, 28));
    }

    /**
     *  <p>Pruebas de particiones de equivalencia para la variable día ,
     *   en la función para validar la fecha.
     *   Esta función solo se encarga de los valores límite superiores en el mes de febrero,
     *  con el año bisiesto. </p>
     *
     */
    @Test
    void validDateDayBoundaryTestForFebruaryLeap() {
        final long leapYear   = 1724;
        final Month month = Month.FEBRUARY;
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> Date.of(leapYear, month, 30));
        Assertions.assertDoesNotThrow(
                () -> Date.of(leapYear, month, 29));
    }


    /**
     *  <p>Pruebas de particiones de equivalencia para la variable mes,
     *   en la función para validar la fecha.
     *
     */
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

    /**
     *  <p>Pruebas de particiones de equivalencia para la variable año,
     *   en la función para validar la fecha.
     */
    @Test
    void validDateYearBoundaryTest() {
        final int day      =  15;
        final Month month  = Month.FEBRUARY;
        final long boundaryYear = 1583;

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> Date.of(boundaryYear - 1, month, day));


        Assertions.assertDoesNotThrow(
                () -> Date.of(boundaryYear, month, day));

        // Nuevo
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> Date.of(boundaryYear - 1, Month.JANUARY, day));
    }

    // nuevo

    @Test
    void nullMonthAssertionError() {
        final int year    = 1777;
        final int day     = 15;
        final Month month = null;

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new GregorianDate(year, month, day));
    }

    /**
     *  <p>Pruebas parametrizables de particiones de equivalencia para la variable día,
     *   en la función para calcular el día siguiente.
     *  Esta función solo se encarga de los valores límite inferiores en todos los meses. </p>
     * @param month Mes
     */
    @ParameterizedTest
    @EnumSource(value = Month.class)
    void nextDayLowBoundaryTest(Month month) {
        final long year = 1877;
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> Date.of(year, month, 0).nextDay());

        Assertions.assertEquals(Date.of(year, month, 1).nextDay(),
                Date.of(year, month, 2));
    }


    /**
     *  <p>Pruebas parametrizables de particiones de equivalencia para la variable día,
     *   en la función para calcular el día siguiente.
     *  Esta función solo se encarga de los valores límite superiores, en los meses con 31 días. </p>
     * @param month Mes
     */
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


    /**
     *  <p>Pruebas parametrizables de particiones de equivalencia para la variable día,
     *   en la función para calcular el día siguiente.
     *  Esta función solo se encarga de los valores límite superiores, en los meses con 30 días. </p>
     * @param month Mes
     */
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


    /**
     *  <p>Pruebas de particiones de equivalencia para la variable día,
     *   en la función para calcular el día siguiente.
     *  Esta función solo se encarga de los valores límite superiores, en febrero sin año bisiesto. </p>
     */
    @Test
    void nextDayBoundaryTestForFebruaryNoLeap() {
        final long year   = 1721;
        final Month month = Month.FEBRUARY;
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> Date.of(year, month, 29).nextDay());

        Assertions.assertEquals(Date.of(year, month, 28).nextDay(),
                Date.of(year, month.next(), 1));
    }

    /**
     *  <p>Pruebas de particiones de equivalencia para la variable día,
     *   en la función para calcular el día siguiente.
     *  Esta función solo se encarga de los valores límite superiores, en febrero con año bisiesto. </p>
     */
    @Test
    void nextDayBoundaryTestForFebruaryLeap() {
        final long leapYear   = 1724;
        final Month month = Month.FEBRUARY;
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> Date.of(leapYear, month, 30).nextDay());
        Assertions.assertEquals(Date.of(leapYear, month, 29).nextDay(),
                Date.of(leapYear, month.next(), 1));
    }

    /**
     *  <p>Pruebas de particiones de equivalencia para la variable mes,
     *   en la función para calcular el dia siguiente.
     */
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

    /**
     *  <p>Pruebas de particiones de equivalencia para la variable año,
     *   en la función para calcular el dia siguiente.
     */
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

    /**
     * Prueba para verificar que el método toString cumpla con el formato (YYYY, MM, DD).
     */
    @Test
    void toStringFormatTest() {
        final int day      =  15;
        final Month month  = Month.JANUARY;
        final long year = 1583;

        Date date = Date.of(year, month, day);
        Assertions.assertTrue(
                Pattern.matches(FORMAT_REGEX,
                        date.toString()));
    }

    /**
     *  <p>Pruebas de particiones de equivalencia para la variable año,
     *      en la función para determinar si un año es bisiesto.
     */
    @Test
    void isLeapYearBoundaryTest() {
        final long boundaryYear = 1582;
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> GregorianDate.isLeapYear(boundaryYear));

        Assertions.assertFalse(
                GregorianDate.isLeapYear(boundaryYear + 1));
    }

    /**
     * <p>Prueba parametrizable para el método que determina si un año es bisiesto.</p>
     * @param year Año
     * @param isLeapYearExpected Valor esperado
     */
    @ParameterizedTest
    @MethodSource("yearLeapTestProvider")
    void leapYearTest(long year, boolean isLeapYearExpected) {
        Assertions.assertEquals(
                isLeapYearExpected,
                GregorianDate.isLeapYear(year));
    }


    /**
     * <p>Este método provee los datos a la prueba leapYearTest. Cada dato pertenece a un grupo específico.</p>
     * @return Parámetros para prueba leapYearTest.
     */
    static Stream<Arguments> yearLeapTestProvider() {
        return Stream.of(
                Arguments.of(1777L, false),
                Arguments.of(1900L, false),
                Arguments.of(1904L, true),
                Arguments.of(1600L, true));
    }

    /**
     * <p>Prueba para el método que agrega o resta días.</p>
     */
    @Test
    void addDaysTest() {
        final int year    = 1600;
        final int day     = 1;
        final Month month = Month.FEBRUARY;
        final long offset = 2110;

        Date date = Date.of(year, month, day);
        Date expectedAddDate = Date.of(1605, Month.NOVEMBER, 11);
        Date expectedMinus = Date.of(1594, Month.APRIL, 23);

        Assertions.assertEquals(expectedAddDate, date.addDays(offset));
        Assertions.assertEquals(expectedMinus, date.addDays(-offset));
    }

}
