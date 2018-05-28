package com.cenfotec.proyectoqa.api;

public class Date {
    private static final int GREGORIAN_CALENDAR_INIT_DATE = 1582;
    private static final int LEAP_YEAR_INTERVAL = 4;
    private static final int LEAP_YEAR_POSITIVE_COMPENSATION = 400;
    private static final int NUM_DAY_WEEK = 7;
    private static final int NUM_MONTH = 12;
    private static final int CENTURY = 100;
    private static final int CENTURY_OFFSET = 5;

    private int day;
    private Month month;
    private int year;

    public Date(int day, Month month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public boolean isLeapYear() {
        return (year % LEAP_YEAR_INTERVAL == 0
                && year % CENTURY != 0)
                || year % LEAP_YEAR_POSITIVE_COMPENSATION == 0;
    }

    /***
     *
     * @return
     */
    public int getDayOfWeak() {
        int yearCopy          = year;
        int monthNum          = month.ordinal() + 1;
        yearCopy             -=  monthNum < 3 ? 1 : 0;
        int century           = yearCopy / CENTURY;
        int centuryRem        = yearCopy % CENTURY;
        int cycleOffset       = CENTURY_OFFSET * (century % 4);
        int centuryLeapYears  = (centuryRem / 4);
        return (day
                  + cycleOffset
                  + monthOffset(monthNum)
                  + centuryRem
                  + centuryLeapYears)
                % NUM_DAY_WEEK;
    }

    /*Mejor usar tabla*/
    public static int monthOffset(int month) {
        double rotatedMonth = (double) ((month + 9) % NUM_MONTH + 1);
        return (int) Math.floor(2.6d * rotatedMonth - 0.2d);
    }

}
