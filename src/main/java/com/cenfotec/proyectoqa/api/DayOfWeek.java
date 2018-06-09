package com.cenfotec.proyectoqa.api;

/**
 * <p>DayOfWeek es una enumeración que representa los 7 días de la semana: domingo, lunes, martes, miércoles, jueves, viernes y sábado.
 * Además del nombre textual de la enumeración cada una tiene un valor int asociado, del 0 (domingo) al 7(sabado).
 * Se debe utilizar el método ordinal() para conseguir la representación numérica.</p>
 */
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


    /**
     * <p>Mapea la representación numérica a su enumeración equivalente.</p>
     * @param dayOfWeekNumber Representación numérica del día de la semana, del 0 (domingo) al 7(sábado)
     * @return Enumeración con el día de la semana.
     */
    public static DayOfWeek ofIndex(final int dayOfWeekNumber) {
        if (dayOfWeekNumber < MIN_INDEX || dayOfWeekNumber > MAX_INDEX) {
            throw new IllegalArgumentException();
        }
        return DAYS_IN_WEEK[dayOfWeekNumber];
    }

    /**
     * <p>Cantidad de días en un semana.</p>
     * @return Número de días en una semana.
     */
    public static int daysInWeek() {
        return DAYS_IN_WEEK.length;
    }
}
