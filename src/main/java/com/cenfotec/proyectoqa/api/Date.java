package com.cenfotec.proyectoqa.api;

public interface Date {
    GregorianDate addDays(final int offset);
    boolean isLeapYear();
    int getYearDays();
    DayOfWeek getDayOfWeek();
}
