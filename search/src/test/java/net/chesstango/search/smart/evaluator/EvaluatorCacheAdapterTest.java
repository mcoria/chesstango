package net.chesstango.search.smart.evaluator;

import net.chesstango.board.Game;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.gardel.fen.FEN;
import net.chesstango.search.smart.evalcache.EvaluatorCache;
import net.chesstango.search.smart.evalcache.EvaluatorCacheEntry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @author Mauricio Coria
 */
@ExtendWith(MockitoExtension.class)
public class EvaluatorCacheAdapterTest {

    @InjectMocks
    private EvaluatorCacheAdapter evaluatorCacheAdapter;

    @Mock
    private Evaluator evaluator;

    @Mock
    private EvaluatorCache evaluatorCache;


    @Test
    public void testRedFromCache() {
        // Setup
        Game game = Game.from(FEN.START_POSITION);
        evaluatorCacheAdapter.setGame(game);

        long hashPosition = game.getPosition().getZobristHash();

        EvaluatorCacheEntry entry = new EvaluatorCacheEntry(hashPosition, 10, 0);

        when(evaluatorCache.read(hashPosition)).thenReturn(entry);

        // Act
        int result = evaluatorCacheAdapter.evaluate();

        // Assert
        assertEquals(10, result);
        verify(evaluatorCache, times(1)).read(hashPosition);
    }

    @Test
    public void testEvaluate() {
        // Setup
        Game game = Game.from(FEN.START_POSITION);
        evaluatorCacheAdapter.setGame(game);

        long hashPosition = game.getPosition().getZobristHash();

        when(evaluatorCache.read(hashPosition)).thenReturn(null);
        when(evaluator.evaluate()).thenReturn(10);

        EvaluatorCacheEntry entry = new EvaluatorCacheEntry(hashPosition, 10, 0);
        when(evaluatorCache.write(hashPosition, 10)).thenReturn(entry);

        // Act
        int result = evaluatorCacheAdapter.evaluate();

        // Assert
        assertEquals(10, result);
        verify(evaluatorCache, times(1)).read(hashPosition);
        verify(evaluatorCache, times(1)).write(hashPosition, 10);
        verify(evaluator, times(1)).evaluate();
    }
}