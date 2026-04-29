package net.chesstango.search.smart.alphabeta.transposition;

import org.junit.jupiter.api.Test;

import static net.chesstango.search.Bound.*;
import static org.junit.jupiter.api.Assertions.*;

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
        TTableArrayPrimitives tTableArray = new TTableArrayPrimitives(3, 1024);
        TranspositionEntry newEntry = new TranspositionEntry()
                .setHash(123456789L)
                .setDraft((byte) 3)
                .setMove((short) 13)
                .setValue(37)
                .setBound(EXACT);

        // Act
        tTableArray.save(newEntry);

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
        TTableArrayPrimitives tTableArray = new TTableArrayPrimitives(3, 1024);
        TranspositionEntry newEntry = new TranspositionEntry()
                .setHash(Long.MIN_VALUE)
                .setDraft(Byte.MIN_VALUE)
                .setMove(Short.MIN_VALUE)
                .setValue(Integer.MIN_VALUE)
                .setBound(LOWER_BOUND);

        // Act
        tTableArray.save(newEntry);


        // Load
        TranspositionEntry loadEntry = new TranspositionEntry();

        // Try to load the entry
        boolean loaded = tTableArray.load(Long.MIN_VALUE, loadEntry);

        // Assert
        assertTrue(loaded);

        // Assert
        assertEquals(newEntry, loadEntry);
    }

    @Test
    public void testSaveInsertsNewEntryPositives() {
        // Arrange
        TTableArrayPrimitives tTableArray = new TTableArrayPrimitives(3, 1024);
        TranspositionEntry newEntry = new TranspositionEntry()
                .setHash(Long.MAX_VALUE)
                .setDraft(Byte.MAX_VALUE)
                .setMove(Short.MAX_VALUE)
                .setValue(Integer.MAX_VALUE)
                .setBound(UPPER_BOUND);

        // Act
        tTableArray.save(newEntry);


        // Load
        TranspositionEntry loadEntry = new TranspositionEntry();

        // Try to load the entry
        boolean loaded = tTableArray.load(Long.MAX_VALUE, loadEntry);

        // Assert
        assertTrue(loaded);

        // Assert
        assertEquals(newEntry, loadEntry);
    }

    @Test
    public void testSaveUpdatesExistingEntrySameHash() {
        // Arrange
        TTableArrayPrimitives tTableArray = new TTableArrayPrimitives(3, 1024);
        long hash = 123456789L;

        TranspositionEntry initialEntry = new TranspositionEntry()
                .setHash(hash)
                .setDraft((byte) 5)
                .setMove((short) 1)
                .setValue(100)
                .setBound(EXACT);
        tTableArray.save(initialEntry);

        TranspositionEntry updatedEntry = new TranspositionEntry()
                .setHash(hash)
                .setDraft((byte) 6)
                .setMove((short) 2)
                .setValue(200)
                .setBound(LOWER_BOUND);

        // Act
        tTableArray.save(updatedEntry);

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
        TTableArrayPrimitives tTableArray = new TTableArrayPrimitives(3, 1024);
        long hash1 = 123456789L;
        long hash2 = 2 * 1024 * 512 + hash1;

        TranspositionEntry entry1 = new TranspositionEntry()
                .setHash(hash1)
                .setDraft((byte) 5)
                .setMove((short) 1)
                .setValue(100)
                .setBound(EXACT);
        tTableArray.save(entry1);

        TranspositionEntry conflictingEntry = new TranspositionEntry()
                .setHash(hash2)
                .setDraft((byte) 6)
                .setMove((short) 2)
                .setValue(200)
                .setBound(UPPER_BOUND);

        // Act
        tTableArray.save(conflictingEntry);

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
        TTableArrayPrimitives tTableArray = new TTableArrayPrimitives(3, 1024);
        long hash = -1123456789L;

        TranspositionEntry entryInOldSession = new TranspositionEntry()
                .setHash(hash)
                .setDraft((byte) 5)
                .setMove((short) 1)
                .setValue(100)
                .setBound(EXACT);
        tTableArray.save(entryInOldSession);

        tTableArray.clear();

        TranspositionEntry entryInNewSession = new TranspositionEntry()
                .setHash(hash)
                .setDraft((byte) 6)
                .setMove((short) 2)
                .setValue(200)
                .setBound(LOWER_BOUND);

        // Act
        tTableArray.save(entryInNewSession);

        // Load
        TranspositionEntry loadEntry = new TranspositionEntry();

        // Try to load the entry
        boolean loaded = tTableArray.load(hash, loadEntry);

        // Assert
        assertTrue(loaded);

        // Assert
        assertEquals(entryInNewSession, loadEntry);
    }

    @Test
    public void testSaveInsertsNewAge() {
        // Arrange
        TTableArrayPrimitives tTableArray = new TTableArrayPrimitives(3, 1024);
        TranspositionEntry newEntry = new TranspositionEntry()
                .setHash(Long.MAX_VALUE)
                .setDraft(Byte.MAX_VALUE)
                .setMove(Short.MAX_VALUE)
                .setValue(Integer.MAX_VALUE)
                .setBound(UPPER_BOUND);

        // Act
        tTableArray.save(newEntry);


        // Load
        TranspositionEntry loadEntry = new TranspositionEntry();

        // Try to load the entry
        boolean loaded = tTableArray.load(Long.MAX_VALUE, loadEntry);

        // Assert
        assertTrue(loaded);

        // Assert
        assertEquals(newEntry, loadEntry);


        tTableArray.increaseAge();
        tTableArray.increaseAge();
        tTableArray.increaseAge();

        loaded = tTableArray.load(Long.MAX_VALUE, loadEntry);

        // Assert
        assertTrue(loaded);

        // Assert
        assertEquals(newEntry, loadEntry);

        tTableArray.increaseAge();

        loaded = tTableArray.load(Long.MAX_VALUE, loadEntry);

        // Assert
        assertFalse(loaded);
    }

    @Test
    public void testSaveInsertsMaxAge() {
        // Arrange
        TTableArrayPrimitives tTableArray = new TTableArrayPrimitives(3, 1024);

        while (tTableArray.currentAge != TTableArrayPrimitives.MAX_AGE) {
            tTableArray.increaseAge();
        }

        TranspositionEntry newEntry = new TranspositionEntry()
                .setHash(Long.MAX_VALUE)
                .setDraft(Byte.MAX_VALUE)
                .setMove(Short.MAX_VALUE)
                .setValue(Integer.MAX_VALUE)
                .setBound(UPPER_BOUND);

        // Act
        tTableArray.save(newEntry);


        // Load
        TranspositionEntry loadEntry = new TranspositionEntry();

        // Try to load the entry
        boolean loaded = tTableArray.load(Long.MAX_VALUE, loadEntry);

        // Assert
        assertTrue(loaded);

        // Assert
        assertEquals(newEntry, loadEntry);

        tTableArray.increaseAge();

        // Try to load the entry
        loaded = tTableArray.load(Long.MAX_VALUE, loadEntry);

        // Assert
        assertFalse(loaded);
    }

    @Test
    public void testLoad() {
        // Arrange
        TTableArrayPrimitives tTableArray = new TTableArrayPrimitives(3, 1024);

        tTableArray.increaseAge();

        TranspositionEntry entry = new TranspositionEntry();

        boolean loaded = tTableArray.load(0, entry);

        // Assert
        assertFalse(loaded);
    }

    @Test
    public void testLoadCollision() {
        // Arrange
        TTableArrayPrimitives tTableArray = new TTableArrayPrimitives(0, 16);

        tTableArray.increaseAge();

        TranspositionEntry saveEntry = new TranspositionEntry()
                .setHash(1)
                .setDraft((byte) 5)
                .setMove((short) 1)
                .setValue(100)
                .setBound(EXACT);
        tTableArray.save(saveEntry);

        TranspositionEntry entry = new TranspositionEntry();

        boolean loaded = tTableArray.load(1024 + 1, entry);

        // Assert
        assertTrue(loaded);
        assertEquals(saveEntry, entry);
    }
}