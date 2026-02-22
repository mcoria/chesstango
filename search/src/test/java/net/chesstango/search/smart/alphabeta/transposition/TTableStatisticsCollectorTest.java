package net.chesstango.search.smart.alphabeta.transposition;

import net.chesstango.search.smart.alphabeta.statistics.transposition.TTableStatisticsCollector;
import net.chesstango.search.smart.alphabeta.transposition.TTable;
import net.chesstango.search.smart.alphabeta.transposition.TranspositionEntry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the TTableStatisticsCollector class.
 * <p>
 * This test class specifically targets the `save` method of the `TTableStatisticsCollector` class.
 * The `save` method tracks the number of table collisions when entries are replaced in the underlying TTable.
 */
@ExtendWith(MockitoExtension.class)
public class TTableStatisticsCollectorTest {

    @Mock
    private TTable mockTTable;

    @Test
    public void testSave_InsertResultInserted() {
        // Arrange
        TTableStatisticsCollector collector = new TTableStatisticsCollector();
        collector.setTTable(mockTTable);

        TranspositionEntry entry = new TranspositionEntry();
        when(mockTTable.save(entry)).thenReturn(TTable.InsertResult.INSERTED);

        // Act
        TTable.InsertResult result = collector.save(entry);

        // Assert
        assertEquals(TTable.InsertResult.INSERTED, result);
        assertEquals(0, collector.getOverWrites());
        verify(mockTTable, times(1)).save(entry);
    }

    @Test
    public void testSave_InsertResultUpdated() {
        // Arrange
        TTableStatisticsCollector collector = new TTableStatisticsCollector();
        collector.setTTable(mockTTable);

        TranspositionEntry entry = new TranspositionEntry();
        when(mockTTable.save(entry)).thenReturn(TTable.InsertResult.UPDATED);

        // Act
        TTable.InsertResult result = collector.save(entry);

        // Assert
        assertEquals(TTable.InsertResult.UPDATED, result);
        assertEquals(0, collector.getOverWrites());
        verify(mockTTable, times(1)).save(entry);
    }

    @Test
    public void testSave_InsertResultReplaced() {
        // Arrange
        TTableStatisticsCollector collector = new TTableStatisticsCollector();
        collector.setTTable(mockTTable);

        TranspositionEntry entry = new TranspositionEntry();
        when(mockTTable.save(entry)).thenReturn(TTable.InsertResult.OVER_WRITTEN);

        // Act
        TTable.InsertResult result = collector.save(entry);

        // Assert
        assertEquals(TTable.InsertResult.OVER_WRITTEN, result);
        assertEquals(1, collector.getOverWrites());
        verify(mockTTable, times(1)).save(entry);
    }

    @Test
    public void testLoad_SuccessfulLoadIncrementsTableHits() {
        // Arrange
        TTableStatisticsCollector collector = new TTableStatisticsCollector();
        collector.setTTable(mockTTable);

        TranspositionEntry entry = new TranspositionEntry();
        long hash = 123L;
        when(mockTTable.load(hash, entry)).thenReturn(true);

        // Act
        boolean result = collector.load(hash, entry);

        // Assert
        assertTrue(result);
        assertEquals(1, collector.getReadHits());
        verify(mockTTable, times(1)).load(hash, entry);
    }

    @Test
    public void testLoad_UnsuccessfulLoadDoesNotIncrementTableHits() {
        // Arrange
        TTableStatisticsCollector collector = new TTableStatisticsCollector();
        collector.setTTable(mockTTable);

        TranspositionEntry entry = new TranspositionEntry();
        long hash = 456L;
        when(mockTTable.load(hash, entry)).thenReturn(false);

        // Act
        boolean result = collector.load(hash, entry);

        // Assert
        assertFalse(result);
        assertEquals(0, collector.getReadHits());
        verify(mockTTable, times(1)).load(hash, entry);
    }
}