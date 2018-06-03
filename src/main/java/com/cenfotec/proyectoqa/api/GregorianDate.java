package com.cenfotec.proyectoqa.api;

public final class GregorianDate implements Date {
    private static final int DAYS_IN_YEAR_NO_LEAP         = 365;
    private static final long GREGORIAN_CALENDAR_INIT_DATE = 1582;
    private static final long LEAP_YEAR_INTERVAL = 4;
    private static final long CENTURY_INTERVAL   = 4;
    private static final long CENTURY            = 100;
    private static final long CENTURY_OFFSET     = 5;

    private final int day;
    private final Month month;
    private final long year;

    public GregorianDate(final long year, final Month month, final int day) {
        if(!isValidDate(year, month, day)) {
            throw new IllegalArgumentException("Invalid date");
        }
        this.day   = day;
        this.month = month;
        this.year  = year;
    }

    private boolean isValidDate(long year, Month month, int day) {
        int leap = month == Month.FEBRUARY ? leapCount(year) : 0;
        return 0 < day &&  day <= (month.getDays() + leap)
                && year > GREGORIAN_CALENDAR_INIT_DATE;
    }

    public static boolean isLeapYear(final long year) {
        return (year % LEAP_YEAR_INTERVAL == 0 && year % CENTURY != 0)
                || year % (CENTURY_INTERVAL * CENTURY) == 0;
    }

    public boolean isLeapYear() {
        return isLeapYear(year);
    }

    public GregorianDate addDays(final long offset) {
        long yearDays = Math.addExact(getYearDays(), offset);
        long year     = this.year;

        while (yearDays > daysInYear(year)) {
            yearDays -= daysInYear(year);
            year = Math.incrementExact(year);
        }
        while(yearDays <= 0) {
            year = Math.decrementExact(year);
            yearDays += daysInYear(year);
        }
        return fromYearDays((int) yearDays, year);
    }

    private GregorianDate fromYearDays(final int daysOfYear, final long year) {
        Month month = Month.DECEMBER;
        int leap    = leapCount(year);

        while(daysOfYear <= (month.getAccumulatedDays() + leap)) {
            month = month.previous();
            leap  = !greaterThanFebruary(month) ? 0 : leap;
        }
        int days = daysOfYear
                - month.getAccumulatedDays()
                - leap;
        return new GregorianDate(year, month, days);
    }

    private int daysInYear(final long year) {
        return DAYS_IN_YEAR_NO_LEAP + leapCount(year);
    }

    public int getYearDays() {
        int days =  month.getAccumulatedDays() + day;
        if (greaterThanFebruary(month)) {
            days += leapCount(year);
        }
        return  days;
    }

