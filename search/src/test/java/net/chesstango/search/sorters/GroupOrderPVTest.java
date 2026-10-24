package net.chesstango.search.sorters;

import net.chesstango.board.moves.Move;
import net.chesstango.search.PVMove;
import net.chesstango.search.alphabeta.pv.groupsorters.PrincipalVariationGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static net.chesstango.search.alphabeta.Constants.MAX_DEPTH;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * @author Mauricio Coria
 */
@ExtendWith(MockitoExtension.class)
public class GroupOrderPVTest {

    private PrincipalVariationGroup principalVariationGroup;

    private GroupOrder groupOrder;

    @Mock
    private Move move1;

    @Mock
    private Move move2;

    @Mock
    private Move move3;

    @Mock
    private Move move4;

    @BeforeEach
    public void setUp() {
        principalVariationGroup = new PrincipalVariationGroup();
        groupOrder = GroupOrder.createPrincipalVariation(principalVariationGroup);
    }


    @Test
    public void testBeforeSearchClearsLastPVMoves() {
        PVMove pvMove = new PVMove(12345L, move1);
        principalVariationGroup.setLastPVMoves(List.of(pvMove));

        principalVariationGroup.beforeSearch();
        principalVariationGroup.beforeSearchByDepth();

        principalVariationGroup.beforeSort(0);

        List<Move> moves = new ArrayList<>(List.of(move1, move2));
        int result = groupOrder.sort(moves, 0);

        assertEquals(0, result);
        assertEquals(List.of(move1, move2), moves);
    }

    @Test
    public void testBeforeSearchByDepthWithNullLastPVMoves() {
        principalVariationGroup.setLastPVMoves(null);
        principalVariationGroup.beforeSearchByDepth();

        principalVariationGroup.beforeSort(0);

        List<Move> moves = new ArrayList<>(List.of(move1, move2));
        int result = groupOrder.sort(moves, 0);

        assertEquals(0, result);
        assertEquals(List.of(move1, move2), moves);
    }

    @Test
    public void testBeforeSearchByDepthPopulatesPvMoves() {
        PVMove pvMove0 = new PVMove(1L, move1);
        PVMove pvMove1 = new PVMove(2L, move2);
        principalVariationGroup.setLastPVMoves(List.of(pvMove0, pvMove1));

        principalVariationGroup.beforeSearchByDepth();

        // Ply 0
        principalVariationGroup.beforeSort(0);
        List<Move> movesPly0 = new ArrayList<>(List.of(move3, move1, move4));
        int resultPly0 = groupOrder.sort(movesPly0, 0);

        assertEquals(1, resultPly0);
        assertEquals(List.of(move1, move3, move4), movesPly0);

        // Ply 1
        principalVariationGroup.beforeSort(1);
        List<Move> movesPly1 = new ArrayList<>(List.of(move4, move3, move2));
        int resultPly1 = groupOrder.sort(movesPly1, 0);

        assertEquals(1, resultPly1);
        assertEquals(List.of(move2, move3, move4), movesPly1);

        // Ply 2 (beyond lastPVMoves size)
        principalVariationGroup.beforeSort(2);
        List<Move> movesPly2 = new ArrayList<>(List.of(move3, move4));
        int resultPly2 = groupOrder.sort(movesPly2, 0);

        assertEquals(0, resultPly2);
        assertEquals(List.of(move3, move4), movesPly2);
    }

    @Test
    public void testBeforeSearchByDepthExceedingMaxDepth() {
        List<PVMove> largePvMoves = new ArrayList<>();
        for (int i = 0; i < MAX_DEPTH + 10; i++) {
            largePvMoves.add(new PVMove(i, mock(Move.class)));
        }

        principalVariationGroup.setLastPVMoves(largePvMoves);
        assertDoesNotThrow(() -> principalVariationGroup.beforeSearchByDepth());
    }

    @Test
    public void testSortWithoutBeforeSort() {
        GroupOrder groupOrder = GroupOrder.createPrincipalVariation(principalVariationGroup);

        List<Move> moves = new ArrayList<>(List.of(move1, move2, move3));

        int result = groupOrder.sort(moves, 0);

        assertEquals(0, result);
        assertEquals(List.of(move1, move2, move3), moves);
    }

