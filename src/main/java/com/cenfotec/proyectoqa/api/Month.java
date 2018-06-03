package com.cenfotec.proyectoqa.api;

public enum Month {
    JANUARY,
    FEBRUARY,
    MARCH,
    APRIL,
    MAY,
    JUNE,
    JULY,
    AUGUST,
    SEPTEMBER,
    OCTOBER,
    NOVEMBER,
    DECEMBER;


    private static final int[] ACCUMULATED_DAYS = {
            0, 31, 59, 90,
            120, 151, 181,
            212, 243, 273,
            304, 334, 365
    };
    private static final Month[] MONTHS = Month.values();

    public int getDays() {
        int index = ordinal();
        return ACCUMULATED_DAYS[index + 1]
                - ACCUMULATED_DAYS[index];
    }

    public int getAccumulatedDays() {
        return ACCUMULATED_DAYS[ordinal()];
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
        return MONTHS[Math.floorMod(this.ordinal() + offset, MONTHS.length)];
    }

    public Month previous() {
        return offset(-1);
    }

    public Month next() {
        return offset(1);
    }
}