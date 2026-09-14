package net.chesstango.search.smart.statistics.node.filters;

import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.containers.MoveContainerReader;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.AlphaBetaFilter;
import net.chesstango.search.smart.statistics.node.NodeCounters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @author Mauricio Coria
 */
@ExtendWith(MockitoExtension.class)
public class QuiescenceNodeExpectedTest {

    private QuiescenceNodeExpected quiescenceNodeExpected;

    @Mock
    private NodeCounters nodeCounters;

    @Mock
    private AlphaBetaFilter next;

    @Mock
    private Game game;

    @Mock
    private MoveContainerReader<Move> possibleMoves;

    @Mock
    private Move quietMove;

    @Mock
    private Move captureMove;

    @BeforeEach
    public void setUp() {
        quiescenceNodeExpected = new QuiescenceNodeExpected();
        quiescenceNodeExpected.setNodeCounters(nodeCounters);
        quiescenceNodeExpected.setGame(game);
        quiescenceNodeExpected.setNext(next);
    }

    @Test
    public void testAlphaBetaCountsOnlyNonQuietMoves() {
        when(quietMove.isQuiet()).thenReturn(true);
        when(captureMove.isQuiet()).thenReturn(false);
        when(possibleMoves.iterator()).thenReturn(List.of(quietMove, captureMove, captureMove).iterator());
        when(game.getPossibleMoves()).thenReturn(possibleMoves);
        when(next.alphaBeta(3, -100, 100)).thenReturn(77);

        int result = quiescenceNodeExpected.alphaBeta(3, -100, 100);

        assertEquals(77, result);

        InOrder inOrder = inOrder(nodeCounters, next);
        inOrder.verify(nodeCounters).increaseExpectedCounter(4, 2);
        inOrder.verify(next).alphaBeta(3, -100, 100);
        verifyNoMoreInteractions(nodeCounters);
    }

    @Test
    public void testAlphaBetaWhenAllMovesAreQuiet() {
        when(quietMove.isQuiet()).thenReturn(true);
        when(possibleMoves.iterator()).thenReturn(List.of(quietMove, quietMove).iterator());
        when(game.getPossibleMoves()).thenReturn(possibleMoves);
        when(next.alphaBeta(2, -100, 100)).thenReturn(5);

        assertEquals(5, quiescenceNodeExpected.alphaBeta(2, -100, 100));

        verify(nodeCounters, times(1)).increaseExpectedCounter(3, 0);
    }

    @Test
    public void testAccept() {
        Visitor visitor = mock(Visitor.class);

        quiescenceNodeExpected.accept(visitor);

        verify(visitor, times(1)).visit(quiescenceNodeExpected);
    }

    @Test
    public void testGetNext() {
        assertEquals(next, quiescenceNodeExpected.getNext());
    }
}
