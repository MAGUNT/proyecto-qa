import com.cenfotec.proyectoqa.api.Date;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class tests
{
    @Test
    public static void  tests() {


    }

    @ParameterizedTest
    @MethodSource("range")
    void testWithRangeMethodSource(int month) {

        int[] monthOffsetValues = {0, 0, 3, 2, 5, 0, 3, 5, 1, 4, 6, 2, 4};
        assertEquals(monthOffsetValues[month], Date.monthOffset(month) % 7);
    }

    static IntStream range() {
        return IntStream.range(1, 12);
    }
}
