package net.chesstango.search.smart.features.transposition;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for the save method in TTableArray class.
 *
 * <p>The save method is responsible for inserting, updating, or replacing entries in the
 * transposition table based on the current session ID and the provided entry's hash.</p>
 */
public class TTableArrayTest {

    @Test
    public void testSaveInsertsNewEntry() {
        // Arrange
        TTableArray tTableArray = new TTableArray();
        TranspositionEntry newEntry = new TranspositionEntry()
                .setHash(123456789L)
                .setDraft(5)
                .setMove((short) 1)
                .setValue(100)
                .setBound(TranspositionBound.EXACT);

        // Act
        TTable.InsertResult result = tTableArray.save(newEntry);

        // Assert
        assertEquals(TTable.InsertResult.INSERTED, result);
    }

    @Test
    public void testSaveUpdatesExistingEntrySameHash() {
        // Arrange
        TTableArray tTableArray = new TTableArray();
        long hash = 123456789L;

        TranspositionEntry initialEntry = new TranspositionEntry()
                .setHash(hash)
                .setDraft(5)
                .setMove((short) 1)
                .setValue(100)
                .setBound(TranspositionBound.EXACT);
        tTableArray.save(initialEntry);

        TranspositionEntry updatedEntry = new TranspositionEntry()
                .setHash(hash)
                .setDraft(6)
                .setMove((short) 2)
                .setValue(200)
                .setBound(TranspositionBound.LOWER_BOUND);

        // Act
        TTable.InsertResult result = tTableArray.save(updatedEntry);

        // Assert
        assertEquals(TTable.InsertResult.UPDATED, result);
    }

    @Test
    public void testSaveReplacesEntryWithDifferentHash() {
        // Arrange
        TTableArray tTableArray = new TTableArray();
        long hash1 = 123456789L;
        long hash2 = 1024 * 512 + hash1;

        TranspositionEntry entry1 = new TranspositionEntry()
                .setHash(hash1)
                .setDraft(5)
                .setMove((short) 1)
                .setValue(100)
                .setBound(TranspositionBound.EXACT);
        tTableArray.save(entry1);

        TranspositionEntry conflictingEntry = new TranspositionEntry()
                .setHash(hash2)
                .setDraft(6)
                .setMove((short) 2)
                .setValue(200)
                .setBound(TranspositionBound.UPPER_BOUND);

        // Act
        TTable.InsertResult result = tTableArray.save(conflictingEntry);

        // Assert
        assertEquals(TTable.InsertResult.OVER_WRITTEN, result);
    }

    @Test
    public void testSaveInsertsEntryInNewSession() {
        // Arrange
        TTableArray tTableArray = new TTableArray();
        long hash = 123456789L;

        TranspositionEntry entryInOldSession = new TranspositionEntry()
                .setHash(hash)
                .setDraft(5)
                .setMove((short) 1)
                .setValue(100)
                .setBound(TranspositionBound.EXACT);
        tTableArray.save(entryInOldSession);

        tTableArray.clear(); // Move to a new session

        TranspositionEntry entryInNewSession = new TranspositionEntry()
                .setHash(hash)
                .setDraft(6)
                .setMove((short) 2)
                .setValue(200)
                .setBound(TranspositionBound.LOWER_BOUND);

        // Act
        TTable.InsertResult result = tTableArray.save(entryInNewSession);

        // Assert
        assertEquals(TTable.InsertResult.INSERTED, result);
    }
}