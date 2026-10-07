package net.chesstango.search.alphabeta.root.filters;

import net.chesstango.evaluation.Evaluator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static net.chesstango.search.alphabeta.root.filters.AspirationWindows.MAX_SHIFT;
import static net.chesstango.search.alphabeta.root.filters.AspirationWindows.OFFSET;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Mauricio Coria
 */
public class AspirationWindowsTest {

    private AspirationWindows aspirationWindows;


    @BeforeEach
    public void setup() {
        aspirationWindows = new AspirationWindows();
    }


    @Test
    public void test_lowerBound() {
        assertEquals(-511, aspirationWindows.lowerBound(Evaluator.INFINITE_NEGATIVE, 0, 0));
        assertEquals(-2147483647, aspirationWindows.lowerBound(Evaluator.INFINITE_NEGATIVE, 0, MAX_SHIFT));
    }

    @Test
    public void test_upperBound() {
        assertEquals(511, aspirationWindows.upperBound (Evaluator.INFINITE_POSITIVE, 0, 0));
        assertEquals(2147483647, aspirationWindows.upperBound(Evaluator.INFINITE_POSITIVE, 0, MAX_SHIFT));
    }


    @Test
    @Disabled
    public void test_Delta() {
       // System.out.println(1 << 17);
        for (int i = 0; i <= MAX_SHIFT; i++) {
            System.out.printf("Cycle %d - Delta: %d%n", i, aspirationWindows.delta(i));
        }

        System.out.println(Evaluator.WON);
    }

}

