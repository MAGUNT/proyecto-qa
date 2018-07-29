import com.cenfotec.proyectoqa.api.Date;
import com.cenfotec.proyectoqa.api.GregorianDate;
import com.cenfotec.proyectoqa.api.Month;

public class Main {
    public static void main(String[] args) {
        final int offset  = 100;
        final int year    = 44;
        final int day     = 5;
        final Month month = Month.JANUARY;
        final Date date   = Date.of(year, month, day);
        final Date date2  = Date.of(2 * year, month, day);

        System.out.println(date);
        System.out.println(GregorianDate.isValidDate(year, month, day));
        System.out.println(date.nextDay());
        System.out.println(date.getDayOfWeek().ordinal());
        System.out.println(GregorianDate.isLeapYear(year));

        System.out.println(date.daysBetween(date2));
        System.out.println(date.futureDate(offset));
        System.out.println(date.pastDate(offset));
    }
}
