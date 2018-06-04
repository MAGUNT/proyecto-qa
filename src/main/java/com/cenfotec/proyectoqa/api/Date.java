package com.cenfotec.proyectoqa.api;

public interface Date {
    GregorianDate addDays(final long offset);
    boolean isLeapYear();
    int getYearDays();
    DayOfWeek getDayOfWeek();

    static Date of(long year, Month month, int day) {
        return new GregorianDate(year, month, day);
    }
}
