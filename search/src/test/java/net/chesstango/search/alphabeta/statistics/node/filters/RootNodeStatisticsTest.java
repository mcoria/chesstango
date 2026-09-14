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
public class RootNodeStatisticsTest {

    private RootNodeStatistics rootNodeStatistics;

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
        rootNodeStatistics = new RootNodeStatistics();
        rootNodeStatistics.setNodeCounters(nodeCounters);
        rootNodeStatistics.setGame(game);
        rootNodeStatistics.setNext(next);
    }

    @Test
    public void testAlphaBeta() {
        when(game.getPossibleMoves()).thenReturn(possibleMoves);
        when(possibleMoves.size()).thenReturn(20);
        when(next.alphaBeta(0, -100, 100)).thenReturn(33);

        int result = rootNodeStatistics.alphaBeta(0, -100, 100);

        assertEquals(33, result);

        InOrder inOrder = inOrder(nodeCounters, next);
        inOrder.verify(nodeCounters).increaseRootCounter();
        inOrder.verify(nodeCounters).increaseExpectedCounter(0, 1);
        inOrder.verify(nodeCounters).increaseVisitedCounter(0);
        inOrder.verify(nodeCounters).increaseExpectedCounter(1, 20);
        inOrder.verify(next).alphaBeta(0, -100, 100);
        verifyNoMoreInteractions(nodeCounters);
    }

    @Test
    public void testAccept() {
        Visitor visitor = mock(Visitor.class);

        rootNodeStatistics.accept(visitor);

        verify(visitor, times(1)).visit(rootNodeStatistics);
    }

    @Test
    public void testGetNext() {
        assertEquals(next, rootNodeStatistics.getNext());
    }
}
