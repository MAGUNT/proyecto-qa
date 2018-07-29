package com.cenfotec.proyectoqa.api;


/**
 * <p>
 *     GregorianDate es un objeto inmutable de fecha (Calendario gregoriano) que
 *     representa una fecha, a menudo vista como año-mes-día.
 *     También se puede acceder a otros campos de fecha, como día del año,
 *     día de la semana y semana del año. Por ejemplo, el valor "2 de octubre de 2007"
 *     se puede almacenar en un GregorianDate.
 * </p>
 */
public final class GregorianDate implements Date {
    /**
     * <p>Cantidad de días en un año no bisiesto.</p>
     */
    private static final int DAYS_IN_YEAR_NO_LEAP = 365;

    /**
     * <p>Año en que se introdujo el calendario gregoriano.</p>
     */
    private static final long GREGORIAN_CALENDAR_INIT_DATE = 1582;

    /**
     * <p>Intervalo de año en el cual ocurre un año bisiesto.</p>
     */
    private static final long LEAP_YEAR_INTERVAL = 4;
    /**
     * <p>Cantidad de años en un siglo.</p>
     */
    private static final long CENTURY_INTERVAL      = 100;
    /**
     * <p>El desfase que produce una siglo sobre los días de la semana.</p>
     */
    private static final long CENTURY_OFFSET        = 5;

    /**
     * <p>El intervalo en siglos en el cual hay un año bisiesto.</p>
     */
    private static final long LEAP_CENTURY_INTERVAL = LEAP_YEAR_INTERVAL * CENTURY_INTERVAL;


    private final int day;
    private final Month month;
    private final long year;

    /**
     *<p>Este método construye una fecha del calendario gregoriano a partir del año mes y día.
     * Tiene como precondición que la fecha sea válida, según lo que dicta el calendario gregoriano. </p>
     * @param year Año
     * @param month Enumeración con el mes.
     * @param day Día del mes.
     */

    public GregorianDate(final long year, final Month month, final int day) {
        if(month == null || !isValidDate(year, month, day)) {
            throw new IllegalArgumentException("Invalid date");
        }
        this.day   = day;
        this.month = month;
        this.year  = year;
    }

    /**
     *
     *<p>Este método construye una fecha del calendario gregoriano a partir del año mes y día.
     * Tiene como precondición que la fecha sea válida, según lo que dicta el calendario gregoriano. </p>
     * @param year Año
     * @param month Enumeración con el mes. \(1\leq mes \leq 12\)
     * @param day Día del mes
     *
     */
    public GregorianDate(final long year, final int month, final int day){
        this(year, Month.fromNumber(month), day);
    }

    /**
     * <p>Metodo para validar la fecha.</p>
     * @param year Año
     * @param month Enumeración con el mes.
     * @param day Día del mes
     * @return Si la fecha es valida.
     */
    public static boolean isValidDate(long year, Month month, int day) {
        final int leap = month == Month.FEBRUARY ? leapCount(year) : 0;
        return 0 < day
                &&  day <= (month.getDays() + leap)
                && checkYear(year);
    }

    /**
     * <p>Método estático que determina si un año es bisiesto.
     * Sin validación del año.</p>
     * @param year Año
     * @return Si el año es bisiesto.
     */
    private static boolean isLeap(final long year) {
        return year % LEAP_YEAR_INTERVAL == 0
                && (year % CENTURY_INTERVAL != 0
                || year % LEAP_CENTURY_INTERVAL == 0);
    }

    /**
     * <p>Método para validar el año.</p>
     * @param year Año
     * @return Si el año es mayor a 1582
     */
    private static boolean checkYear(final long year) {
        return year > GREGORIAN_CALENDAR_INIT_DATE;
    }

    /**
     * Método estático que determina si un año es bisiesto.
     * @param year Año
     * @return Si el año es bisiesto.
     */
    public static boolean isLeapYear(final long year) {
        if(!checkYear(year)) {
            throw new IllegalArgumentException("Invalid year");
        }
        return isLeap(year);
    }

    /**
     * <p>Una nueva fecha desplazada offset cantidad de días.</p>
     * <p>Este método utiliza fromYearDays y suma el offset. Luego un
     * ciclo corre hasta que la cantidad de días
     * sea menor o igual a la cantidad de días que hay en un año (Este siglo corre solo para offset positivos)</p>
     *
     *  <pre><code class="language-java">
     *      while (yearDays &gt; daysInYear(year)) {
     *          yearDays -= daysInYear(year);
     *          year = Math.incrementExact(year);
     *      }
     *  </code></pre>
     * <p>Luego tenemos otro ciclo similar para offsets negativos. </p>
     *
     * <pre><code class="language-java">
     *     while(yearDays &lt;= 0) {
     *         year = Math.decrementExact(year);
     *         yearDays += daysInYear(year);
     *     }
     *  </code></pre>
     *
     *  Ambos ciclos recalculan el año agregándole o restándole a los días del año la cantidad de días que hay en este.
     *  En cada ciglo se mantiene invariante el total neto de días.  Finalmente al terminar ambos ciclos, se tiene los días del año
     *  y el año talque \(1\leq dias \leq 366\).
     *
     * @param offset Dias que se quieren sumar o restar.
     * @return Una nueva fecha desplazada offset cantidad de dias.
     */
    @Override
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

    /**
     * <p>Este método agrega un día a la fecha actual.</p>
     * @return La fecha del día siguiente.
     */
    @Override
    public GregorianDate nextDay() {
        return addDays(1);
    }

