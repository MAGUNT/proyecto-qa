package com.cenfotec.proyectoqa.api;

public final class GregorianDate implements Date {
    private static final int DAYS_IN_YEAR_NO_LEAP         = 365;
    private static final int GREGORIAN_CALENDAR_INIT_DATE = 1582;
    private static final int LEAP_YEAR_INTERVAL = 4;
    private static final int CENTURY_INTERVAL   = 4;
    private static final int CENTURY            = 100;
    private static final int CENTURY_OFFSET     = 5;

    private final int day;
    private final Month month;
    private final int year;

    public GregorianDate(final int day, final Month month, final int year) {
        this.day   = day;
        this.month = month;
        this.year  = year;
    }
    public static boolean isLeapYear(final int year) {
        return (year % LEAP_YEAR_INTERVAL == 0 && year % CENTURY != 0)
                || year % (CENTURY_INTERVAL * CENTURY) == 0;
    }

    public boolean isLeapYear() {
        return isLeapYear(year);
    }

    public GregorianDate addDays(final int offset) {
        int yearDays = getYearDays() + offset;
        int year     = this.year;

        while (yearDays > daysInYear(year)) {
            yearDays -= daysInYear(year);
            year += 1;
        }
        while(yearDays <= 0) {
            year -= 1;
            yearDays += daysInYear(year);
        }
        return fromYearDays(yearDays, year);
    }

    private GregorianDate fromYearDays(final int daysOfYear, final int year) {
        Month month = Month.DECEMBER;
        int leap    = leapCount(year);
        while(daysOfYear <= (month.getAccumulatedDays() + leap)) {
            month = month.previous();
            leap  = !greaterThanFebruary(month) ? 0 : leap;
        }
        int days = daysOfYear
                - month.getAccumulatedDays()
                - leap;
        return new GregorianDate(days, month, year);
    }

    private int daysInYear(final int year) {
        return DAYS_IN_YEAR_NO_LEAP + leapCount(year);
    }

    public int getYearDays() {
        int days =  month.getAccumulatedDays() + day;
        if (greaterThanFebruary(month)) {
            days += leapCount(year);
        }
        return  days;
    }

    public DayOfWeek getDayOfWeek() {
        final int yearCopy = year - (!greaterThanFebruary(month) ? 1 : 0);
        final int century  = Math.floorDiv(yearCopy, CENTURY);
        final int week = (day
                + cycleOffset(century)
                + monthOffset(month)
                + centuryReminderOffset(yearCopy))
                % DayOfWeek.daysInWeek();

        return DayOfWeek.ofIndex(week);
    }

    private static int leapCount(final int year) {
        return isLeapYear(year) ? 1 : 0;
    }

    private static boolean greaterThanFebruary(final Month month) {
        return month.compareTo(Month.FEBRUARY) > 0;
    }


    private int cycleOffset(final int century) {
        return CENTURY_OFFSET * Math.floorMod(century, CENTURY_INTERVAL);
    }

    /***
     * Nota: Se utiliza  Math.floorMod para replazar el operador % ya que para valores negativos del año (
     * A pesar de que solo vamos a usar años mayores a 1582) queremos que el modulo sea positivo, por ejemplo
     *
     *  -48 % 5 == -3
     *  Math.floorMod(-48 , 5) == 2
     *
     *  Ademas se utiliza Math.floorDiv(yearCopy, CENTURY) ya que la divicion normal trunca hacia cero
     *  mientras que esta operacion trunca hacia el valor menor. (Igual esto es solo importante para años menores a cero)
     *  por ejemplo:
     *
     *  Math.floorDiv(-4, 100) == -1
     *  -4 / 100 == 0
     *
     *  Esto es importante para calcular posteriormente el desface del ciclo por ejemplo
     *  Math.floorMod(-1, 4) == 3 el cual es el valor esperado.
     * @return
     */
    private int centuryReminderOffset(final int year) {
        int centuryRem       = Math.floorMod(year , CENTURY);
        int centuryLeapYears = (centuryRem / LEAP_YEAR_INTERVAL);
        return centuryRem + centuryLeapYears;
    }

    private int monthOffset(final Month month) {
        return month.getAccumulatedDays()
                - (greaterThanFebruary(month) ? 1 : 0);

    }

    @Override
    public String toString() {
        return String.format("(%d, %s, %d)",
                day, month.toNumber(), year);
    }

    /*Mejor usar tabla*/
    /*
    private int monthOffset(int month) {
        double rotatedMonth = (double) ((month + 9) % NUM_MONTH + 1);
        return (int) Math.floor(2.6d * rotatedMonth - 0.2d);
    }
    */

}
