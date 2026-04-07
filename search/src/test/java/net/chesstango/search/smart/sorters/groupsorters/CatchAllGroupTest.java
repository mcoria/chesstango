package net.chesstango.search.smart.sorters.groupsorters;

import net.chesstango.board.moves.Move;
import net.chesstango.search.smart.sorters.MoveComparator;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class CatchAllGroupTest {

    /**
     * Test for the `offer` method of the CatchAllGroup class.
     * <p>
     * The `offer` method is responsible for adding a Move into the group.
     * It returns `true` if the move is added successfully.
     */
    @Test
    void testOfferAddsMoveSuccessfully() {
        // Arrange
        CatchAllSortGroup catchAllGroup = new CatchAllSortGroup();
        Move mockMove = mock(Move.class);

        // Act
        boolean result = catchAllGroup.offer(mockMove);

        // Assert
        assertTrue(result, "The offer method should return true when a move is successfully added.");
    }

    /**
     * Test for the `offer` method to ensure moves are stored correctly.
     * <p>
     * The `offer` method adds the move to an internal list. We verify
     * that the internal list contains the offered move after the method is invoked.
     */
    @Test
    void testOfferStoresMoveInInternalList() {
        // Arrange
        CatchAllSortGroup catchAllGroup = new CatchAllSortGroup();
        Move mockMove = mock(Move.class);

        // Act
        catchAllGroup.offer(mockMove);

        // Verify by indirect observation
        List<Move> collectedMoves = new ArrayList<>();
        catchAllGroup.collect(collectedMoves);

        // Assert
        assertEquals(1, collectedMoves.size(), "The internal list should contain exactly one move.");
        assertTrue(collectedMoves.contains(mockMove), "The internal list should contain the offered move.");
    }

    /**
     * Test for the `offer` method with multiple moves added.
     * <p>
     * Verifies that multiple calls to `offer` correctly add the moves
     * to the group's internal list.
     */
    @Test
    void testOfferWithMultipleMoves() {
        // Arrange
        CatchAllSortGroup catchAllGroup = new CatchAllSortGroup();

        catchAllGroup.setMoveComparator(new MoveComparator() {
            @Override
            public int compare(Move o1, Move o2) {
                return 0;
            }

            @Override
            public void beforeSort(int currentPly) {
            }

            @Override
            public void afterSort() {
            }
        });
        Move mockMove1 = mock(Move.class);
        Move mockMove2 = mock(Move.class);

        // Act
        catchAllGroup.offer(mockMove1);
        catchAllGroup.offer(mockMove2);

        // Verify by indirect observation
        List<Move> collectedMoves = new ArrayList<>();
        catchAllGroup.collect(collectedMoves);

        // Assert
        assertEquals(2, collectedMoves.size(), "The internal list should contain exactly two moves.");
        assertTrue(collectedMoves.contains(mockMove1), "The internal list should contain the first offered move.");
        assertTrue(collectedMoves.contains(mockMove2), "The internal list should contain the second offered move.");
    }
}