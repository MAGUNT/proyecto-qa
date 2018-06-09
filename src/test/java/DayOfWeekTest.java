import com.cenfotec.proyectoqa.api.DayOfWeek;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * <p>Pruebas para la enumeración DayOfWeek</p>
 */
public class DayOfWeekTest {


    /**
     * <p>Prueba las clases de equivalencia y valor límite para el método indexOf.</p>
     */
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
