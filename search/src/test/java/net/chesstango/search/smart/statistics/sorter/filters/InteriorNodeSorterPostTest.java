package net.chesstango.search.smart.statistics.sorter.filters;

import net.chesstango.search.Visitor;
import net.chesstango.search.smart.AlphaBetaFilter;
import net.chesstango.search.smart.statistics.sorter.SorterCounters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

/**
 * @author Mauricio Coria
 */
@ExtendWith(MockitoExtension.class)
public class InteriorNodeSorterPostTest {

    private InteriorNodeSorterPost interiorNodeSorterPost;

    @Mock
    private SorterCounters sorterCounters;

    @Mock
    private AlphaBetaFilter next;

    @BeforeEach
    public void setUp() {
        interiorNodeSorterPost = new InteriorNodeSorterPost();
        interiorNodeSorterPost.setSorterCounters(sorterCounters);
        interiorNodeSorterPost.setNext(next);
    }

    @Test
    public void testAlphaBeta() {
        when(next.alphaBeta(3, -100, 100)).thenReturn(50);

        int result = interiorNodeSorterPost.alphaBeta(3, -100, 100);

        assertEquals(50, result);

        InOrder inOrder = inOrder(sorterCounters, next);
        inOrder.verify(sorterCounters).increaseIndex(3);
        inOrder.verify(next).alphaBeta(3, -100, 100);

        verify(sorterCounters, times(1)).increaseIndex(anyInt());
        verifyNoMoreInteractions(sorterCounters);
    }

    @Test
    public void testAlphaBetaIncreasesIndexPerPly() {
        when(next.alphaBeta(1, -10, 10)).thenReturn(1);
        when(next.alphaBeta(2, -10, 10)).thenReturn(2);

        assertEquals(1, interiorNodeSorterPost.alphaBeta(1, -10, 10));
        assertEquals(2, interiorNodeSorterPost.alphaBeta(2, -10, 10));
        assertEquals(1, interiorNodeSorterPost.alphaBeta(1, -10, 10));

        verify(sorterCounters, times(2)).increaseIndex(1);
        verify(sorterCounters, times(1)).increaseIndex(2);
    }

    @Test
    public void testGetNext() {
        assertSame(next, interiorNodeSorterPost.getNext());
    }

    @Test
    public void testAccept() {
        Visitor visitor = mock(Visitor.class);

        interiorNodeSorterPost.accept(visitor);

        verify(visitor, times(1)).visit(interiorNodeSorterPost);
    }
}
