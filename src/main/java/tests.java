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


    }

    static IntStream range() {
        return IntStream.range(1, 12);
    }
}
