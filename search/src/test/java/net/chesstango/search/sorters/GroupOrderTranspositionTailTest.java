package net.chesstango.search.sorters;

import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.containers.MoveToHashMap;
import net.chesstango.search.alphabeta.transposition.TTable;
import net.chesstango.search.alphabeta.transposition.TranspositionEntry;
import net.chesstango.search.alphabeta.transposition.comparators.TranspositionTailMoveComparator;
import net.chesstango.search.alphabeta.transposition.groupsorters.TranspositionTailMoveGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

/**
 * @author Mauricio Coria
 */
@ExtendWith(MockitoExtension.class)
public class GroupOrderTranspositionTailTest {

    private TranspositionTailMoveGroup transpositionTailMoveGroup;

    private TranspositionTailMoveComparator tailMoveComparator;

    private GroupOrder groupOrder;

    @Mock
    private TTable tTable;

    @Mock
    private MoveToHashMap moveToZobrist;

    @Mock
    private Move move1;

    @Mock
    private Move move2;

    @Mock
    private Move move3;

    @Mock
    private Move move4;

    @Mock
    private Move move5;

    @Mock
    private Move move6;

    @BeforeEach
    public void setUp() {
        transpositionTailMoveGroup = new TranspositionTailMoveGroup();
        transpositionTailMoveGroup.setTTable(tTable);
        transpositionTailMoveGroup.setMoveToZobrist(moveToZobrist);

        tailMoveComparator = new TranspositionTailMoveComparator();
        tailMoveComparator.setTTable(tTable);
        tailMoveComparator.setMoveToZobrist(moveToZobrist);


        groupOrder = GroupOrder.createTranspositionTailMoveGroup(transpositionTailMoveGroup, tailMoveComparator);
    }


    @Test
    public void testSortSingleElementRange() {
        List<Move> moves = new ArrayList<>(List.of(move1, move2));

        int result = groupOrder.sort(moves, 0);

        assertEquals(0, result);
        assertEquals(List.of(move1, move2), moves);
    }

    @Test
    public void testSortTwoMovesBothMiss() {
        when(moveToZobrist.read(move1)).thenReturn(100L);
        when(moveToZobrist.read(move2)).thenReturn(200L);

        when(tTable.load(eq(100L), any(TranspositionEntry.class))).thenReturn(false);
        when(tTable.load(eq(200L), any(TranspositionEntry.class))).thenReturn(false);

        List<Move> moves = new ArrayList<>(List.of(move1, move2));

        int result = groupOrder.sort(moves, 0);

        assertEquals(0, result);
        assertEquals(List.of(move1, move2), moves);
    }

    @Test
    public void testSortTwoMovesHitThenMiss() {
        when(moveToZobrist.read(move1)).thenReturn(100L);

        setupTTableHit(100L, 1);

        List<Move> moves = new ArrayList<>(List.of(move1, move2));

        int result = groupOrder.sort(moves, 0);

        assertEquals(1, result);
        assertEquals(List.of(move1, move2), moves);
    }

    @Test
    public void testSortTwoMovesMissThenHit() {
        when(moveToZobrist.read(move1)).thenReturn(100L);
        when(moveToZobrist.read(move2)).thenReturn(200L);

        when(tTable.load(eq(100L), any(TranspositionEntry.class))).thenReturn(false);
        setupTTableHit(200L, 2);

        List<Move> moves = new ArrayList<>(List.of(move1, move2));

        int result = groupOrder.sort(moves, 0);

        assertEquals(1, result);
        assertEquals(List.of(move2, move1), moves);
    }

    @Test
    public void testSortNoMovesInTTable() {
        when(moveToZobrist.read(move1)).thenReturn(100L);
        when(moveToZobrist.read(move2)).thenReturn(200L);
        when(moveToZobrist.read(move3)).thenReturn(300L);

        when(tTable.load(eq(100L), any(TranspositionEntry.class))).thenReturn(false);
        when(tTable.load(eq(200L), any(TranspositionEntry.class))).thenReturn(false);
        when(tTable.load(eq(300L), any(TranspositionEntry.class))).thenReturn(false);

        List<Move> moves = new ArrayList<>(List.of(move1, move2, move3));

        int result = groupOrder.sort(moves, 0);

        assertEquals(0, result);
        assertEquals(List.of(move1, move2, move3), moves);
    }

    @Test
    public void testSortAllMovesInTTable() {
        when(moveToZobrist.read(move1)).thenReturn(100L);
        when(moveToZobrist.read(move2)).thenReturn(200L);

        setupTTableHit(100L, 1);
        setupTTableHit(200L, 2);

        List<Move> moves = new ArrayList<>(List.of(move1, move2, move3));

        int result = groupOrder.sort(moves, 0);

        assertEquals(2, result);
    }

