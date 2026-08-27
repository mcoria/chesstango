package net.chesstango.search.smart.evaluator;

import net.chesstango.board.Game;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.evaluation.EvaluatorCacheRead;
import net.chesstango.gardel.fen.FEN;
import net.chesstango.search.smart.debug.DebugNodeTracker;
import net.chesstango.search.smart.debug.model.DebugCacheRead;
import net.chesstango.search.smart.debug.model.DebugNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

/**
 * @author Mauricio Coria
 */
@ExtendWith(MockitoExtension.class)
public class EvaluatorCacheDebugTest {

    @InjectMocks
    private EvaluatorCacheDebug evaluatorCacheDebug;

    @Mock
    private EvaluatorCacheRead evaluatorCacheRead;

    @Mock
    private DebugNode debugNode;

    @Mock
    private DebugNodeTracker debugNodeTracker;

    /**
     * Tests the trackComparatorsEvalCacheReads() method of MoveSorterDebug class.
     * Ensures that the evaluation cache reads are populated properly.
     */
    @Test
    void testTrackComparatorsEvalCacheReads() {
        Game game = Game.from(FEN.START_POSITION);
        Move move = game.getMove(Square.e2, Square.e4);

        // Arrange
        evaluatorCacheDebug.setGame(game);

        List<DebugCacheRead> evalCacheReads = new ArrayList<>();
        when(debugNode.getEvalCacheReads()).thenReturn(evalCacheReads);
        when(debugNodeTracker.getCurrentNode()).thenReturn(debugNode);

        // Act
        evaluatorCacheDebug.trackReadFromCache(move.getZobristHash(), 10);

        // Assert
        assertEquals(1, evalCacheReads.size());
        assertEquals("e2e4", evalCacheReads.getFirst().getMove());
        assertEquals(10, evalCacheReads.getFirst().getEvaluation());
    }
}