    /***
     * <p>  Este método se basa en un algoritmo de Gauss para encontrar el día de la semana del 1 de enero de un año.
     * Lo que nos importa en este algoritmo es el desfase producido por los años y meses según un punto de referencia.
     * Para poder explicar mejor este algoritmo, definimos a la función \(Pe :\mathbb{Z} \mapsto \left \{ 0,1,2,..,6 \right \}\)
     * que calcula el día de la semana del primero de enero, según un año. El 0 corresponde al domingo, 1 a lunes etc.
     * También definimos c y g como \(y= 100\cdot c+g\).
     * Antes de explicar el código se debe entender las siguientes propiedades del calendario gregoriano:</p>
     *
     * <ul>
     *     <li>
     *         <p>Hay una periodicidad de 400 años esto quiere decir que \(Pe(y) = Pe(y + i\cdot 400) \forall i\in\mathbb{Z}\).
     *             Podemos explicar porque existe esta periodicidad verificando cuanto desfasé hay en 100 años.
     *             Primero vemos que la contribución de un año no bisiesto es \(365\equiv 1 \thinspace mod \thinspace 7\),
     *             por lo tanto cada año agrega un desfase de 1. Como hay 24 días bisiestos en 100 años tenemos que
     *             \(24 +100\equiv 5 \thinspace mod \thinspace 7\) o en otras palabras cada 100 años hay un desfase de 5 días.
     *             Finalmente, para 400 tenemos que \(4\cdot 5+1 \equiv 0 \thinspace mod \thinspace 7\),
     *             dentro de la formula anterior se agrega 1 para tomar en cuenta el año bisiesto que es
     *             divisible entre 400, que debe estar dentro de ese rango.
     *             Tomando lo anterior en cuenta podemos calcular el desfase producido por el siglo con la siguiente función:</p>
     *
     *             <pre><code class="language-java">
     *                private int cycleOffset(final int c) {
     *                    return 5 * Math.floorMod(c, 4);
     *                }
     *             </code></pre>
     *
     *     </li>
     *
     *     <li>
     *         Podemos calcular el número de años bisiestos adentro de un siglo con la siguiente función: \(f(g)=\left\lfloor\dfrac{g}{4}\right\rfloor\).
     *         Esta fórmula funciona debido a que un año que es divisible entre 4 y no 100, es bisiesto. Como cada año contribuye a un día de desfase,
     *         para calcular el desfase total ocasionado por g podemos utilizar la siguiente función:
     *
     *         <pre><code class="language-java">
     *             private int centuryReminderOffset(final int dateYear) {
     *                 int g = Math.floorMod(dateYear , 100);
     *                 int centuryLeapYears = (g / 4);
     *                 return g + centuryLeapYears;
     *             }
     *         </code></pre>
     *
     *     </li>
     * </ul>
     *
     *<p>Podemos utilizar estas dos funciones anteriores para definir \(Pe(y)\) en código:</p>
     * <pre><code class="language-java">
     *       public int firstOfJanuary(int year) {
     *         final int c  = Math.floorDiv(year, 100);
     *         return (centuryReminderOffset(year) + cycleOffset(c)) % 7;
     *     }
     * </code></pre>
     * <p>
     *     Esta función calcula cero para el año cero, pero según el calendario gregoriano debería ser sábado.
     *     Esto se debe a que esta función es incorrecta para cualquier año bisiesto, porque el primero de enero
     *     es antes de marzo y estamos contando este año como bisiesto. Por esta razón debemos redefinir g y c.
     * </p>
     * <p>
     *     Para extender este algoritmo debemos tomar en cuenta que dentro de un año bisiesto hay un incremento del desfase solo si el mes (m) es mayor a febrero.
     *     Por eso le restamos 1 al año cuando el mes es menor o igual a febrero.
     *     Para \(m= 1,2,\cdots,12\), definimos el siglo (c) y los dos últimos dígitos de un año (g) de la siguiente manera:
     * </p>
     * <ul  style="list-style-type: none;">
     *     <li>
     *         Para \(m\geqslant 3\)  tenemos que \(y= 100\cdot c+g\).
     *     </li>
     *     <li>
     *         Para \(m= 1,2\)  tenemos que \(y-1= 100\cdot c+g\).
     *     </li>
     *     <li>
     *         Talque \(0\leq g\leq 99\)
     *     </li>
     * </ul>
     *
     * <p>Finalmente para compensar el hecho de que estamos calculando el año anterior, agregamos 1 a la funcion:</p>
     *  <pre><code class="language-java">
     *     public int firstOfJanuary(int year) {
     *         final int yearCopy =  year - 1;
     *         final int c  = Math.floorDiv(yearCopy, 100);
     *         return 1 + (centuryReminderOffset(yearCopy) + cycleOffset(c)) % 7;
     *     }
     *  </code></pre>
     *  <p>
     *      Para finalizar calculamos el desfase de los meses. Definimos la función \(dm: \left \{ 1,2,3 ...,12 \right \}\mapsto \left \{ 1,2,3 ...,31 \right \}\)
     *      para determinar los días que contiene un mes. Luego definimos los días acumulados de un mes como \(a(m) = \sum_{i=1}^{m-1}dm(i)\).
     *      Tenemos que restar 1 al acumulado de todos los meses mayores a febrero, porque vamos a sumar los días del mes. Recordemos
     *      que cuando \(y= 100\cdot c+g\) los días del mes empiezan en cero. El código seria el siguiente:
     *  </p>
     *  <pre><code class="language-java">
     *      private int monthOffset(final int month) {
     *         return a(month) - (2 &lt; month ? 1 : 0);
     *      }
     *  </code></pre>
     *  <p>El modulo con respecto a 7 de este código es equivalente a \(\left\lfloor 2.6 \cdot m +0.2 \right\rfloor\) ,si tomamos a marzo como el mes número 1.
     *  Recopilando lo mencionado, a continuación, se muestra el código para calcular el día de la semana correspondiente a una fecha:</p>
     *
     *  <pre><code class="language-java">
     *      public DayOfWeek getDayOfWeek2(final int day, final int month, final int year) {
     *          final int yearCopy = year - (month &lt; 3 ? 1 : 0);
     *          final int century  = Math.floorDiv(yearCopy, 100);
     *          return  (day
     *                  + cycleOffset(century)
     *                  + monthOffset(month)
     *                  + centuryReminderOffset(yearCopy))
     *                  % DayOfWeek.daysInWeek();
     *      }
     *  </code></pre>
     *
     *  <p> Nota: Se utiliza Math.floorMod para remplazar el operador % ya que para valores negativos del año (A pesar de que solo vamos a usar años
     *  mayores a 1582) queremos que el modulo sea positivo, por ejemplo: </p>
     *
     *      <pre><code class="language-java">
     *          -48 % 5; // -3
     *          Math.floorMod(-48 , 5); // 2
     *      </code></pre>
     *
     *  <p> Además, se utiliza Math.floorDiv(yearCopy, CENTURY) ya que la división normal trunca hacia cero mientras que esta operación
     *  trunca hacia el valor menor. (Igual esto es solo importante para años menores a cero) por ejemplo: </p>
     *       <pre><code class="language-java">
     *           Math.floorDiv(-4, 100); // -1
     *           -4 / 100; // 0
     *       </code></pre>
     * <p>Esto es importante para calcular posteriormente el desfase del ciclo, por ejemplo: </p>
     *       <pre><code class="language-java">
     *           Math.floorMod(-1, 4); // 3
     *       </code></pre>
     *
     * @return Día correspondiente a la fecha gregoriana.
     *
     *
     */
    public DayOfWeek getDayOfWeek() {
        final long yearCopy = year - (!greaterThanFebruary(month) ? 1 : 0);
        final long century  = Math.floorDiv(yearCopy, CENTURY);
        final long week = (day
                + cycleOffset(century)
                + monthOffset(month)
                + centuryReminderOffset(yearCopy))
                % DayOfWeek.daysInWeek();

        return DayOfWeek.ofIndex((int) week);
    }

