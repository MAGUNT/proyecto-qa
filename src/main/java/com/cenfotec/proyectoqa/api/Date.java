package com.cenfotec.proyectoqa.api;

public interface Date {
    GregorianDate addDays(final long offset);
    boolean isLeapYear();
    int getYearDays();
    DayOfWeek getDayOfWeek();

    public static Date of(int year, Month month, int day) {
        return new GregorianDate(year, month, day);
    }
}