    @Test
    public void testSortWithTTableHitButHashMismatch() {
        when(moveToZobrist.read(move1)).thenReturn(100L);
        when(moveToZobrist.read(move2)).thenReturn(200L);

        // move1 has a hash mismatch in TT (collision)
        doAnswer(invocation -> {
            TranspositionEntry entry = invocation.getArgument(1);
            entry.setHash(999L);
            return true;
        }).when(tTable).load(eq(100L), any(TranspositionEntry.class));

        // move2 is a valid hit
        setupTTableHit(200L, 1);

        List<Move> moves = new ArrayList<>(List.of(move1, move2));

        int result = groupOrder.sort(moves, 0);

        assertEquals(1, result);
        assertEquals(List.of(move2, move1), moves);
    }

    @Test
    public void testSortSwapMovesToFront() {
        // move1 (miss), move2 (miss), move3 (hit), move4 (hit)
        when(moveToZobrist.read(move1)).thenReturn(100L);
        when(moveToZobrist.read(move2)).thenReturn(200L);
        when(moveToZobrist.read(move3)).thenReturn(300L);
        when(moveToZobrist.read(move4)).thenReturn(400L);

        when(tTable.load(eq(100L), any(TranspositionEntry.class))).thenReturn(false);
        when(tTable.load(eq(200L), any(TranspositionEntry.class))).thenReturn(false);
        setupTTableHit(300L, 2);
        setupTTableHit(400L, 1);

        List<Move> moves = new ArrayList<>(List.of(move1, move3, move2, move4));

        int result = groupOrder.sort(moves, 0);

        assertEquals(2, result);
        assertEquals(move3, moves.get(0));
        assertEquals(move4, moves.get(1));
        assertEquals(move2, moves.get(2));
        assertEquals(move1, moves.get(3));
    }

    @Test
    public void testSortAlreadyPartitioned() {
        // move1 (hit), move2 (hit), move3 (miss), move4 (miss)
        when(moveToZobrist.read(move1)).thenReturn(100L);
        when(moveToZobrist.read(move2)).thenReturn(200L);
        when(moveToZobrist.read(move3)).thenReturn(300L);
        when(moveToZobrist.read(move4)).thenReturn(400L);

        setupTTableHit(100L, 2);
        setupTTableHit(200L, 1);
        when(tTable.load(eq(300L), any(TranspositionEntry.class))).thenReturn(false);
        when(tTable.load(eq(400L), any(TranspositionEntry.class))).thenReturn(false);

        List<Move> moves = new ArrayList<>(List.of(move1, move2, move3, move4));

        int result = groupOrder.sort(moves, 0);

        assertEquals(2, result);
        assertEquals(List.of(move1, move2, move3, move4), moves);
    }

    @Test
    public void testSortSubRange() {
        // Moves: [move1, move2 (miss), move3 (miss), move4 (hit), move5 (hit), move6]
        // Range: from = 1, to = 5
        when(moveToZobrist.read(move2)).thenReturn(200L);
        when(moveToZobrist.read(move3)).thenReturn(300L);
        when(moveToZobrist.read(move4)).thenReturn(400L);
        when(moveToZobrist.read(move5)).thenReturn(500L);
        when(moveToZobrist.read(move6)).thenReturn(600L);

        when(tTable.load(eq(200L), any(TranspositionEntry.class))).thenReturn(false);
        when(tTable.load(eq(300L), any(TranspositionEntry.class))).thenReturn(false);
        setupTTableHit(400L, 1);
        setupTTableHit(500L, 2);

        List<Move> moves = new ArrayList<>(List.of(move1, move2, move3, move4, move5, move6));

        int result = groupOrder.sort(moves, 1);

        assertEquals(3, result);
        // move1 and move6 remain undisturbed outside range
        assertEquals(move1, moves.get(0));
        assertEquals(move6, moves.get(5));
        assertEquals(move5, moves.get(1));
        assertEquals(move4, moves.get(2));
        assertEquals(move2, moves.get(3));
        assertEquals(move3, moves.get(4));
    }

    private void setupTTableHit(long hash, int value) {
        doAnswer(invocation -> {
            TranspositionEntry entry = invocation.getArgument(1);
            entry.setHash(hash);
            entry.setValue(value);
            return true;
        }).when(tTable).load(eq(hash), any(TranspositionEntry.class));
    }
}
