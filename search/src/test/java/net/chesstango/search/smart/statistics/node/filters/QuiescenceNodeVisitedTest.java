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
public class QuiescenceNodeVisitedTest {

    private QuiescenceNodeVisited quiescenceNodeVisited;

    @Mock
    private NodeCounters nodeCounters;

    @Mock
    private AlphaBetaFilter next;

    @BeforeEach
    public void setUp() {
        quiescenceNodeVisited = new QuiescenceNodeVisited();
        quiescenceNodeVisited.setNodeCounters(nodeCounters);
        quiescenceNodeVisited.setNext(next);
    }

    @Test
    public void testAlphaBeta() {
        when(next.alphaBeta(8, -100, 100)).thenReturn(42);

        int result = quiescenceNodeVisited.alphaBeta(8, -100, 100);

        assertEquals(42, result);

        InOrder inOrder = inOrder(nodeCounters, next);
        inOrder.verify(nodeCounters).increaseQuiescenceCounter();
        inOrder.verify(nodeCounters).increaseVisitedCounter(8);
        inOrder.verify(next).alphaBeta(8, -100, 100);
        verifyNoMoreInteractions(nodeCounters);
    }

    @Test
    public void testAccept() {
        Visitor visitor = mock(Visitor.class);

        quiescenceNodeVisited.accept(visitor);

        verify(visitor, times(1)).visit(quiescenceNodeVisited);
    }

    @Test
    public void testGetNext() {
        assertEquals(next, quiescenceNodeVisited.getNext());
    }
}
