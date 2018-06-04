import com.cenfotec.proyectoqa.api.Month;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

public class MonthTest {


    private static final int[] ACCUMULATED_DAYS = {
            0, 31, 59, 90,
            120, 151, 181,
            212, 243, 273,
            304, 334, 365
    };
    @ParameterizedTest
    @EnumSource(Month.class)
    void accumulatedDaysTest(Month month) {
        Assertions.assertEquals(month.getAccumulatedDays(),
                ACCUMULATED_DAYS[month.ordinal()]);

    }
}
