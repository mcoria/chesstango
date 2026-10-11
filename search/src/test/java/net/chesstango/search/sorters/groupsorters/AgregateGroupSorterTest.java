package net.chesstango.search.sorters.groupsorters;

import net.chesstango.board.moves.Move;
import net.chesstango.search.sorters.GroupSorter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @author Mauricio Coria
 */
@ExtendWith(MockitoExtension.class)
public class AgregateGroupSorterTest {

    private AgregateGroupSorter agregateGroupSorter;

    @Mock
    private GroupSorter sorter1;

    @Mock
    private GroupSorter sorter2;

    @Mock
    private GroupSorter sorter3;

    @Mock
    private List<Move> moves;

    @BeforeEach
    public void setUp() {
        agregateGroupSorter = new AgregateGroupSorter();
    }

    @Test
    public void testEmptyGroupSorters() {
        int result = agregateGroupSorter.sort(moves, 2, 8);

        assertEquals(2, result);
        verifyNoInteractions(moves);
    }

    @Test
    public void testSingleGroupSorter() {
        agregateGroupSorter.addGroupSorter(sorter1);

        when(sorter1.sort(moves, 1, 5)).thenReturn(3);

        int result = agregateGroupSorter.sort(moves, 1, 5);

        assertEquals(3, result);
        verify(sorter1).sort(moves, 1, 5);
        verifyNoMoreInteractions(sorter1);
    }

    @Test
    public void testMultipleGroupSortersChainedExecution() {
        agregateGroupSorter.addGroupSorter(sorter1);
        agregateGroupSorter.addGroupSorter(sorter2);
        agregateGroupSorter.addGroupSorter(sorter3);

        when(sorter1.sort(moves, 0, 10)).thenReturn(3);
        when(sorter2.sort(moves, 3, 10)).thenReturn(7);
        when(sorter3.sort(moves, 7, 10)).thenReturn(10);

        int result = agregateGroupSorter.sort(moves, 0, 10);

        assertEquals(10, result);

        InOrder inOrder = inOrder(sorter1, sorter2, sorter3);
        inOrder.verify(sorter1).sort(moves, 0, 10);
        inOrder.verify(sorter2).sort(moves, 3, 10);
        inOrder.verify(sorter3).sort(moves, 7, 10);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testMultipleGroupSortersSubRange() {
        agregateGroupSorter.addGroupSorter(sorter1);
        agregateGroupSorter.addGroupSorter(sorter2);

        when(sorter1.sort(moves, 2, 8)).thenReturn(4);
        when(sorter2.sort(moves, 4, 8)).thenReturn(6);

        int result = agregateGroupSorter.sort(moves, 2, 8);

        assertEquals(6, result);

        InOrder inOrder = inOrder(sorter1, sorter2);
        inOrder.verify(sorter1).sort(moves, 2, 8);
        inOrder.verify(sorter2).sort(moves, 4, 8);
        inOrder.verifyNoMoreInteractions();
    }
}
