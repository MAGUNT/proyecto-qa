package com.cenfotec.proyectoqa.api;

public enum DayOfWeek {
    SUNDAY,
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY,
    SATURDAY;

    private static final DayOfWeek[] DAYS_IN_WEEK = DayOfWeek.values();
    private static final int MAX_INDEX = 6;
    private static final int MIN_INDEX = 0;


    public static DayOfWeek ofIndex(final int index) {
        if (index < MIN_INDEX || index > MAX_INDEX) {
            throw new IllegalArgumentException();
        }
        return DAYS_IN_WEEK[index];
    }

    public static int daysInWeek() {
        return DAYS_IN_WEEK.length;
    }
}
