package net.chesstango.search.alphabeta.statistics.node.filters;

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
public class TerminalNodeStatisticsTest {

    private TerminalNodeStatistics terminalNodeStatistics;

    @Mock
    private NodeCounters nodeCounters;

    @Mock
    private AlphaBetaFilter next;

    @BeforeEach
    public void setUp() {
        terminalNodeStatistics = new TerminalNodeStatistics();
        terminalNodeStatistics.setNodeCounters(nodeCounters);
        terminalNodeStatistics.setNext(next);
    }

    @Test
    public void testAlphaBeta() {
        when(next.alphaBeta(5, -100, 100)).thenReturn(1000);

        int result = terminalNodeStatistics.alphaBeta(5, -100, 100);

        assertEquals(1000, result);

        InOrder inOrder = inOrder(nodeCounters, next);
        inOrder.verify(nodeCounters).increaseTerminalCounter();
        inOrder.verify(nodeCounters).increaseVisitedCounter(5);
        inOrder.verify(next).alphaBeta(5, -100, 100);
        verifyNoMoreInteractions(nodeCounters);
    }

    @Test
    public void testAccept() {
        Visitor visitor = mock(Visitor.class);

        terminalNodeStatistics.accept(visitor);

        verify(visitor, times(1)).visit(terminalNodeStatistics);
    }

    @Test
    public void testGetNext() {
        assertEquals(next, terminalNodeStatistics.getNext());
    }
}
