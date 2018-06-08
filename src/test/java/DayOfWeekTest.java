import com.cenfotec.proyectoqa.api.DayOfWeek;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DayOfWeekTest {


    @Test
    void ofIndexTest() {
       final int lowerBoundary = 0;
       final int upperBoundary = 6;
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> DayOfWeek.ofIndex(lowerBoundary - 1));

        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> DayOfWeek.ofIndex(upperBoundary + 1));

        Assertions.assertEquals(
                DayOfWeek.ofIndex(upperBoundary),
                DayOfWeek.SATURDAY);
        Assertions.assertEquals(
                DayOfWeek.ofIndex(lowerBoundary),
                DayOfWeek.SUNDAY);
    }
}