    /**
     * <p>Esta función retorna 1 si el año es bisiesto o de lo contrario 0. </p>
     * @param year Año.
     * @return  1 si el año es bisiesto o de lo contrario 0.
     */
    private static int leapCount(final long year) {
        return isLeapYear(year) ? 1 : 0;
    }


    /**
     * <p>Esta función verifica que el mes sea posterior a febrero.</p>
     * @param month Mes.
     * @return Si \(month &lt; 2\).
     */
    private static boolean greaterThanFebruary(final Month month) {
        return month.compareTo(Month.FEBRUARY) > 0;
    }

    /**
     * <p>Esta función calcula el desplazamiento producido por el siglo</p>
     * <p>Hay una periodicidad de 400 años esto quiere decir que \(Pe(y) = Pe(y + i\cdot 400) \forall i\in\mathbb{Z}\).
     * Podemos explicar porque existe esta periodicidad verificando cuanto desfasé hay en 100 años.
     * Primero vemos que la contribución de un año no bisiesto es \(365\equiv 1 \thinspace mod \thinspace 7\),
     * por lo tanto cada año agrega un desfase de 1. Como hay 24 días bisiestos en 100 años tenemos que
     * \(24 +100\equiv 5 \thinspace mod \thinspace 7\) o en otras palabras cada 100 años hay un desfase de 5 días.
     * Finalmente, para 400 tenemos que \(4\cdot 5+1 \equiv 0 \thinspace mod \thinspace 7\),
     * dentro de la formula anterior se agrega 1 para tomar en cuenta el año bisiesto que es
     * divisible entre 400, que debe estar dentro de ese rango.
     * Tomando lo anterior en cuenta podemos calcular el desfase producido por el siglo con la siguiente función:</p>
     * <pre><code class="language-java">
     *     private int cycleOffset(final int c) {
     *         return 5 * Math.floorMod(c, 4);
     *     }
     * </code></pre>
     * @param century El siglo del año definido como: \(year= 100\cdot century + g\)
     * @return El desplazamiento de los días de la semana producido por el siglo.
     */

