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
public class EgtbNodeStatisticsTest {

    private EgtbNodeStatistics egtbNodeStatistics;

    @Mock
    private NodeCounters nodeCounters;

    @Mock
    private AlphaBetaFilter next;

    @BeforeEach
    public void setUp() {
        egtbNodeStatistics = new EgtbNodeStatistics();
        egtbNodeStatistics.setNodeCounters(nodeCounters);
        egtbNodeStatistics.setNext(next);
    }

    @Test
    public void testAlphaBeta() {
        when(next.alphaBeta(7, -100, 100)).thenReturn(-1);

        int result = egtbNodeStatistics.alphaBeta(7, -100, 100);

        assertEquals(-1, result);

        InOrder inOrder = inOrder(nodeCounters, next);
        inOrder.verify(nodeCounters).increaseEgtbCounter();
        inOrder.verify(nodeCounters).increaseVisitedCounter(7);
        inOrder.verify(next).alphaBeta(7, -100, 100);
        verifyNoMoreInteractions(nodeCounters);
    }

    @Test
    public void testAccept() {
        Visitor visitor = mock(Visitor.class);

        egtbNodeStatistics.accept(visitor);

        verify(visitor, times(1)).visit(egtbNodeStatistics);
    }

    @Test
    public void testGetNext() {
        assertEquals(next, egtbNodeStatistics.getNext());
    }
}
