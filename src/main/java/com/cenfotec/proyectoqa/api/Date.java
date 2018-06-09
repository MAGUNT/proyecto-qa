package com.cenfotec.proyectoqa.api;

/**
 * Interface que representa una fecha.
 */
public interface Date {
    /**
     *
     * Este método agrega o substrae días a una fecha.
     * @param offset Desplazamiento en días
     * @return Nueva fecha con el desplazamiento en días.
     */
    GregorianDate addDays(final long offset);

    /**
     * <p>Este método agrega un día a la fecha actual.</p>
     * @return La fecha del día siguiente.
     */
    GregorianDate nextDay();

    /**
     * <p>Método estático que determina si un año es bisiesto.</p>
     * @return Si el año es bisiesto.
     */
    boolean isLeapYear();

    /**
     * <p>Este metodo calcula la cantidad de dias del año.</p>
     * @return Dias del año. \(1\leq dias \leq 366\)
     */
    int getYearDays();

    /**
     * <p>Este método calcula el día de la semana correspondiente a la fecha.</p>.
     * @return Día de la semana
     */
    DayOfWeek getDayOfWeek();

    /**
     *
     * @return Año
     */
    long getYear();

    /**
     *
     * @return Día
     */
    int getDay();

    /**
     *
     * @return Mes
     */
    Month getMonth();

    /**
     * <p>Fabrica estática para crear alguna implementación concreta de la interface.</p>
     * @param year Año
     * @param month Mes
     * @param day Día
     * @return Fecha
     */
    static Date of(long year, Month month, int day) {
        return new GregorianDate(year, month, day);
    }

    /**
     * <p>Fabrica estática para crear alguna implementación concreta de la interface.</p>
     * @param year Año
     * @param month Mes
     * @param day Día
     * @return Fecha
     */
    static Date of(long year, int month, int day) {
        return new GregorianDate(year, month, day);
    }


}