    /**
     * <p>Es la operación inversa del método daysInYear(final long year).
     *  Calcula una fecha a partir del año y los días de ese año. </p>
     *
     *  <p>
     *      Empezando desde diciembre se busca el mes en el cual los días acumulados sean menores a los días del año.
     *      (Esto sirve porque los valores acumulados se recorren de forma descendiente).
     *  </p>
     *   <pre><code class="language-java">
     *      Month month = Month.DECEMBER;
     *      int leap    = leapCount(year);
     *
     *      while(daysOfYear &lt;= (month.getAccumulatedDays() + leap)) {
     *          month = month.previous();
     *          leap  = !greaterThanFebruary(month) ? 0 : leap;
     *      }
     *   </code></pre>
     *
     *  <p>Finalmente se calcula los días de la fecha, restando los días acumulados y 1 en caso de que el mes sea mayor a febrero y el año bisiesto</p>
     *
     *   <pre><code class="language-java">
     *     int days = daysOfYear
     *                 - month.getAccumulatedDays()
     *                 - leap;
     *     </code></pre>
     * @param daysOfYear Días del año
     * @param year Año
     * @return Fecha gregoriana.
     */
    public static GregorianDate fromYearDays(final int daysOfYear, final long year) {
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

    /**
     * <p>Calcula la cantidad de días que hay en un año en específico.</p>
     * @param year Año
     * @return Cantidad de días en un año: 365 para años normales y 366 para bisiestos.
     */
    private int daysInYear(final long year) {
        return DAYS_IN_YEAR_NO_LEAP + leapCount(year);
    }

    /**
     * <p>Este metodo calcula la cantidad de dias del año.</p>
     * @return Dias del año. \(1\leq dias \leq 366\)
     */
    @Override
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
     *  <p> Además, se utiliza Math.floorDiv(yearCopy, CENTURY_INTERVAL) ya que la división normal trunca hacia cero mientras que esta operación
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
    @Override
    public DayOfWeek getDayOfWeek() {
        final long yearCopy = year - (!greaterThanFebruary(month) ? 1 : 0);
        final long century  = Math.floorDiv(yearCopy, CENTURY_INTERVAL);
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
     * @return Si \(2 &lt; month\).
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
        return CENTURY_OFFSET * Math.floorMod(century, LEAP_YEAR_INTERVAL);
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
        final long centuryRem       = Math.floorMod(dateYear , CENTURY_INTERVAL);
        final long centuryLeapYears = (centuryRem / LEAP_YEAR_INTERVAL);
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

    /**
     * <p>Método retorna una representación con el formato (YYYY, MM, DD)</p>
     * @return (YYYY, MM, DD).
     */
    @Override
    public String toString() {
        return String.format("(%04d, %02d, %02d)",
                year, month.toNumber(), day);
    }


    /**
     * <p>Método equals.</p>
     * @param other El objeto a comparar.
     * @return Si son iguales.
     */
    @Override
    public boolean equals(Object other) {
        if(this == other) {
            return true;
        }
        if(!(other instanceof GregorianDate)) {
            return false;
        }
        GregorianDate date = (GregorianDate) other;
        return  this.year == date.year
                && this.day == date.day
                && this.month == date.month;
     }

    /**
     * <p>Este método calcula el número de días desde el año 0.</p>
     *
     * Este condicion es para tomar en cuenta años negativos.
     * <pre><code class="language-java">
     *     (year <= 0 ? 0 : 1);
     *     </code></pre>
     *
     * <p>Para años positivos este método calcula los días bisiestos
     * desde el año cero y suma lo retornado por getYearDays.</p>
     * @return Cantidad de dias
     */
    @Override
    public long numOfDays() {
        final long isYearPositive = (year <= 0 ? 0 : 1);
        final long yearCopy       = year - isYearPositive;
        final long leapDays       = leapDaysInYear(yearCopy);
        final long yearZero       = (isYearPositive
                * (DAYS_IN_YEAR_NO_LEAP + 1));

        return  yearZero
                + yearCopy
                * DAYS_IN_YEAR_NO_LEAP
                + leapDays
                + getYearDays();
    }

    /**
     * <p>Calcula la cantidad de días bisiesto de un año
     * (asume que el mes es mayor a febrero)</p>
     * @param year Año
     * @return Cantidad de dias
     */
    private long leapDaysInYear(final long year) {
        return (year / LEAP_YEAR_INTERVAL)
                - (year / CENTURY_INTERVAL)
                + (year / LEAP_CENTURY_INTERVAL);
    }

    /**
     * <p>Calcula la cantidad de días entre 2 fechas.</p>
     * @param other otra fecha.
     * @return cantidad de días entre 2 fechas
     */
    @Override
    public long daysBetween(final Date other) {
        return Math.abs(other.numOfDays() - numOfDays());
    }


    /**
     * <p>Agrega días a una fecha.</p>
     * @param offset cantidad de días agregados
     * @return Nueva fecha.
     */
    @Override
    public Date futureDate(final long offset) {
        checkOffset(offset);
        return addDays(offset);
    }

    /**
     * <p>Substrae días a una fecha.</p>
     * @param offset cantidad de días agregados
     * @return Nueva fecha.
     */
    @Override
    public Date pastDate(final long offset) {
        checkOffset(offset);
        return addDays(-offset);
    }

    /**
     * <p>Verifica que la cantidad de días no sea negativa.</p>
     * @param offset cantidad de días agregados
     */
    private void checkOffset(final long offset) {
        if (offset < 0) {
            throw new IllegalArgumentException("Offset can't be less than 0");
        }
    }

    /**
     * <p>Método hashCode.</p>
     * @return Código hash.
     */
     @Override
     public int hashCode() {
         final int intBits = 32;
         final int prime   = 31;
         int result        = 17;
         result = prime * result + day;
         result = prime * result + (int) (year ^ (year >>> intBits));
         result = prime * result + month.toNumber();
         return result;
     }
}
