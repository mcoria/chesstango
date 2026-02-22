package net.chesstango.search.smart.alphabeta.core.filters.once;

import net.chesstango.search.smart.alphabeta.core.filters.once.AspirationWindows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    public void testBoundPositiveCycle_00() {
        assertEquals(1, aspirationWindows.diffBound(Integer.MAX_VALUE, Integer.MAX_VALUE - 1, 0));
        assertEquals(2, aspirationWindows.diffBound(Integer.MAX_VALUE, Integer.MAX_VALUE - 2, 0));
        assertEquals(3, aspirationWindows.diffBound(Integer.MAX_VALUE, Integer.MAX_VALUE - 3, 0));
        assertEquals(4, aspirationWindows.diffBound(Integer.MAX_VALUE, Integer.MAX_VALUE - 4, 0));
        assertEquals(5, aspirationWindows.diffBound(Integer.MAX_VALUE, Integer.MAX_VALUE - 5, 0));
        assertEquals(6, aspirationWindows.diffBound(Integer.MAX_VALUE, Integer.MAX_VALUE - 6, 0));
        assertEquals(7, aspirationWindows.diffBound(Integer.MAX_VALUE, Integer.MAX_VALUE - 7, 0));
        assertEquals(8, aspirationWindows.diffBound(Integer.MAX_VALUE, Integer.MAX_VALUE - 8, 0));
        assertEquals(9, aspirationWindows.diffBound(Integer.MAX_VALUE, Integer.MAX_VALUE - 9, 0));
        assertEquals(10, aspirationWindows.diffBound(Integer.MAX_VALUE, Integer.MAX_VALUE - 10, 0));
    }

    @Test
    public void testBoundPositiveCycle_16() {
        assertEquals(1, aspirationWindows.diffBound(Integer.MAX_VALUE, Integer.MAX_VALUE - 1, 16));
        assertEquals(2, aspirationWindows.diffBound(Integer.MAX_VALUE, Integer.MAX_VALUE - 2, 16));
        assertEquals(3, aspirationWindows.diffBound(Integer.MAX_VALUE, Integer.MAX_VALUE - 3, 16));
        assertEquals(4, aspirationWindows.diffBound(Integer.MAX_VALUE, Integer.MAX_VALUE - 4, 16));
        assertEquals(5, aspirationWindows.diffBound(Integer.MAX_VALUE, Integer.MAX_VALUE - 5, 16));
        assertEquals(6, aspirationWindows.diffBound(Integer.MAX_VALUE, Integer.MAX_VALUE - 6, 16));
        assertEquals(7, aspirationWindows.diffBound(Integer.MAX_VALUE, Integer.MAX_VALUE - 7, 16));
        assertEquals(8, aspirationWindows.diffBound(Integer.MAX_VALUE, Integer.MAX_VALUE - 8, 16));
        assertEquals(9, aspirationWindows.diffBound(Integer.MAX_VALUE, Integer.MAX_VALUE - 9, 16));
        assertEquals(10, aspirationWindows.diffBound(Integer.MAX_VALUE, Integer.MAX_VALUE - 10, 16));
    }

    @Test
    public void testBoundPositiveCycle_99() {
        assertEquals(1, aspirationWindows.diffBound(Integer.MAX_VALUE, Integer.MAX_VALUE - 1, 99));
        assertEquals(2, aspirationWindows.diffBound(Integer.MAX_VALUE, Integer.MAX_VALUE - 2, 99));
        assertEquals(3, aspirationWindows.diffBound(Integer.MAX_VALUE, Integer.MAX_VALUE - 3, 99));
        assertEquals(4, aspirationWindows.diffBound(Integer.MAX_VALUE, Integer.MAX_VALUE - 4, 99));
        assertEquals(5, aspirationWindows.diffBound(Integer.MAX_VALUE, Integer.MAX_VALUE - 5, 99));
        assertEquals(6, aspirationWindows.diffBound(Integer.MAX_VALUE, Integer.MAX_VALUE - 6, 9));
        assertEquals(7, aspirationWindows.diffBound(Integer.MAX_VALUE, Integer.MAX_VALUE - 7, 99));
        assertEquals(8, aspirationWindows.diffBound(Integer.MAX_VALUE, Integer.MAX_VALUE - 8, 99));
        assertEquals(9, aspirationWindows.diffBound(Integer.MAX_VALUE, Integer.MAX_VALUE - 9, 9));
        assertEquals(10, aspirationWindows.diffBound(Integer.MAX_VALUE, Integer.MAX_VALUE - 10, 9));
    }

    @Test
    public void testBoundNegative() {
        assertEquals(1, aspirationWindows.diffBound(-Integer.MAX_VALUE, 1 - Integer.MAX_VALUE, 0));
        assertEquals(2, aspirationWindows.diffBound(-Integer.MAX_VALUE, 2 - Integer.MAX_VALUE, 0));
        assertEquals(3, aspirationWindows.diffBound(-Integer.MAX_VALUE, 3 - Integer.MAX_VALUE, 0));
        assertEquals(4, aspirationWindows.diffBound(-Integer.MAX_VALUE, 4 - Integer.MAX_VALUE, 0));
        assertEquals(5, aspirationWindows.diffBound(-Integer.MAX_VALUE, 5 - Integer.MAX_VALUE, 0));
        assertEquals(6, aspirationWindows.diffBound(-Integer.MAX_VALUE, 6 - Integer.MAX_VALUE, 0));
        assertEquals(7, aspirationWindows.diffBound(-Integer.MAX_VALUE, 7 - Integer.MAX_VALUE, 0));
        assertEquals(8, aspirationWindows.diffBound(-Integer.MAX_VALUE, 8 - Integer.MAX_VALUE, 0));
        assertEquals(9, aspirationWindows.diffBound(-Integer.MAX_VALUE, 9 - Integer.MAX_VALUE, 0));
        assertEquals(10, aspirationWindows.diffBound(-Integer.MAX_VALUE, 10 - Integer.MAX_VALUE, 0));
    }

}

