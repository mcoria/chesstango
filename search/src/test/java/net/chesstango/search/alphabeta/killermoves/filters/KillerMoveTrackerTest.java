package net.chesstango.search.alphabeta.killermoves.filters;

import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.position.GameHistoryRecord;
import net.chesstango.board.position.GameHistoryReader;
import net.chesstango.search.Visitor;
import net.chesstango.search.alphabeta.AlphaBetaFilter;
import net.chesstango.search.alphabeta.killermoves.KillerMoves;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @author Mauricio Coria
 */
@ExtendWith(MockitoExtension.class)
public class KillerMoveTrackerTest {

    private KillerMoveTracker killerMoveTracker;

    @Mock
    private AlphaBetaFilter next;

    @Mock
    private Game game;

    @Mock
    private GameHistoryReader gameHistoryReader;

    @Mock
    private GameHistoryRecord gameHistoryRecord;

    @Mock
    private KillerMoves killerMoves;

    @Mock
    private Visitor visitor;

    @Mock
    private Move playedMove;

    @BeforeEach
    public void setUp() {
        killerMoveTracker = new KillerMoveTracker();
        killerMoveTracker.setNext(next);
        killerMoveTracker.setGame(game);
        killerMoveTracker.setKillerMoves(killerMoves);
    }

    @Test
    public void testAccept() {
        killerMoveTracker.accept(visitor);

        verify(visitor).visit(killerMoveTracker);
        verifyNoMoreInteractions(visitor);
    }

    @Test
    public void testGettersAndSetters() {
        assertEquals(next, killerMoveTracker.getNext());

        AlphaBetaFilter newNext = mock(AlphaBetaFilter.class);
        killerMoveTracker.setNext(newNext);

        assertEquals(newNext, killerMoveTracker.getNext());
    }

    @Test
    public void testAlphaBetaWithBetaCutoff() {
        when(next.alphaBeta(2, 50, 100)).thenReturn(150);
        when(game.getHistory()).thenReturn(gameHistoryReader);
        when(gameHistoryReader.peekLastRecord()).thenReturn(gameHistoryRecord);
        when(gameHistoryRecord.playedMove()).thenReturn(playedMove);

        int result = killerMoveTracker.alphaBeta(2, 50, 100);

        assertEquals(150, result);
        verify(next).alphaBeta(2, 50, 100);
        verify(killerMoves).trackKillerMove(playedMove, 2);
    }

    @Test
    public void testAlphaBetaWithoutBetaCutoffEqualToBeta() {
        when(next.alphaBeta(2, 50, 100)).thenReturn(100);

        int result = killerMoveTracker.alphaBeta(2, 50, 100);

        assertEquals(100, result);
        verify(next).alphaBeta(2, 50, 100);
        verifyNoInteractions(game);
        verifyNoInteractions(killerMoves);
    }

    @Test
    public void testAlphaBetaWithoutBetaCutoffLessThanBeta() {
        when(next.alphaBeta(2, 50, 100)).thenReturn(80);

        int result = killerMoveTracker.alphaBeta(2, 50, 100);

        assertEquals(80, result);
        verify(next).alphaBeta(2, 50, 100);
        verifyNoInteractions(game);
        verifyNoInteractions(killerMoves);
    }
}
