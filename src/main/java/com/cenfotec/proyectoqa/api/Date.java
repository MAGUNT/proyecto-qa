package com.cenfotec.proyectoqa.api;

public interface Date {
    GregorianDate addDays(final long offset);
    GregorianDate nextDay();
    boolean isLeapYear();
    int getYearDays();
    DayOfWeek getDayOfWeek();

    static Date of(long year, Month month, int day) {
        return new GregorianDate(year, month, day);
    }
    static Date of(long year, int month, int day) {
        return new GregorianDate(year, month, day);
    }
}
