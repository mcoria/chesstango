package net.chesstango.search.smart.alphabeta.transposition;

import net.chesstango.search.smart.alphabeta.statistics.transposition.TTableCounters;
import net.chesstango.search.smart.alphabeta.statistics.transposition.TTableStatisticsNodeCollector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for the TTableStatisticsCollector class.
 * <p>
 * This test class specifically targets the `save` method of the `TTableStatisticsCollector` class.
 * The `save` method tracks the number of table collisions when entries are replaced in the underlying TTable.
 */
public class TTableStatisticsCollectorTest {

    private TTableStatisticsNodeCollector collector;

    private TTableCounters tTableCounters;

    private TranspositionEntry entry;

    @BeforeEach
    public void setUp() {
        entry = new TranspositionEntry();
        tTableCounters = new TTableCounters();

        collector = new TTableStatisticsNodeCollector(tTableCounters);
    }

    @Test
    public void testSave_InsertResultInserted() {
        collector.setTTable(new TTable() {
            @Override
            public boolean load(long hash, TranspositionEntry entry) {
                return false;
            }

            @Override
            public void save(TranspositionEntry entry) {

            }

            @Override
            public void increaseAge() {

            }

            @Override
            public void clear() {

            }
        });

        // Act
        collector.save(entry);

        // Assert
        assertEquals(1, tTableCounters.getWrites());
        assertEquals(0, tTableCounters.getUpdates());
        assertEquals(0, tTableCounters.getOverWrites());
    }

    @Test
    public void testSave_InsertResultUpdated() {
        // Arrange
        collector.setTTable(new TTable() {
            @Override
            public boolean load(long hash, TranspositionEntry entry) {
                entry.setHash(hash);
                return true;
            }

            @Override
            public void save(TranspositionEntry entry) {

            }

            @Override
            public void increaseAge() {

            }

            @Override
            public void clear() {

            }
        });

        // Act
        collector.save(entry);

        // Assert
        assertEquals(1, tTableCounters.getWrites());
        assertEquals(1, tTableCounters.getUpdates());
        assertEquals(0, tTableCounters.getOverWrites());
    }

    @Test
    public void testSave_InsertResultReplaced() {
        // Arrange
        collector.setTTable(new TTable() {
            @Override
            public boolean load(long hash, TranspositionEntry entry) {
                entry.setHash(1L);
                return true;
            }

            @Override
            public void save(TranspositionEntry entry) {

            }

            @Override
            public void increaseAge() {

            }

            @Override
            public void clear() {

            }
        });

        // Act
        collector.save(entry);

        // Assert
        assertEquals(1, tTableCounters.getWrites());
        assertEquals(0, tTableCounters.getUpdates());
        assertEquals(1, tTableCounters.getOverWrites());
    }

    @Test
    public void testLoad_SuccessfulLoadIncrementsTableHits() {
        // Arrange
        collector.setTTable(new TTable() {
            @Override
            public boolean load(long hash, TranspositionEntry entry) {
                entry.setHash(hash);
                return true;
            }

            @Override
            public void save(TranspositionEntry entry) {

            }

            @Override
            public void increaseAge() {

            }

            @Override
            public void clear() {

            }
        });

        // Act
        boolean result = collector.load(123L, entry);

        // Assert
        assertTrue(result);
        assertEquals(1, tTableCounters.getReads());
        assertEquals(1, tTableCounters.getReadNodeHits());
    }

    @Test
    public void testLoad_UnsuccessfulLoadDoesNotIncrementTableHits() {
        // Arrange
        collector.setTTable(new TTable() {
            @Override
            public boolean load(long hash, TranspositionEntry entry) {
                entry.setHash(1L);
                return true;
            }

            @Override
            public void save(TranspositionEntry entry) {

            }

            @Override
            public void increaseAge() {

            }

            @Override
            public void clear() {

            }
        });

        // Act
        boolean result = collector.load(123L, entry);

        // Assert
        assertTrue(result);
        assertEquals(1, tTableCounters.getReads());
        assertEquals(0, tTableCounters.getReadNodeHits());
    }
}