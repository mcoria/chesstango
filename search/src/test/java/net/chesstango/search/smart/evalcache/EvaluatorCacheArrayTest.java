package net.chesstango.search.smart.evalcache;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static net.chesstango.search.smart.evalcache.EvaluatorCacheArray.CACHE_ARRAY_SIZE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * @author Mauricio Coria
 */
public class EvaluatorCacheArrayTest {

    private EvaluatorCacheArray evaluatorCacheArray;

    @BeforeEach
    void setUp() {
        evaluatorCacheArray = new EvaluatorCacheArray();

        evaluatorCacheArray.increaseAge();
    }

    /**
     * Test class for EvaluatorCache's evaluate method. The evaluate method integrates with a cached
     * evaluation mechanism to reduce repeated computations, leveraging the hash value of the game's
     * current position and checks for cache age and freshness.
     */
    @Test
    void testReadZero() {
        // Act
        EvaluatorCacheEntry result = evaluatorCacheArray.read(1L);

        // Assert
        assertNull(result);
    }

    @Test
    void testWrite() {
        // Act
        EvaluatorCacheEntry result = evaluatorCacheArray.write(1L, 42);

        // Assert
        assertEquals(1L, result.getHash());
        assertEquals(42, result.getEvaluation());
        assertEquals(evaluatorCacheArray.getCurrentAge(), result.getAge());

    }

    @Test
    void testWriteAndRead() {
        // Act
        evaluatorCacheArray.write(1L, 42);

        EvaluatorCacheEntry result = evaluatorCacheArray.read(1L);

        // Assert
        assertEquals(1L, result.getHash());
        assertEquals(42, result.getEvaluation());
        assertEquals(evaluatorCacheArray.getCurrentAge(), result.getAge());
    }

    @Test
    void testWriteAndReadSameKey() {
        // Act
        evaluatorCacheArray.write(CACHE_ARRAY_SIZE + 1L, 42);

        EvaluatorCacheEntry result = evaluatorCacheArray.read(1L);

        // Assert
        assertNull(result);
    }


}