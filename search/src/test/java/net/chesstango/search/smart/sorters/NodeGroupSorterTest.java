package net.chesstango.search.smart.sorters;

import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.containers.MoveContainerReader;
import net.chesstango.search.smart.sorters.groupsorters.CatchAllSortGroup;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit test for the NodeGroupSorter class.
 * <p>
 * This test class validates the behavior of the `getOrderedMoves` method
 * by ensuring that it integrates properly with both the Game and GroupSorter components.
 */
@ExtendWith(MockitoExtension.class)
public class NodeGroupSorterTest {

    @Mock
    private Game mockGame;

    @Mock
    private GroupSorter mockGroupSorter;

    @InjectMocks
    private NodeGroupSorter nodeGroupSorter;

    /**
     * Verifies that the `getOrderedMoves` method correctly sorts moves
     * using a mock Game and a mock GroupSorter.
     */
    @Test
    @Disabled
    public void testGetOrderedMovesBasic() {
        // Arrange
        Move mockMove1 = mock(Move.class);
        Move mockMove2 = mock(Move.class);
        Move mockMove3 = mock(Move.class);

        MoveContainerReader<Move> mockMoveContainer = mock(MoveContainerReader.class);
        when(mockMoveContainer.iterator()).thenReturn(Arrays.asList(mockMove1, mockMove2, mockMove3).iterator());
        when(mockMoveContainer.size()).thenReturn(3);

        when(mockGame.getPossibleMoves()).thenReturn(mockMoveContainer);

        doAnswer(invocation -> {
            List<Move> movesListArg = invocation.getArgument(0);
            movesListArg.add(mockMove1);
            movesListArg.add(mockMove2);
            movesListArg.add(mockMove3);
            return null;
        }).when(mockGroupSorter).collect(anyList());

        // Act
        Iterable<Move> orderedMoves = nodeGroupSorter.getOrderedMoves(0);

        // Assert
        List<Move> resultingMoves = new ArrayList<>();
        orderedMoves.forEach(resultingMoves::add);

        assertEquals(3, resultingMoves.size());
        assertTrue(resultingMoves.containsAll(Arrays.asList(mockMove1, mockMove2, mockMove3)));

        verify(mockGroupSorter).beforeSort(0);
        verify(mockGroupSorter).offer(mockMove1);
        verify(mockGroupSorter).offer(mockMove2);
        verify(mockGroupSorter).offer(mockMove3);
        verify(mockGroupSorter).collect(anyList());
        verify(mockGroupSorter).afterSort();
    }

    /**
     * Ensures that an exception is thrown when the `offer` method of the GroupSorter returns false.
     */
    @Test
    public void testOfferThrowsException() {
        // Arrange
        Move mockMove = mock(Move.class);

        MoveContainerReader<Move> mockMoveContainer = mock(MoveContainerReader.class);
        when(mockMoveContainer.iterator()).thenReturn(List.of(mockMove).iterator());
        when(mockMoveContainer.size()).thenReturn(1);

        when(mockGame.getPossibleMoves()).thenReturn(mockMoveContainer);
        when(mockGroupSorter.offer(any(Move.class))).thenReturn(false);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> nodeGroupSorter.getOrderedMoves(1));
    }

    /**
     * Ensures the `getOrderedMoves` method correctly integrates
     * with a real CatchAllSortGroup implementation for sorting.
     */
    @Test
    @Disabled
    public void testGetOrderedMovesWithRealGroupSorter() {
        // Arrange
        CatchAllSortGroup realGroupSorter = new CatchAllSortGroup();

        List<Move> mockMoves = Arrays.asList(mock(Move.class), mock(Move.class), mock(Move.class));
        MoveContainerReader<Move> mockMoveContainer = mock(MoveContainerReader.class);
        when(mockMoveContainer.iterator()).thenReturn(mockMoves.iterator());
        when(mockMoveContainer.size()).thenReturn(3);

        when(mockGame.getPossibleMoves()).thenReturn(mockMoveContainer);

        nodeGroupSorter.setGroupSorter(realGroupSorter);

        // Act
        Iterable<Move> orderedMoves = nodeGroupSorter.getOrderedMoves(5);

        // Assert
        List<Move> resultingMoves = new ArrayList<>();
        orderedMoves.forEach(resultingMoves::add);

        assertEquals(3, resultingMoves.size());
        assertEquals(mockMoves, resultingMoves);
    }

    /**
     * Tests the behavior when the game returns an empty set of moves.
     */
    @Test
    public void testGetOrderedMovesWithNoMoves() {
        // Arrange
        MoveContainerReader<Move> mockMoveContainer = mock(MoveContainerReader.class);
        when(mockMoveContainer.iterator()).thenReturn(new ArrayList<Move>().iterator());
        when(mockMoveContainer.size()).thenReturn(0);

        when(mockGame.getPossibleMoves()).thenReturn(mockMoveContainer);

        // Act
        Iterable<Move> orderedMoves = nodeGroupSorter.getOrderedMoves(3);

        // Assert
        Iterator<Move> iterator = orderedMoves.iterator();
        assertFalse(iterator.hasNext());

        verify(mockGroupSorter).beforeSort(3);
        verify(mockGroupSorter).collect(anyList());
        verify(mockGroupSorter).afterSort();
    }
}