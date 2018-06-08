package com.cenfotec.proyectoqa.api;


/**
 *
 *<p>El mes es una enumeración que representa los 12 meses del año: enero, febrero, marzo, abril, mayo,
 * junio, julio, agosto, septiembre, octubre, noviembre y diciembre.
 * Además del nombre enum textual, cada mes del año tiene un valor int.
 * El valor int sigue el uso normal y el estándar ISO-8601, del 1 (enero) al 12 (diciembre).
 * Se recomienda que las aplicaciones utilicen la enumeración en lugar del valor int para garantizar la claridad del código.
 * </p>
 *<p> Se debe utilizar getNumber() para conseguir su representación numérica (no ordinal())</p>
 */
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

    /**
     * <p>Se calcula los valores acumulados estáticamente.</p>
     */
    static {
        int accumulatedCount = 0;
        for (Month month : MONTHS) {
            month.accumulatedDays = accumulatedCount;
            accumulatedCount += month.getDays();
        }

    }

    /**
     * @param days Días que contiene el mes (28, 30 o 31).
     */
    Month(final int days) {
        this.days = days;
    }

    /**
     * @return Días que contiene el mes (28, 30 o 31).
     */
    public int getDays() {
        return days;
    }

    /**
     * <p>Este método retorna los días acumulados de los meses anteriores.</p>
     * @return Días acumulados
     */
    public int getAccumulatedDays() {
        return accumulatedDays;
    }

    /**
     * <p>Calcula la representación numérica del mes.</p>
     * @return Representación numérica del mes.
     */
    public int toNumber() {
        return ordinal() + 1;
    }

    /**
     * <p>Método que mapea la representación numérica a su respectivo mes. </p>
     * @param number Representación numérica del mes.
     * @return Enumeración que corresponde a ese mes.
     */
    public static Month fromNumber(final int number) {
        if( MONTHS.length < number || number < 1 ) {
            throw new IllegalArgumentException();
        }
        return MONTHS[number - 1];
    }

    /**
     * <p>Calcula la enumeración que corresponde al desplazamiento dado por offset.</p>
     * @param offset Desplazamiento negativo o positivo en meses con respecto al mes.
     * @return La enumeración que corresponde al desplazamiento.
     */
    public Month offset(final int offset) {
        final int index = Math.floorMod(this.ordinal() + offset, MONTHS.length);
        return MONTHS[index];
    }

    /**
     * <p>Calcula el mes anterior.</p>
     * @return Enumeración correspondiente al mes anterior.
     */
    public Month previous() {
        return offset(-1);
    }

    /**
     * <p>Calcula el siguiente mes.</p>
     * @return Enumeración correspondiente al mes siguiente.
     */
    public Month next() {
        return offset(1);
    }
}