    private long cycleOffset(final long century) {
        return CENTURY_OFFSET * Math.floorMod(century, CENTURY_INTERVAL);
    }

    /**   <p> Esta función  descompone el año como \(dateYear= 100\cdot century + g\)
     *    y calcula el desplazamiento de los días de la semana producido por g.
     *    </p>
     *    Podemos calcular el número de años bisiestos adentro de un siglo con la siguiente función: \(f(g)=\left\lfloor\dfrac{g}{4}\right\rfloor\).
     *    Esta fórmula funciona debido a que un año que es divisible entre 4 y no 100, es bisiesto. Como cada año contribuye a un día de desfase,
     *    para calcular el desfase total ocasionado por g podemos utilizar la siguiente función:
     *      <pre><code class="language-java">
     *          private int centuryReminderOffset(final int dateYear) {
     *              int g = Math.floorMod(dateYear , 100);
     *              int centuryLeapYears = (g / 4);
     *          return g + centuryLeapYears;
     *      }
     *      </code></pre>
     *
     * @param dateYear Año.
     * @return El desplazamiento de los días de la semana producido por los 2 últimos dígitos del año.
     */
    private long centuryReminderOffset(final long dateYear) {
        long centuryRem       = Math.floorMod(dateYear , CENTURY);
        long centuryLeapYears = (centuryRem / LEAP_YEAR_INTERVAL);
        return centuryRem + centuryLeapYears;
    }


    /**
     * <p>Esta función calcula el desfase de días de la semana producidos por el mes.</p>
     * <p>
     *     Definimos la función \(dm: \left \{ 1,2,3 ...,12 \right \}\mapsto \left \{ 1,2,3 ...,31 \right \}\)
     *     para determinar los días que contiene un mes. Luego definimos los días acumulados de un mes como \(a(m) = \sum_{i=1}^{m-1}dm(i)\).
     *     Tenemos que restar 1 al acumulado de todos los meses mayores a febrero, porque vamos a sumar los días del mes. Recordemos
     *     que cuando \(y= 100\cdot c+g\) los días del mes empiezan en cero. El código seria el siguiente:
     * </p>
     *  <pre><code class="language-java">
     *      private int monthOffset(final int month) {
     *         return a(month) - (2 &lt; month ? 1 : 0);
     *      }
     *  </code></pre>
     * @param month Mes.
     * @return El desfase de días de la semana producidos por el mes.
     */
    private int monthOffset(final Month month) {
        return month.getAccumulatedDays()
                - (greaterThanFebruary(month) ? 1 : 0);
    }

    @Override
    public String toString() {
        return String.format("(%d, %s, %d)",
                day, month.toNumber(), year);
    }
}
