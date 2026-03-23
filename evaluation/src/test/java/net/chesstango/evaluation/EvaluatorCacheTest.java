package net.chesstango.evaluation;

import net.chesstango.board.Game;
import net.chesstango.board.position.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

class EvaluatorCacheTest {

    Evaluator mockEvaluator = mock(Evaluator.class);
    Game mockGame = mock(Game.class);

    EvaluatorCache evaluatorCache;

    @BeforeEach
    void setUp() {
        evaluatorCache = new EvaluatorCache();
        evaluatorCache.setImp(mockEvaluator);
        evaluatorCache.setGame(mockGame);

        evaluatorCache.increaseAge();
    }

    /**
     * Test class for EvaluatorCache's evaluate method. The evaluate method integrates with a cached
     * evaluation mechanism to reduce repeated computations, leveraging the hash value of the game's
     * current position and checks for cache age and freshness.
     */

    @Test
    void testEvaluateCacheMiss() {
        // Act
        Integer result = evaluatorCache.readFromCache(0L);

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

        EvaluatorCache evaluatorCache = new EvaluatorCache();
        evaluatorCache.setImp(mockEvaluator);
        evaluatorCache.setGame(mockGame);

        // Act
        int result = evaluatorCache.evaluate(); // Cache hit should occur here

        // Assert
        assertEquals(42, result);
        assertEquals(0, evaluatorCache.getCacheHitsCounter());
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

        EvaluatorCache evaluatorCache = new EvaluatorCache();
        evaluatorCache.setImp(mockEvaluator);
        evaluatorCache.setGame(mockGame);

        // Cache miss
        evaluatorCache.evaluate();

        // Act
        int result = evaluatorCache.evaluate(); // Cache hit should occur here

        // Assert
        assertEquals(42, result);
        assertEquals(1, evaluatorCache.getCacheHitsCounter());
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

        EvaluatorCache evaluatorCache = new EvaluatorCache();
        evaluatorCache.setImp(mockEvaluator);
        evaluatorCache.setGame(mockGame);

        // Cache miss
        evaluatorCache.evaluate();

        // Increase age to stale the cache entry
        for (int i = 0; i <= 3; i++) {
            evaluatorCache.increaseAge();
        }

        // Act
        int result = evaluatorCache.evaluate(); // Should result in a cache miss due to staleness

        // Assert
        assertEquals(50, result); // New evaluation value
        assertEquals(0, evaluatorCache.getCacheHitsCounter());
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

        EvaluatorCache evaluatorCache = new EvaluatorCache();
        evaluatorCache.setImp(mockEvaluator);
        evaluatorCache.setGame(mockGame);

        // Cache miss for first hash
        evaluatorCache.evaluate();

        // Act
        evaluatorCache.setGame(mockGame); // Simulate a new state
        int result = evaluatorCache.evaluate(); // Cache miss due to hash mismatch

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

        EvaluatorCache evaluatorCache = new EvaluatorCache();
        evaluatorCache.setImp(mockEvaluator);

        // Act
        Integer cachedValue = evaluatorCache.readFromCache(12345L);

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

        EvaluatorCache evaluatorCache = new EvaluatorCache();
        evaluatorCache.setImp(mockEvaluator);
        evaluatorCache.setGame(mockGame);

        // Simulate cache population
        evaluatorCache.evaluate();

        // Act
        Integer cachedValue = evaluatorCache.readFromCache(12345L);

        // Assert
        assertEquals(42, cachedValue); // Should return the cached value
        verify(mockEvaluator, times(1)).evaluate();
        verify(mockGame, times(1)).getPosition();
        verify(mockPosition, times(1)).getZobristHash();
    }
}