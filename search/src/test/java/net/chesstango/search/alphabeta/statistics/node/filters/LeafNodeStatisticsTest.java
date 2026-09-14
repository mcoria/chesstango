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
public class LeafNodeStatisticsTest {

    private LeafNodeStatistics leafNodeStatistics;

    @Mock
    private NodeCounters nodeCounters;

    @Mock
    private AlphaBetaFilter next;

    @BeforeEach
    public void setUp() {
        leafNodeStatistics = new LeafNodeStatistics();
        leafNodeStatistics.setNodeCounters(nodeCounters);
        leafNodeStatistics.setNext(next);
    }

    @Test
    public void testAlphaBeta() {
        when(next.alphaBeta(4, -100, 100)).thenReturn(-12);

        int result = leafNodeStatistics.alphaBeta(4, -100, 100);

        assertEquals(-12, result);

        InOrder inOrder = inOrder(nodeCounters, next);
        inOrder.verify(nodeCounters).increaseLeafCounter();
        inOrder.verify(nodeCounters).increaseVisitedCounter(4);
        inOrder.verify(next).alphaBeta(4, -100, 100);
        verifyNoMoreInteractions(nodeCounters);
    }

    @Test
    public void testAccept() {
        Visitor visitor = mock(Visitor.class);

        leafNodeStatistics.accept(visitor);

        verify(visitor, times(1)).visit(leafNodeStatistics);
    }

    @Test
    public void testGetNext() {
        assertEquals(next, leafNodeStatistics.getNext());
    }
}
