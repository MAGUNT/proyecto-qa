import com.cenfotec.proyectoqa.api.Date;
import com.cenfotec.proyectoqa.api.GregorianDate;
import com.cenfotec.proyectoqa.api.Month;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DateTest {

    @Test
    void addDaysOverflowYearTest() {
        final long offset = 366;
        final int day     = 1;
        final Month month = Month.FEBRUARY;

        Assertions.assertThrows(ArithmeticException.class, () ->
                Date.of(Long.MAX_VALUE, month, day).addDays(offset));
    }

    @Test
    void addDaysOverflowOffsetTest() {
        final long year   = 1900;
        final int day     = 1;
        final Month month = Month.FEBRUARY;

        Assertions.assertThrows(ArithmeticException.class,
                () -> Date.of(year, month, day).addDays(Long.MAX_VALUE));
    }



}
