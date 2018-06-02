import com.cenfotec.proyectoqa.api.DayOfWeek;
import com.cenfotec.proyectoqa.api.GregorianDate;
import com.cenfotec.proyectoqa.api.Month;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.stream.IntStream;

public class Main {


    public static void main(String[] args) {
        int year = 0;
        int day = 1;
        Month m = Month.JANUARY;
        int offset = 0;


        GregorianDate d = new GregorianDate(day, m, year);
        LocalDate d2 = LocalDate.of(year, m.toNumber(), day);

        System.out.println(d.addDays(offset));
        System.out.println(d2.minusDays(-offset));

        System.out.println(d.getDayOfWeek());
        System.out.println(d2.getDayOfWeek());
    }
}
