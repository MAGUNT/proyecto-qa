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

    public static DayOfWeek ofIndex(final int index) {
        if (index < 0 || index > 6) {
            throw new IllegalArgumentException();
        }
        return DAYS_IN_WEEK[index];
    }

    public static int daysInWeek() {
        return DAYS_IN_WEEK.length;
    }
}
