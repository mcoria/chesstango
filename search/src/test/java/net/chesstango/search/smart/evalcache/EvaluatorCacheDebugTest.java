package net.chesstango.search.smart.evalcache;

import net.chesstango.board.Game;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.gardel.fen.FEN;
import net.chesstango.search.smart.debug.DebugNodeTracker;
import net.chesstango.search.smart.debug.model.DebugCacheRead;
import net.chesstango.search.smart.debug.model.DebugNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

/**
 * @author Mauricio Coria
 */
@ExtendWith(MockitoExtension.class)
public class EvaluatorCacheDebugTest {

    @InjectMocks
    private EvaluatorCacheDebug evaluatorCacheDebug;

    @Mock
    private EvaluatorCache evaluatorCache;

    @Mock
    private DebugNode debugNode;

    @Mock
    private DebugNodeTracker debugNodeTracker;


    @Test
    void testReadNull() {
        Game game = Game.from(FEN.START_POSITION);
        Move move = game.getMove(Square.e2, Square.e4);

        long hashMove = move.getZobristHash();

        // Arrange
        evaluatorCacheDebug.setGame(game);
        when(evaluatorCache.read(hashMove)).thenReturn(null);

        // Act
        EvaluatorCacheEntry result = evaluatorCacheDebug.read(hashMove);

        // Assert
        assertNull(result);
        verify(evaluatorCache, times(1)).read(hashMove);
    }

    @Test
    void testReadNotNull() {
        EvaluatorCacheDebug evaluatorCacheDebugSpy = spy(evaluatorCacheDebug);

        Game game = Game.from(FEN.START_POSITION);
        Move move = game.getMove(Square.e2, Square.e4);

        long hashMove = move.getZobristHash();

        EvaluatorCacheEntry entry = new EvaluatorCacheEntry()
                .setAge(1)
                .setEvaluation(1000)
                .setHash(hashMove);


        // Arrange
        evaluatorCacheDebug.setGame(game);
        when(evaluatorCache.read(hashMove)).thenReturn(entry);

        Mockito.doNothing().when(evaluatorCacheDebugSpy).trackReadFromCache(hashMove, 1000);

        // Act
        EvaluatorCacheEntry result = evaluatorCacheDebugSpy.read(hashMove);

        // Assert
        assertEquals(entry, result);
        verify(evaluatorCache, times(1)).read(hashMove);
        verify(evaluatorCacheDebugSpy, times(1)).trackReadFromCache(hashMove, 1000);
    }

    /**
     * Tests the trackComparatorsEvalCacheReads() method of MoveSorterDebug class.
     * Ensures that the evaluation cache reads are populated properly.
     */
    @Test
    void testTrackComparatorsEvalCacheReads() {
        Game game = Game.from(FEN.START_POSITION);
        Move move = game.getMove(Square.e2, Square.e4);

        long hashMove = move.getZobristHash();

        // Arrange
        evaluatorCacheDebug.setGame(game);

        List<DebugCacheRead> evalCacheReads = new ArrayList<>();
        when(debugNode.getEvalCacheReads()).thenReturn(evalCacheReads);
        when(debugNodeTracker.getCurrentNode()).thenReturn(debugNode);

        // Act
        evaluatorCacheDebug.trackReadFromCache(hashMove, 10);

        // Assert
        assertEquals(1, evalCacheReads.size());
        assertEquals("e2e4", evalCacheReads.getFirst().getMove());
        assertEquals(10, evalCacheReads.getFirst().getEvaluation());
    }
}
