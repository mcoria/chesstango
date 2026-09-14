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
public class InteriorNodeVisitedTest {

    private InteriorNodeVisited interiorNodeVisited;

    @Mock
    private NodeCounters nodeCounters;

    @Mock
    private AlphaBetaFilter next;

    @BeforeEach
    public void setUp() {
        interiorNodeVisited = new InteriorNodeVisited();
        interiorNodeVisited.setNodeCounters(nodeCounters);
        interiorNodeVisited.setNext(next);
    }

    @Test
    public void testAlphaBeta() {
        when(next.alphaBeta(2, -100, 100)).thenReturn(25);

        int result = interiorNodeVisited.alphaBeta(2, -100, 100);

        assertEquals(25, result);

        InOrder inOrder = inOrder(nodeCounters, next);
        inOrder.verify(nodeCounters).increaseInteriorCounter();
        inOrder.verify(nodeCounters).increaseVisitedCounter(2);
        inOrder.verify(next).alphaBeta(2, -100, 100);
        verifyNoMoreInteractions(nodeCounters);
    }

    @Test
    public void testAccept() {
        Visitor visitor = mock(Visitor.class);

        interiorNodeVisited.accept(visitor);

        verify(visitor, times(1)).visit(interiorNodeVisited);
    }

    @Test
    public void testGetNext() {
        assertEquals(next, interiorNodeVisited.getNext());
    }
}
