package net.chesstango.search.smart;

import net.chesstango.search.Bound;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Mauricio Coria
 */
class BoundTest {

    @Test
    void testCompare() {
        /*
        System.out.println(Bound.LOWER_BOUND.compareTo(Bound.LOWER_BOUND));
        System.out.println(Bound.LOWER_BOUND.compareTo(Bound.EXACT));
        System.out.println(Bound.LOWER_BOUND.compareTo(Bound.UPPER_BOUND));
         */

        assertTrue(Bound.LOWER_BOUND.compareTo(Bound.LOWER_BOUND) ==  0);
        assertTrue(Bound.LOWER_BOUND.compareTo(Bound.EXACT) >  0);
        assertTrue(Bound.LOWER_BOUND.compareTo(Bound.UPPER_BOUND) >  0);

        assertTrue(Bound.EXACT.compareTo(Bound.LOWER_BOUND) < 0);
        assertTrue(Bound.EXACT.compareTo(Bound.EXACT) == 0);
        assertTrue(Bound.EXACT.compareTo(Bound.UPPER_BOUND) > 0);

        assertTrue(Bound.UPPER_BOUND.compareTo(Bound.LOWER_BOUND) < 0);
        assertTrue(Bound.UPPER_BOUND.compareTo(Bound.EXACT) < 0);
        assertTrue(Bound.UPPER_BOUND.compareTo(Bound.UPPER_BOUND) == 0);

    }
}
