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

    /**
     * <p>Este método calcula el número de días desde el año 0.
     * Para años positivos este método calcula los días bisiestos
     * desde el año cero y suma lo retornado por getYearDays.</p>
     * @return Cantidad de dias
     */
    long numOfDays();

    /**
     * <p>Calcula la cantidad de días entre 2 fechas.</p>
     * @param other otra fecha.
     * @return cantidad de días entre 2 fechas
     */
    long daysBetween(final Date other);

    /**
     * <p>Agrega días a una fecha.</p>
     * @param offset cantidad de días agregados
     * @return Nueva fecha.
     */
    Date futureDate(final long offset);

    /**
     * <p>Substrae días a una fecha.</p>
     * @param offset cantidad de días agregados
     * @return Nueva fecha.
     */
    Date pastDate(final long offset);

}
