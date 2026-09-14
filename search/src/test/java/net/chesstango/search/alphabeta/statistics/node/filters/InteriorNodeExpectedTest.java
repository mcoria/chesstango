package net.chesstango.search.alphabeta.statistics.node.filters;

import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.containers.MoveContainerReader;
import net.chesstango.search.Visitor;
import net.chesstango.search.alphabeta.AlphaBetaFilter;
import net.chesstango.search.alphabeta.statistics.node.NodeCounters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @author Mauricio Coria
 */
@ExtendWith(MockitoExtension.class)
public class InteriorNodeExpectedTest {

    private InteriorNodeExpected interiorNodeExpected;

    @Mock
    private NodeCounters nodeCounters;

    @Mock
    private AlphaBetaFilter next;

    @Mock
    private Game game;

    @Mock
    private MoveContainerReader<Move> possibleMoves;

    @BeforeEach
    public void setUp() {
        interiorNodeExpected = new InteriorNodeExpected();
        interiorNodeExpected.setNodeCounters(nodeCounters);
        interiorNodeExpected.setGame(game);
        interiorNodeExpected.setNext(next);
    }

    @Test
    public void testAlphaBeta() {
        when(game.getPossibleMoves()).thenReturn(possibleMoves);
        when(possibleMoves.size()).thenReturn(7);
        when(next.alphaBeta(3, -100, 100)).thenReturn(15);

        int result = interiorNodeExpected.alphaBeta(3, -100, 100);

        assertEquals(15, result);

        InOrder inOrder = inOrder(nodeCounters, next);
        inOrder.verify(nodeCounters).increaseExpectedCounter(4, 7);
        inOrder.verify(next).alphaBeta(3, -100, 100);
        verifyNoMoreInteractions(nodeCounters);
    }

    @Test
    public void testAlphaBetaWithoutMoves() {
        when(game.getPossibleMoves()).thenReturn(possibleMoves);
        when(possibleMoves.size()).thenReturn(0);
        when(next.alphaBeta(1, -5, 5)).thenReturn(0);

        assertEquals(0, interiorNodeExpected.alphaBeta(1, -5, 5));

        verify(nodeCounters, times(1)).increaseExpectedCounter(2, 0);
    }

    @Test
    public void testAccept() {
        Visitor visitor = mock(Visitor.class);

        interiorNodeExpected.accept(visitor);

        verify(visitor, times(1)).visit(interiorNodeExpected);
    }

    @Test
    public void testGetNext() {
        assertEquals(next, interiorNodeExpected.getNext());
    }
}
