package net.chesstango.search.smart.statistics.node.filters;

import net.chesstango.search.Visitor;
import net.chesstango.search.smart.AlphaBetaFilter;
import net.chesstango.search.smart.statistics.node.NodeCounters;
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
public class LoopNodeStatisticsTest {

    private LoopNodeStatistics loopNodeStatistics;

    @Mock
    private NodeCounters nodeCounters;

    @Mock
    private AlphaBetaFilter next;

    @BeforeEach
    public void setUp() {
        loopNodeStatistics = new LoopNodeStatistics();
        loopNodeStatistics.setNodeCounters(nodeCounters);
        loopNodeStatistics.setNext(next);
    }

    @Test
    public void testAlphaBeta() {
        when(next.alphaBeta(6, -100, 100)).thenReturn(0);

        int result = loopNodeStatistics.alphaBeta(6, -100, 100);

        assertEquals(0, result);

        InOrder inOrder = inOrder(nodeCounters, next);
        inOrder.verify(nodeCounters).increaseLoopCounter();
        inOrder.verify(nodeCounters).increaseVisitedCounter(6);
        inOrder.verify(next).alphaBeta(6, -100, 100);
        verifyNoMoreInteractions(nodeCounters);
    }

    @Test
    public void testAccept() {
        Visitor visitor = mock(Visitor.class);

        loopNodeStatistics.accept(visitor);

        verify(visitor, times(1)).visit(loopNodeStatistics);
    }

    @Test
    public void testGetNext() {
        assertEquals(next, loopNodeStatistics.getNext());
    }
}
