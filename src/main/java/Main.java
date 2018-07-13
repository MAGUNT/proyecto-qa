import com.cenfotec.proyectoqa.api.Date;
import com.cenfotec.proyectoqa.api.GregorianDate;
import com.cenfotec.proyectoqa.api.Month;

public class Main {
    public static void main(String[] args) {
        final int year    = 1644;
        final int day     = 25;
        final Month month = Month.FEBRUARY;
        final Date date   = Date.of(year, month, day);

        System.out.println(date);
        System.out.println(GregorianDate.isValidDate(year, month, day));
        System.out.println(date.nextDay());
        System.out.println(date.getDayOfWeek().ordinal());
        System.out.println(GregorianDate.isLeapYear(year));
    }
}
