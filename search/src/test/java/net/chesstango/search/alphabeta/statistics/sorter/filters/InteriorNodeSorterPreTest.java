package net.chesstango.search.alphabeta.statistics.sorter.filters;

import net.chesstango.search.Visitor;
import net.chesstango.search.alphabeta.AlphaBetaFilter;
import net.chesstango.search.alphabeta.statistics.sorter.SorterCounters;
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
public class InteriorNodeSorterPreTest {

    private InteriorNodeSorterPre interiorNodeSorterPre;

    @Mock
    private SorterCounters sorterCounters;

    @Mock
    private AlphaBetaFilter next;

    @BeforeEach
    public void setUp() {
        interiorNodeSorterPre = new InteriorNodeSorterPre();
        interiorNodeSorterPre.setSorterCounters(sorterCounters);
        interiorNodeSorterPre.setNext(next);
    }

    @Test
    public void testAlphaBetaWithoutFailHigh() {
        when(next.alphaBeta(3, -100, 100)).thenReturn(50);

        int result = interiorNodeSorterPre.alphaBeta(3, -100, 100);

        assertEquals(50, result);

        verify(sorterCounters, times(1)).resetIndex(3);
        verify(sorterCounters, never()).increaseFailHighCounter(anyInt());
    }

    @Test
    public void testAlphaBetaWithFailHigh() {
        when(next.alphaBeta(2, -100, 100)).thenReturn(200);

        int result = interiorNodeSorterPre.alphaBeta(2, -100, 100);

        assertEquals(200, result);

        InOrder inOrder = inOrder(sorterCounters, next);
        inOrder.verify(sorterCounters).resetIndex(2);
        inOrder.verify(next).alphaBeta(2, -100, 100);
        inOrder.verify(sorterCounters).increaseFailHighCounter(2);
    }

    @Test
    public void testAlphaBetaValueEqualsBeta() {
        when(next.alphaBeta(1, -100, 100)).thenReturn(100);

        int result = interiorNodeSorterPre.alphaBeta(1, -100, 100);

        assertEquals(100, result);

        verify(sorterCounters, times(1)).resetIndex(1);
        verify(sorterCounters, never()).increaseFailHighCounter(anyInt());
    }

    @Test
    public void testAccept() {
        Visitor visitor = mock(Visitor.class);

        interiorNodeSorterPre.accept(visitor);

        verify(visitor, times(1)).visit(interiorNodeSorterPre);
    }
}
