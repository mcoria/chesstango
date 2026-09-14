package net.chesstango.search.smart.statistics.sorter;

import net.chesstango.search.Visitor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * @author Mauricio Coria
 */
public class SorterCountersTest {

    private SorterCounters sorterCounters;

    @BeforeEach
    public void setUp() {
        sorterCounters = new SorterCounters();
        sorterCounters.beforeSearch();
    }

    @Test
    public void testInitialStatistics() {
        SorterStatistics statistics = sorterCounters.getSorterStatistics();

        assertEquals(0, statistics.failHighCounter());
        assertEquals(0, statistics.failHighFirstCounter());
    }

    @Test
    public void testFailHighOnFirstMove() {
        sorterCounters.resetIndex(1);
        sorterCounters.increaseIndex(1);

        sorterCounters.increaseFailHighCounter(1);

        SorterStatistics statistics = sorterCounters.getSorterStatistics();
        assertEquals(1, statistics.failHighCounter());
        assertEquals(1, statistics.failHighFirstCounter());
    }

    @Test
    public void testFailHighOnSecondMove() {
        sorterCounters.resetIndex(1);
        sorterCounters.increaseIndex(1);
        sorterCounters.increaseIndex(1);

        sorterCounters.increaseFailHighCounter(1);

        SorterStatistics statistics = sorterCounters.getSorterStatistics();
        assertEquals(1, statistics.failHighCounter());
        assertEquals(0, statistics.failHighFirstCounter());
    }

    @Test
    public void testFailHighCountersByPly() {
        sorterCounters.resetIndex(0);
        sorterCounters.resetIndex(1);

        // ply 0: fail high on the first move
        sorterCounters.increaseIndex(0);
        sorterCounters.increaseFailHighCounter(0);

        // ply 1: fail high on the third move
        sorterCounters.increaseIndex(1);
        sorterCounters.increaseIndex(1);
        sorterCounters.increaseIndex(1);
        sorterCounters.increaseFailHighCounter(1);

        // ply 0: fail high again, index is not the first move anymore
        sorterCounters.increaseIndex(0);
        sorterCounters.increaseFailHighCounter(0);

        SorterStatistics statistics = sorterCounters.getSorterStatistics();
        assertEquals(3, statistics.failHighCounter());
        assertEquals(1, statistics.failHighFirstCounter());
    }

    @Test
    public void testResetIndexDiscardsPreviousIndex() {
        sorterCounters.resetIndex(2);
        sorterCounters.increaseIndex(2);
        sorterCounters.increaseIndex(2);

        sorterCounters.resetIndex(2);
        sorterCounters.increaseIndex(2);
        sorterCounters.increaseFailHighCounter(2);

        SorterStatistics statistics = sorterCounters.getSorterStatistics();
        assertEquals(1, statistics.failHighCounter());
        assertEquals(1, statistics.failHighFirstCounter());
    }

    @Test
    public void testBeforeSearchResetsCounters() {
        sorterCounters.resetIndex(1);
        sorterCounters.increaseIndex(1);
        sorterCounters.increaseFailHighCounter(1);

        sorterCounters.beforeSearch();

        SorterStatistics statistics = sorterCounters.getSorterStatistics();
        assertEquals(0, statistics.failHighCounter());
        assertEquals(0, statistics.failHighFirstCounter());
    }

    @Test
    public void testAccept() {
        AtomicReference<SorterCounters> visited = new AtomicReference<>();

        sorterCounters.accept(new Visitor() {
            @Override
            public void visit(SorterCounters sorterCounters) {
                visited.set(sorterCounters);
            }
        });

        assertSame(sorterCounters, visited.get());
    }
}
