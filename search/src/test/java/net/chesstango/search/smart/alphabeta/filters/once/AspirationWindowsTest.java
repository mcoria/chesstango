package net.chesstango.search.smart.alphabeta.filters.once;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Mauricio Coria
 */
public class AspirationWindowsTest {

    @Test
    public void testBoundPositive() {
        AspirationWindows aspirationWindows = new AspirationWindows();

        assertEquals(1, aspirationWindows.diffBound(10, 20, 1));
        assertEquals(2, aspirationWindows.diffBound(10, 20, 2));
        assertEquals(3, aspirationWindows.diffBound(10, 20, 3));
        assertEquals(4, aspirationWindows.diffBound(10, 20, 4));
        assertEquals(5, aspirationWindows.diffBound(10, 20, 5));
        assertEquals(6, aspirationWindows.diffBound(10, 20, 6));
        assertEquals(7, aspirationWindows.diffBound(10, 20, 7));
        assertEquals(8, aspirationWindows.diffBound(10, 20, 8));
        assertEquals(9, aspirationWindows.diffBound(10, 20, 9));
        assertEquals(10, aspirationWindows.diffBound(10, 20, 10));

    }

    @Test
    public void testBoundNegative() {
        AspirationWindows aspirationWindows = new AspirationWindows();

        assertEquals(1, aspirationWindows.diffBound(10, 0, 1));
        assertEquals(2, aspirationWindows.diffBound(10, 0, 2));
        assertEquals(3, aspirationWindows.diffBound(10, 0, 3));
        assertEquals(4, aspirationWindows.diffBound(10, 0, 4));
        assertEquals(5, aspirationWindows.diffBound(10, 0, 5));
        assertEquals(6, aspirationWindows.diffBound(10, 0, 6));
        assertEquals(7, aspirationWindows.diffBound(10, 0, 7));
        assertEquals(8, aspirationWindows.diffBound(10, 0, 8));
        assertEquals(9, aspirationWindows.diffBound(10, 0, 9));
        assertEquals(10, aspirationWindows.diffBound(10, 0, 10));
    }

}

