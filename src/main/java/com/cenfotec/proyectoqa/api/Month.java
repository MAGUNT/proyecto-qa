package com.cenfotec.proyectoqa.api;

import java.util.Arrays;

public enum Month {
    JANUARY(31),
    FEBRUARY(28),
    MARCH(31),
    APRIL(30),
    MAY(31),
    JUNE(30),
    JULY(31),
    AUGUST(31),
    SEPTEMBER(30),
    OCTOBER(31),
    NOVEMBER(30),
    DECEMBER(31);

    private static final Month[] MONTHS = Month.values();

    private final int days;
    private int accumulatedDays;

    static {
        int accumulatedCount = 0;
        for (Month month : MONTHS) {
            month.accumulatedDays = accumulatedCount;
            accumulatedCount += month.getDays();
        }

    }

    Month(final int days) {
        this.days = days;
    }

    public int getDays() {
        return days;
    }

    public int getAccumulatedDays() {
        return accumulatedDays;
    }

    public int toNumber() {
        return ordinal() + 1;
    }

    public static Month fromNumber(final int number) {
        if( MONTHS.length < number || number < 1 ) {
            throw new IllegalArgumentException();
        }
        return MONTHS[number - 1];
    }

    public Month offset(final int offset) {
        final int index = Math.floorMod(this.ordinal() + offset, MONTHS.length);
        return MONTHS[index];
    }

    public Month previous() {
        return offset(-1);
    }

    public Month next() {
        return offset(1);
    }
}