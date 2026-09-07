package net.chesstango.search.smart.evaluator;

import net.chesstango.board.Game;
import net.chesstango.board.position.Position;
import net.chesstango.evaluation.Evaluator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

/**
 * @author Mauricio Coria
 */
@Disabled
public class EvaluatorCacheArrayTest {

    Evaluator mockEvaluator = mock(Evaluator.class);
    Game mockGame = mock(Game.class);

    EvaluatorCacheArray evaluatorCacheArray;

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

    /*
    @Test
    void testEvaluateCacheMiss() {
        // Act
        Integer result = evaluatorCacheArray.readFromCache(0L);

        // Assert
        assertNull(result);
    }

    @Test
    void testEvaluateCacheHitZero() {
        // Arrange
        Position mockPosition = mock(Position.class);

        when(mockGame.getPosition()).thenReturn(mockPosition);
        when(mockPosition.getZobristHash()).thenReturn(0L);
        when(mockEvaluator.evaluate()).thenReturn(42);

        EvaluatorCacheArray evaluatorCacheArray = new EvaluatorCacheArray();
        evaluatorCacheArray.setEvaluator(mockEvaluator);
        evaluatorCacheArray.setGame(mockGame);

        // Act
        int result = evaluatorCacheArray.evaluate(); // Cache hit should occur here

        // Assert
        assertEquals(42, result);
        assertEquals(0, evaluatorCacheArray.getEvaluationsCacheHitsCounter());
        verify(mockEvaluator, times(1)).evaluate(); // Should not re-evaluate
        verify(mockGame, times(1)).getPosition();
        verify(mockPosition, times(1)).getZobristHash();
    }

    @Test
    void testEvaluateCacheHit() {
        // Arrange
        Position mockPosition = mock(Position.class);

        when(mockGame.getPosition()).thenReturn(mockPosition);
        when(mockPosition.getZobristHash()).thenReturn(12345L);
        when(mockEvaluator.evaluate()).thenReturn(42);

        EvaluatorCacheArray evaluatorCacheArray = new EvaluatorCacheArray();
        evaluatorCacheArray.setEvaluator(mockEvaluator);
        evaluatorCacheArray.setGame(mockGame);

        // Cache miss
        evaluatorCacheArray.evaluate();

        // Act
        int result = evaluatorCacheArray.evaluate(); // Cache hit should occur here

        // Assert
        assertEquals(42, result);
        assertEquals(1, evaluatorCacheArray.getEvaluationsCacheHitsCounter());
        verify(mockEvaluator, times(1)).evaluate(); // Should not re-evaluate
        verify(mockGame, times(2)).getPosition();
        verify(mockPosition, times(2)).getZobristHash();
    }

    @Test
    void testEvaluateStaleCache() {
        // Arrange
        Position mockPosition = mock(Position.class);

        when(mockGame.getPosition()).thenReturn(mockPosition);
        when(mockPosition.getZobristHash()).thenReturn(12345L);
        when(mockEvaluator.evaluate()).thenReturn(42, 50); // First and second evaluation results

        EvaluatorCacheArray evaluatorCacheArray = new EvaluatorCacheArray();
        evaluatorCacheArray.setEvaluator(mockEvaluator);
        evaluatorCacheArray.setGame(mockGame);

        // Cache miss
        evaluatorCacheArray.evaluate();

        // Increase age to stale the cache entry
        for (int i = 0; i <= 3; i++) {
            evaluatorCacheArray.increaseAge();
        }

        // Act
        int result = evaluatorCacheArray.evaluate(); // Should result in a cache miss due to staleness

        // Assert
        assertEquals(50, result); // New evaluation value
        assertEquals(0, evaluatorCacheArray.getEvaluationsCacheHitsCounter());
        verify(mockEvaluator, times(2)).evaluate();
        verify(mockGame, times(2)).getPosition();
        verify(mockPosition, times(2)).getZobristHash();
    }

    @Test
    void testEvaluateHashMismatch() {
        // Arrange
        Position mockPosition = mock(Position.class);

        when(mockGame.getPosition()).thenReturn(mockPosition);
        when(mockPosition.getZobristHash()).thenReturn(12345L, 67890L); // Two different hashes
        when(mockEvaluator.evaluate()).thenReturn(42, 50); // Evaluation results

        EvaluatorCacheArray evaluatorCacheArray = new EvaluatorCacheArray();
        evaluatorCacheArray.setEvaluator(mockEvaluator);
        evaluatorCacheArray.setGame(mockGame);

        // Cache miss for first hash
        evaluatorCacheArray.evaluate();

        // Act
        evaluatorCacheArray.setGame(mockGame); // Simulate a new state
        int result = evaluatorCacheArray.evaluate(); // Cache miss due to hash mismatch

        // Assert
        assertEquals(50, result); // New evaluation value
        verify(mockEvaluator, times(2)).evaluate();
        verify(mockGame, times(2)).getPosition();
        verify(mockPosition, times(2)).getZobristHash();
    }

    @Test
    void testReadFromCacheMiss() {
        // Arrange
        Position mockPosition = mock(Position.class);

        when(mockGame.getPosition()).thenReturn(mockPosition);
        when(mockPosition.getZobristHash()).thenReturn(12345L);
        when(mockEvaluator.evaluate()).thenReturn(42);

        EvaluatorCacheArray evaluatorCacheArray = new EvaluatorCacheArray();
        evaluatorCacheArray.setEvaluator(mockEvaluator);

        // Act
        Integer cachedValue = evaluatorCacheArray.readFromCache(12345L);

        // Assert
        assertNull(cachedValue);
        verify(mockEvaluator, never()).evaluate();
    }

    @Test
    void testReadFromCacheHit() {
        // Arrange
        Position mockPosition = mock(Position.class);

        when(mockGame.getPosition()).thenReturn(mockPosition);
        when(mockPosition.getZobristHash()).thenReturn(12345L);
        when(mockEvaluator.evaluate()).thenReturn(42);

        EvaluatorCacheArray evaluatorCacheArray = new EvaluatorCacheArray();
        evaluatorCacheArray.setEvaluator(mockEvaluator);
        evaluatorCacheArray.setGame(mockGame);

        // Simulate cache population
        evaluatorCacheArray.evaluate();

        // Act
        Integer cachedValue = evaluatorCacheArray.readFromCache(12345L);

        // Assert
        assertEquals(42, cachedValue); // Should return the cached value
        verify(mockEvaluator, times(1)).evaluate();
        verify(mockGame, times(1)).getPosition();
        verify(mockPosition, times(1)).getZobristHash();
    }

     */
}