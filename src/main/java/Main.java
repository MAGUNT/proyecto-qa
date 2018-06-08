import com.cenfotec.proyectoqa.api.Date;
import com.cenfotec.proyectoqa.api.Month;
import java.time.LocalDate;

public class Main {


    public static void main(String[] args) {
        int year = 1600;
        int day = 1;
        Month m = Month.FEBRUARY;
        long offset = 0;

        System.out.println(Month.FEBRUARY.getDays());

        Date d = Date.of(year, m, day);
        LocalDate d2 = LocalDate.of(year, m.toNumber(), day);

        System.out.println(d.addDays(offset));
        System.out.println(d2.plusDays(offset));

        System.out.println(d.getDayOfWeek());
        System.out.println(d2.getDayOfWeek());
    }
}
