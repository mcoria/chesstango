package net.chesstango.search.alphabeta.pv.groupsorters;

import net.chesstango.board.moves.Move;
import net.chesstango.search.PVMove;
import net.chesstango.search.Visitor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static net.chesstango.search.alphabeta.Constants.MAX_DEPTH;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author Mauricio Coria
 */
@ExtendWith(MockitoExtension.class)
public class PrincipalVariationGroupTest {

    private PrincipalVariationGroup principalVariationGroup;

    @Mock
    private Visitor visitor;

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
    }

    @Test
    public void testAccept() {
        principalVariationGroup.accept(visitor);

        verify(visitor).visit(principalVariationGroup);
        verifyNoMoreInteractions(visitor);
    }

    @Test
    public void testBeforeSearchClearsLastPVMoves() {
        PVMove pvMove = new PVMove(12345L, move1);
        principalVariationGroup.setLastPVMoves(List.of(pvMove));

        principalVariationGroup.beforeSearch();
        principalVariationGroup.beforeSearchByDepth();

        principalVariationGroup.beforeSort(0);

        List<Move> moves = new ArrayList<>(List.of(move1, move2));
        int result = principalVariationGroup.sort(moves, 0, moves.size());

        assertEquals(0, result);
        assertEquals(List.of(move1, move2), moves);
    }

    @Test
    public void testBeforeSearchByDepthWithNullLastPVMoves() {
        principalVariationGroup.setLastPVMoves(null);
        principalVariationGroup.beforeSearchByDepth();

        principalVariationGroup.beforeSort(0);

        List<Move> moves = new ArrayList<>(List.of(move1, move2));
        int result = principalVariationGroup.sort(moves, 0, moves.size());

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
        int resultPly0 = principalVariationGroup.sort(movesPly0, 0, movesPly0.size());

        assertEquals(1, resultPly0);
        assertEquals(List.of(move1, move3, move4), movesPly0);

        // Ply 1
        principalVariationGroup.beforeSort(1);
        List<Move> movesPly1 = new ArrayList<>(List.of(move4, move3, move2));
        int resultPly1 = principalVariationGroup.sort(movesPly1, 0, movesPly1.size());

        assertEquals(1, resultPly1);
        assertEquals(List.of(move2, move3, move4), movesPly1);

        // Ply 2 (beyond lastPVMoves size)
        principalVariationGroup.beforeSort(2);
        List<Move> movesPly2 = new ArrayList<>(List.of(move3, move4));
        int resultPly2 = principalVariationGroup.sort(movesPly2, 0, movesPly2.size());

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
        List<Move> moves = new ArrayList<>(List.of(move1, move2, move3));

        int result = principalVariationGroup.sort(moves, 0, moves.size());

        assertEquals(0, result);
        assertEquals(List.of(move1, move2, move3), moves);
    }

    @Test
    public void testSortWithCurrentPVMoveAlreadyAtFrom() {
        PVMove pvMove = new PVMove(100L, move1);
        principalVariationGroup.setLastPVMoves(List.of(pvMove));
        principalVariationGroup.beforeSearchByDepth();
        principalVariationGroup.beforeSort(0);

        List<Move> moves = new ArrayList<>(List.of(move1, move2, move3));
        int result = principalVariationGroup.sort(moves, 0, moves.size());

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
        int result = principalVariationGroup.sort(moves, 0, moves.size());

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
        int result = principalVariationGroup.sort(moves, 1, 3);

        assertEquals(2, result);
        assertEquals(List.of(move1, move3, move2, move4), moves);
    }

    @Test
    public void testSortCurrentPVMoveNotInSubRange() {
        PVMove pvMove = new PVMove(100L, move4);
        principalVariationGroup.setLastPVMoves(List.of(pvMove));
        principalVariationGroup.beforeSearchByDepth();
        principalVariationGroup.beforeSort(0);

        List<Move> moves = new ArrayList<>(List.of(move1, move2, move3, move4));
        int result = principalVariationGroup.sort(moves, 0, 2);

        assertEquals(0, result);
        assertEquals(List.of(move1, move2, move3, move4), moves);
    }

    @Test
    public void testSortCurrentPVMoveNotInList() {
        Move missingMove = mock(Move.class);
        PVMove pvMove = new PVMove(100L, missingMove);
        principalVariationGroup.setLastPVMoves(List.of(pvMove));
        principalVariationGroup.beforeSearchByDepth();
        principalVariationGroup.beforeSort(0);

        List<Move> moves = new ArrayList<>(List.of(move1, move2, move3));
        int result = principalVariationGroup.sort(moves, 0, moves.size());

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
        int result1 = principalVariationGroup.sort(moves1, 0, moves1.size());
        assertEquals(1, result1);
        assertEquals(List.of(move2, move1), moves1);

        // Next sort call without beforeSort should have currentPVMove == null
        List<Move> moves2 = new ArrayList<>(List.of(move1, move2));
        int result2 = principalVariationGroup.sort(moves2, 0, moves2.size());
        assertEquals(0, result2);
        assertEquals(List.of(move1, move2), moves2);
    }

    @Test
    public void testBeforeSortClearsSlotInPvMoves() {
        PVMove pvMove = new PVMove(100L, move1);
        principalVariationGroup.setLastPVMoves(List.of(pvMove));
        principalVariationGroup.beforeSearchByDepth();

        // First beforeSort at ply 0 consumes pvMoves[0]
        principalVariationGroup.beforeSort(0);

        List<Move> moves1 = new ArrayList<>(List.of(move2, move1));
        principalVariationGroup.sort(moves1, 0, moves1.size());

        // Calling beforeSort(0) again should not reload pvMoves[0] since it was cleared to null
        principalVariationGroup.beforeSort(0);

        List<Move> moves2 = new ArrayList<>(List.of(move2, move1));
        int result2 = principalVariationGroup.sort(moves2, 0, moves2.size());
        assertEquals(0, result2);
        assertEquals(List.of(move2, move1), moves2);
    }
}
