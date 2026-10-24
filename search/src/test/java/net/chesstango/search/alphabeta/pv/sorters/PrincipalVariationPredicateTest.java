package net.chesstango.search.alphabeta.pv.sorters;

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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

/**
 * @author Mauricio Coria
 */
@ExtendWith(MockitoExtension.class)
public class PrincipalVariationPredicateTest {

    private PrincipalVariationPredicate principalVariationPredicate;

    @Mock
    private Visitor visitor;

    @Mock
    private Move move1;

    @Mock
    private Move move2;

    @Mock
    private Move move3;

    @BeforeEach
    public void setUp() {
        principalVariationPredicate = new PrincipalVariationPredicate();
    }

    @Test
    public void testAccept() {
        principalVariationPredicate.accept(visitor);

        verify(visitor).visit(principalVariationPredicate);
        verifyNoMoreInteractions(visitor);
    }

    @Test
    public void testInitialState() {
        assertFalse(principalVariationPredicate.test(move1));
    }

    @Test
    public void testTestWithMatchingMove() {
        PVMove pvMove = new PVMove(12345L, move1);
        principalVariationPredicate.setLastPVMoves(List.of(pvMove));
        principalVariationPredicate.beforeSearchByDepth();
        principalVariationPredicate.beforeSort(0);

        assertTrue(principalVariationPredicate.test(move1));
        assertFalse(principalVariationPredicate.test(move2));
    }

    @Test
    public void testBeforeSortConsumesMove() {
        PVMove pvMove = new PVMove(12345L, move1);
        principalVariationPredicate.setLastPVMoves(List.of(pvMove));
        principalVariationPredicate.beforeSearchByDepth();

        principalVariationPredicate.beforeSort(0);
        assertTrue(principalVariationPredicate.test(move1));

        principalVariationPredicate.beforeSort(0);
        assertFalse(principalVariationPredicate.test(move1));
    }

    @Test
    public void testBeforeSortWithNoMoveAtPly() {
        PVMove pvMove = new PVMove(12345L, move1);
        principalVariationPredicate.setLastPVMoves(List.of(pvMove));
        principalVariationPredicate.beforeSearchByDepth();

        principalVariationPredicate.beforeSort(1);
        assertFalse(principalVariationPredicate.test(move1));
        assertFalse(principalVariationPredicate.test(move2));
    }

    @Test
    public void testBeforeSortResetsCurrentPVMoveWhenPlyIsNull() {
        PVMove pvMove = new PVMove(12345L, move1);
        principalVariationPredicate.setLastPVMoves(List.of(pvMove));
        principalVariationPredicate.beforeSearchByDepth();

        principalVariationPredicate.beforeSort(0);
        assertTrue(principalVariationPredicate.test(move1));

        principalVariationPredicate.beforeSort(1);
        assertFalse(principalVariationPredicate.test(move1));
    }

    @Test
    public void testMultiPlyProgression() {
        PVMove pvMove0 = new PVMove(100L, move1);
        PVMove pvMove1 = new PVMove(200L, move2);
        PVMove pvMove2 = new PVMove(300L, move3);

        principalVariationPredicate.setLastPVMoves(List.of(pvMove0, pvMove1, pvMove2));
        principalVariationPredicate.beforeSearchByDepth();

        // Ply 0
        principalVariationPredicate.beforeSort(0);
        assertTrue(principalVariationPredicate.test(move1));
        assertFalse(principalVariationPredicate.test(move2));
        assertFalse(principalVariationPredicate.test(move3));

        // Ply 1
        principalVariationPredicate.beforeSort(1);
        assertFalse(principalVariationPredicate.test(move1));
        assertTrue(principalVariationPredicate.test(move2));
        assertFalse(principalVariationPredicate.test(move3));

        // Ply 2
        principalVariationPredicate.beforeSort(2);
        assertFalse(principalVariationPredicate.test(move1));
        assertFalse(principalVariationPredicate.test(move2));
        assertTrue(principalVariationPredicate.test(move3));

        // Ply 3 (beyond lastPVMoves)
        principalVariationPredicate.beforeSort(3);
        assertFalse(principalVariationPredicate.test(move1));
        assertFalse(principalVariationPredicate.test(move2));
        assertFalse(principalVariationPredicate.test(move3));
    }

    @Test
    public void testBeforeSearchClearsLastPVMoves() {
        PVMove pvMove = new PVMove(100L, move1);
        principalVariationPredicate.setLastPVMoves(List.of(pvMove));

        principalVariationPredicate.beforeSearch();
        principalVariationPredicate.beforeSearchByDepth();
        principalVariationPredicate.beforeSort(0);

        assertFalse(principalVariationPredicate.test(move1));
    }

    @Test
    public void testBeforeSearchByDepthWithNullLastPVMoves() {
        principalVariationPredicate.setLastPVMoves(null);
        principalVariationPredicate.beforeSearchByDepth();
        principalVariationPredicate.beforeSort(0);

        assertFalse(principalVariationPredicate.test(move1));
    }

    @Test
    public void testBeforeSearchByDepthClearsPreviousDepthMoves() {
        PVMove pvMove0 = new PVMove(100L, move1);
        PVMove pvMove1 = new PVMove(200L, move2);

        // Depth 2
        principalVariationPredicate.setLastPVMoves(List.of(pvMove0, pvMove1));
        principalVariationPredicate.beforeSearchByDepth();

        // Depth 1 (shorter PV)
        PVMove newPvMove0 = new PVMove(300L, move3);
        principalVariationPredicate.setLastPVMoves(List.of(newPvMove0));
        principalVariationPredicate.beforeSearchByDepth();

        principalVariationPredicate.beforeSort(0);
        assertTrue(principalVariationPredicate.test(move3));
        assertFalse(principalVariationPredicate.test(move1));

        principalVariationPredicate.beforeSort(1);
        assertFalse(principalVariationPredicate.test(move2));
    }

    @Test
    public void testLastPVMovesExceedingMaxDepth() {
        List<PVMove> largePV = new ArrayList<>();
        for (int i = 0; i < MAX_DEPTH + 10; i++) {
            Move m = mock(Move.class);
            largePV.add(new PVMove((long) i, m));
        }

        principalVariationPredicate.setLastPVMoves(largePV);
        principalVariationPredicate.beforeSearchByDepth();

        // Slot 0 to MAX_DEPTH - 1 should have entries
        principalVariationPredicate.beforeSort(0);
        assertTrue(principalVariationPredicate.test(largePV.get(0).move()));

        principalVariationPredicate.beforeSort(MAX_DEPTH - 1);
        assertTrue(principalVariationPredicate.test(largePV.get(MAX_DEPTH - 1).move()));
    }
}
