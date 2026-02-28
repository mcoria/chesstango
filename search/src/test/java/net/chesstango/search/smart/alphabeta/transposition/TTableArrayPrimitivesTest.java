package net.chesstango.search.smart.alphabeta.transposition;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for the save method in TTableArrayPrimitives class.
 *
 * <p>The save method is responsible for inserting, updating, or replacing entries in the
 * transposition table based on the current session ID and the provided entry's hash.</p>
 */
public class TTableArrayPrimitivesTest {

    @Test
    public void testSaveInsertsNewEntry() {
        // Arrange
        TTableArrayPrimitives tTableArray = new TTableArrayPrimitives();
        TranspositionEntry newEntry = new TranspositionEntry()
                .setHash(123456789L)
                .setDraft((byte)3)
                .setMove((short) 13)
                .setValue(37)
                .setBound(TranspositionBound.EXACT);

        // Act
        TTable.SaveResult result = tTableArray.save(newEntry);

        // Assert
        assertEquals(TTable.SaveResult.INSERTED, result);

        // Load
        TranspositionEntry loadEntry = new TranspositionEntry();

        // Try to load the entry
        boolean loaded = tTableArray.load(123456789L, loadEntry);

        // Assert
        assertTrue(loaded);

        // Assert
        assertEquals(newEntry, loadEntry);
    }

    @Test
    public void testSaveInsertsNewEntryNegatives() {
        // Arrange
        TTableArrayPrimitives tTableArray = new TTableArrayPrimitives();
        TranspositionEntry newEntry = new TranspositionEntry()
                .setHash(-123456789L)
                .setDraft((byte)-3)
                .setMove((short) -13)
                .setValue(-37)
                .setBound(TranspositionBound.LOWER_BOUND);

        // Act
        TTable.SaveResult result = tTableArray.save(newEntry);

        // Assert
        assertEquals(TTable.SaveResult.INSERTED, result);

        // Load
        TranspositionEntry loadEntry = new TranspositionEntry();

        // Try to load the entry
        boolean loaded = tTableArray.load(-123456789L, loadEntry);

        // Assert
        assertTrue(loaded);

        // Assert
        assertEquals(newEntry, loadEntry);
    }

    @Test
    public void testSaveUpdatesExistingEntrySameHash() {
        // Arrange
        TTableArrayPrimitives tTableArray = new TTableArrayPrimitives();
        long hash = 123456789L;

        TranspositionEntry initialEntry = new TranspositionEntry()
                .setHash(hash)
                .setDraft((byte)5)
                .setMove((short) 1)
                .setValue(100)
                .setBound(TranspositionBound.EXACT);
        tTableArray.save(initialEntry);

        TranspositionEntry updatedEntry = new TranspositionEntry()
                .setHash(hash)
                .setDraft((byte)6)
                .setMove((short) 2)
                .setValue(200)
                .setBound(TranspositionBound.LOWER_BOUND);

        // Act
        TTable.SaveResult result = tTableArray.save(updatedEntry);

        // Assert
        assertEquals(TTable.SaveResult.UPDATED, result);

        // Load
        TranspositionEntry loadEntry = new TranspositionEntry();

        // Try to load the entry
        boolean loaded = tTableArray.load(hash, loadEntry);

        // Assert
        assertTrue(loaded);

        // Assert
        assertEquals(updatedEntry, loadEntry);
    }

    @Test
    public void testSaveReplacesEntryWithDifferentHash() {
        // Arrange
        TTableArrayPrimitives tTableArray = new TTableArrayPrimitives();
        long hash1 = 123456789L;
        long hash2 = 1024 * 512 + hash1;

        TranspositionEntry entry1 = new TranspositionEntry()
                .setHash(hash1)
                .setDraft((byte)5)
                .setMove((short) 1)
                .setValue(100)
                .setBound(TranspositionBound.EXACT);
        tTableArray.save(entry1);

        TranspositionEntry conflictingEntry = new TranspositionEntry()
                .setHash(hash2)
                .setDraft((byte)6)
                .setMove((short) 2)
                .setValue(200)
                .setBound(TranspositionBound.UPPER_BOUND);

        // Act
        TTable.SaveResult result = tTableArray.save(conflictingEntry);

        // Assert
        assertEquals(TTable.SaveResult.OVER_WRITTEN, result);

        // Load
        TranspositionEntry loadEntry = new TranspositionEntry();

        // Try to load the entry
        boolean loaded = tTableArray.load(hash2, loadEntry);

        // Assert
        assertTrue(loaded);

        // Assert
        assertEquals(conflictingEntry, loadEntry);
    }

    @Test
    public void testSaveInsertsEntryInNewSession() {
        // Arrange
        TTableArrayPrimitives tTableArray = new TTableArrayPrimitives();
        long hash = -1123456789L;

        TranspositionEntry entryInOldSession = new TranspositionEntry()
                .setHash(hash)
                .setDraft((byte)5)
                .setMove((short) 1)
                .setValue(100)
                .setBound(TranspositionBound.EXACT);
        tTableArray.save(entryInOldSession);

        tTableArray.clear(); // Move to a new session

        TranspositionEntry entryInNewSession = new TranspositionEntry()
                .setHash(hash)
                .setDraft((byte)6)
                .setMove((short) 2)
                .setValue(200)
                .setBound(TranspositionBound.LOWER_BOUND);

        // Act
        TTable.SaveResult result = tTableArray.save(entryInNewSession);

        // Assert
        assertEquals(TTable.SaveResult.INSERTED, result);

        // Load
        TranspositionEntry loadEntry = new TranspositionEntry();

        // Try to load the entry
        boolean loaded = tTableArray.load(hash, loadEntry);

        // Assert
        assertTrue(loaded);

        // Assert
        assertEquals(entryInNewSession, loadEntry);
    }
}