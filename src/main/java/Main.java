import com.cenfotec.proyectoqa.api.Date;
import com.cenfotec.proyectoqa.api.Month;

import java.time.LocalDate;

public class Main {

    public static void main(String[] args) {
        Date d = new Date(28, Month.FEBRUARY, 2678);
        System.out.println(d.getDayOfWeak());
        System.out.println(LocalDate.of(2678, 2, 28).getDayOfWeek());
    }
}
