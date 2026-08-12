package net.chesstango.search;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Este test parece trivial, pero es necesario para asegurar que el orden de los bounds es correcto.
 *
 * @author Mauricio Coria
 */
public class BoundTest {

    /**
     * LOWER_BOUND is always greater than any other bound
     */
    @Test
    public void test_LOWER_BOUND() {
        assertTrue(Bound.LOWER_BOUND.compareTo(Bound.EXACT) > 0);
        assertTrue(Bound.LOWER_BOUND.compareTo(Bound.UPPER_BOUND) > 0);
    }

    /**
     * EXACT is lower than LOWER_BOUND and greater than UPPER_BOUND
     */
    @Test
    public void test_EXACT() {
        assertTrue(Bound.EXACT.compareTo(Bound.LOWER_BOUND) < 0);
        assertTrue(Bound.EXACT.compareTo(Bound.UPPER_BOUND) > 0);
    }

    /**
     * UPPER_BOUND is always less than any other bound
     */
    @Test
    public void test_UPPER_BOUND() {
        assertTrue(Bound.UPPER_BOUND.compareTo(Bound.LOWER_BOUND) < 0);
        assertTrue(Bound.UPPER_BOUND.compareTo(Bound.EXACT) < 0);
    }
}