    @Test
    public void testSortWithCurrentPVMoveAlreadyAtFrom() {
        GroupOrder groupOrder = GroupOrder.createPrincipalVariation(principalVariationGroup);

        PVMove pvMove = new PVMove(100L, move1);
        principalVariationGroup.setLastPVMoves(List.of(pvMove));
        principalVariationGroup.beforeSearchByDepth();
        principalVariationGroup.beforeSort(0);

        List<Move> moves = new ArrayList<>(List.of(move1, move2, move3));
        int result = groupOrder.sort(moves, 0);

        assertEquals(1, result);
        assertEquals(List.of(move1, move2, move3), moves);
    }

    @Test
    public void testSortWithCurrentPVMoveInMiddle() {
        PVMove pvMove = new PVMove(100L, move2);
        principalVariationGroup.setLastPVMoves(List.of(pvMove));
        principalVariationGroup.beforeSearchByDepth();
        principalVariationGroup.beforeSort(0);

        List<Move> moves = new ArrayList<>(List.of(move1, move2, move3));
        int result = groupOrder.sort(moves, 0);

        assertEquals(1, result);
        assertEquals(List.of(move2, move1, move3), moves);
    }

    @Test
    public void testSortSubRange() {
        PVMove pvMove = new PVMove(100L, move3);
        principalVariationGroup.setLastPVMoves(List.of(pvMove));
        principalVariationGroup.beforeSearchByDepth();
        principalVariationGroup.beforeSort(0);

        List<Move> moves = new ArrayList<>(List.of(move1, move2, move3, move4));
        int result = groupOrder.sort(moves, 1);

        assertEquals(2, result);
        assertEquals(List.of(move1, move3, move2, move4), moves);
    }

    @Test
    public void testSortCurrentPVMoveLast() {
        PVMove pvMove = new PVMove(100L, move4);
        principalVariationGroup.setLastPVMoves(List.of(pvMove));
        principalVariationGroup.beforeSearchByDepth();
        principalVariationGroup.beforeSort(0);

        List<Move> moves = new ArrayList<>(List.of(move1, move2, move3, move4));
        int result = groupOrder.sort(moves, 0);

        assertEquals(1, result);
        assertEquals(List.of(move4, move2, move3, move1), moves);
    }

    @Test
    public void testSortCurrentPVMoveNotInList() {
        Move missingMove = mock(Move.class);
        PVMove pvMove = new PVMove(100L, missingMove);
        principalVariationGroup.setLastPVMoves(List.of(pvMove));
        principalVariationGroup.beforeSearchByDepth();
        principalVariationGroup.beforeSort(0);

        List<Move> moves = new ArrayList<>(List.of(move1, move2, move3));
        int result = groupOrder.sort(moves, 0);

        assertEquals(0, result);
        assertEquals(List.of(move1, move2, move3), moves);
    }

    @Test
    public void testSortResetsCurrentPVMove() {
        PVMove pvMove = new PVMove(100L, move2);
        principalVariationGroup.setLastPVMoves(List.of(pvMove));
        principalVariationGroup.beforeSearchByDepth();

        principalVariationGroup.beforeSort(0);
        List<Move> moves1 = new ArrayList<>(List.of(move1, move2));
        int result1 = groupOrder.sort(moves1, 0);
        assertEquals(1, result1);
        assertEquals(List.of(move2, move1), moves1);
        principalVariationGroup.afterSort();

        // Next sort call should have currentPVMove == null
        principalVariationGroup.beforeSort(0);
        List<Move> moves2 = new ArrayList<>(List.of(move1, move2));
        int result2 = groupOrder.sort(moves2, 0);
        assertEquals(0, result2);
        assertEquals(List.of(move1, move2), moves2);
        principalVariationGroup.afterSort();
    }

    @Test
    public void testBeforeSortClearsSlotInPvMoves() {
        PVMove pvMove = new PVMove(100L, move1);
        principalVariationGroup.setLastPVMoves(List.of(pvMove));
        principalVariationGroup.beforeSearchByDepth();

        // First beforeSort at ply 0 consumes pvMoves[0]
        principalVariationGroup.beforeSort(0);

        List<Move> moves1 = new ArrayList<>(List.of(move2, move1));
        groupOrder.sort(moves1, 0);

        // Calling beforeSort(0) again should not reload pvMoves[0] since it was cleared to null
        principalVariationGroup.beforeSort(0);

        List<Move> moves2 = new ArrayList<>(List.of(move2, move1));
        int result2 = groupOrder.sort(moves2, 0);
        assertEquals(0, result2);
        assertEquals(List.of(move2, move1), moves2);
    }
}
