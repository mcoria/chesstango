package net.chesstango.search.alphabeta.transposition.groupsorters;

import net.chesstango.board.Game;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.containers.MoveToHashMap;
import net.chesstango.gardel.fen.FEN;
import net.chesstango.search.Bound;
import net.chesstango.search.Visitor;
import net.chesstango.search.alphabeta.transposition.TTable;
import net.chesstango.search.alphabeta.transposition.TTableMap;
import net.chesstango.search.alphabeta.transposition.TranspositionEntry;
import net.chesstango.search.alphabeta.transposition.comparators.TranspositionTailMoveComparator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * @author Mauricio Coria
 */
@ExtendWith(MockitoExtension.class)
public class TranspositionTailMoveGroupTest {

    private TranspositionTailMoveGroup transpositionTailMoveGroup;

    @Mock
    private TTable tTable;

    @Mock
    private MoveToHashMap moveToZobrist;

    @Mock
    private Comparator<Move> moveComparator;

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

    @Mock
    private Move move5;

    @Mock
    private Move move6;

    @BeforeEach
    public void setUp() {
        transpositionTailMoveGroup = new TranspositionTailMoveGroup();
        transpositionTailMoveGroup.setTTable(tTable);
        transpositionTailMoveGroup.setMoveToZobrist(moveToZobrist);
        transpositionTailMoveGroup.setMoveComparator(moveComparator);
    }

    @Test
    public void testAccept() {
        transpositionTailMoveGroup.accept(visitor);

        verify(visitor).visit(transpositionTailMoveGroup);
        verifyNoMoreInteractions(visitor);
    }

    @Test
    public void testGettersAndSetters() {
        assertEquals(tTable, transpositionTailMoveGroup.getTTable());

        TTable newTTable = mock(TTable.class);
        transpositionTailMoveGroup.setTTable(newTTable);
        assertEquals(newTTable, transpositionTailMoveGroup.getTTable());
    }

    @Test
    public void testGetZobristHashMoveFromCache() {
        when(moveToZobrist.read(move1)).thenReturn(12345L);

        long hash = transpositionTailMoveGroup.getZobristHashMove(move1);

        assertEquals(12345L, hash);
        verify(moveToZobrist).read(move1);
        verify(move1, never()).getZobristHash();
        verify(moveToZobrist, never()).write(any(), anyLong());
    }

    @Test
    public void testGetZobristHashMoveComputesAndCaches() {
        when(moveToZobrist.read(move1)).thenReturn(0L);
        when(move1.getZobristHash()).thenReturn(67890L);

        long hash = transpositionTailMoveGroup.getZobristHashMove(move1);

        assertEquals(67890L, hash);
        verify(moveToZobrist).read(move1);
        verify(move1).getZobristHash();
        verify(moveToZobrist).write(move1, 67890L);
    }

    @Test
    public void testSortEmptyRange() {
        List<Move> moves = new ArrayList<>(List.of(move1, move2));

        int result = transpositionTailMoveGroup.sort(moves, 1, 1);

        assertEquals(1, result);
        assertEquals(List.of(move1, move2), moves);
        verifyNoInteractions(moveComparator);
    }

    @Test
    public void testSortSingleElementRange() {
        List<Move> moves = new ArrayList<>(List.of(move1, move2));

        int result = transpositionTailMoveGroup.sort(moves, 0, 1);

        assertEquals(0, result);
        assertEquals(List.of(move1, move2), moves);
        verifyNoInteractions(moveComparator);
    }

    @Test
    public void testSortTwoMovesBothMiss() {
        when(moveToZobrist.read(move1)).thenReturn(100L);
        when(moveToZobrist.read(move2)).thenReturn(200L);

        when(tTable.load(eq(100L), any(TranspositionEntry.class))).thenReturn(false);
        when(tTable.load(eq(200L), any(TranspositionEntry.class))).thenReturn(false);

        List<Move> moves = new ArrayList<>(List.of(move1, move2));

        int result = transpositionTailMoveGroup.sort(moves, 0, moves.size());

        assertEquals(0, result);
        assertEquals(List.of(move1, move2), moves);
        verifyNoInteractions(moveComparator);
    }

    @Test
    public void testSortTwoMovesHitThenMiss() {
        when(moveToZobrist.read(move1)).thenReturn(100L);

        setupTTableHit(100L);

        List<Move> moves = new ArrayList<>(List.of(move1, move2));

        int result = transpositionTailMoveGroup.sort(moves, 0, moves.size());

        assertEquals(1, result);
        assertEquals(List.of(move1, move2), moves);
    }

    @Test
    public void testSortTwoMovesMissThenHit() {
        when(moveToZobrist.read(move1)).thenReturn(100L);
        when(moveToZobrist.read(move2)).thenReturn(200L);

        when(tTable.load(eq(100L), any(TranspositionEntry.class))).thenReturn(false);
        setupTTableHit(200L);

        List<Move> moves = new ArrayList<>(List.of(move1, move2));

        int result = transpositionTailMoveGroup.sort(moves, 0, moves.size());

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

        int result = transpositionTailMoveGroup.sort(moves, 0, moves.size());

        assertEquals(0, result);
        assertEquals(List.of(move1, move2, move3), moves);
        verifyNoInteractions(moveComparator);
    }

    @Test
    public void testSortAllMovesInTTable() {
        when(moveToZobrist.read(move1)).thenReturn(100L);
        when(moveToZobrist.read(move2)).thenReturn(200L);

        setupTTableHit(100L);
        setupTTableHit(200L);

        List<Move> moves = new ArrayList<>(List.of(move1, move2, move3));

        int result = transpositionTailMoveGroup.sort(moves, 0, moves.size());

        assertEquals(2, result);
        verify(moveComparator, atLeastOnce()).compare(any(), any());
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
        setupTTableHit(200L);

        List<Move> moves = new ArrayList<>(List.of(move1, move2));

        int result = transpositionTailMoveGroup.sort(moves, 0, moves.size());

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
        setupTTableHit(300L);
        setupTTableHit(400L);

        List<Move> moves = new ArrayList<>(List.of(move1, move2, move3, move4));

        int result = transpositionTailMoveGroup.sort(moves, 0, moves.size());

        assertEquals(2, result);
        assertEquals(move4, moves.get(0));
        assertEquals(move3, moves.get(1));
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

        setupTTableHit(100L);
        setupTTableHit(200L);
        when(tTable.load(eq(300L), any(TranspositionEntry.class))).thenReturn(false);
        when(tTable.load(eq(400L), any(TranspositionEntry.class))).thenReturn(false);

        List<Move> moves = new ArrayList<>(List.of(move1, move2, move3, move4));

        int result = transpositionTailMoveGroup.sort(moves, 0, moves.size());

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

        when(tTable.load(eq(200L), any(TranspositionEntry.class))).thenReturn(false);
        when(tTable.load(eq(300L), any(TranspositionEntry.class))).thenReturn(false);
        setupTTableHit(400L);
        setupTTableHit(500L);

        List<Move> moves = new ArrayList<>(List.of(move1, move2, move3, move4, move5, move6));

        int result = transpositionTailMoveGroup.sort(moves, 1, 5);

        assertEquals(3, result);
        // move1 and move6 remain undisturbed outside range
        assertEquals(move1, moves.get(0));
        assertEquals(move6, moves.get(5));
        assertEquals(move5, moves.get(1));
        assertEquals(move4, moves.get(2));
        assertEquals(move3, moves.get(3));
        assertEquals(move2, moves.get(4));
    }

    @Test
    public void testSortAppliesComparatorToSortedSubList() {
        when(moveToZobrist.read(move1)).thenReturn(100L);
        when(moveToZobrist.read(move2)).thenReturn(200L);
        when(moveToZobrist.read(move3)).thenReturn(300L);
        when(moveToZobrist.read(move4)).thenReturn(400L);

        setupTTableHit(100L);
        setupTTableHit(200L);
        when(tTable.load(eq(300L), any(TranspositionEntry.class))).thenReturn(false);
        when(tTable.load(eq(400L), any(TranspositionEntry.class))).thenReturn(false);

        // Comparator sorts move2 before move1
        Comparator<Move> reverseComparator = (m1, m2) -> {
            if (m1 == move1 && m2 == move2) return 1;
            if (m1 == move2 && m2 == move1) return -1;
            return 0;
        };
        transpositionTailMoveGroup.setMoveComparator(reverseComparator);

        List<Move> moves = new ArrayList<>(List.of(move1, move2, move3, move4));

        int result = transpositionTailMoveGroup.sort(moves, 0, moves.size());

        assertEquals(2, result);
        assertEquals(move2, moves.get(0));
        assertEquals(move1, moves.get(1));
    }

    @Test
    public void testIntegrationWithGameAndRealTTable() {
        Game game = Game.from(FEN.START_POSITION);
        TTable realTTable = new TTableMap();
        MoveToHashMap realMoveToZobrist = new MoveToHashMap();
        TranspositionTailMoveComparator realComparator = new TranspositionTailMoveComparator();
        realComparator.setTTable(realTTable);
        realComparator.setMoveToZobrist(realMoveToZobrist);

        transpositionTailMoveGroup.setTTable(realTTable);
        transpositionTailMoveGroup.setMoveToZobrist(realMoveToZobrist);
        transpositionTailMoveGroup.setMoveComparator(realComparator);

        Move e2e4 = game.getMove(Square.e2, Square.e4);
        Move d2d4 = game.getMove(Square.d2, Square.d4);

        long hashE4 = e2e4.getZobristHash();
        long hashD4 = d2d4.getZobristHash();

        TranspositionEntry entryE4 = new TranspositionEntry()
                .setHash(hashE4)
                .setBound(Bound.EXACT)
                .setDraft((byte) 2)
                .setValue(50);

        TranspositionEntry entryD4 = new TranspositionEntry()
                .setHash(hashD4)
                .setBound(Bound.EXACT)
                .setDraft((byte) 2)
                .setValue(100);

        realTTable.save(entryE4);
        realTTable.save(entryD4);

        List<Move> moves = new ArrayList<>();
        game.getPossibleMoves().forEach(moves::add);

        int result = transpositionTailMoveGroup.sort(moves, 0, moves.size());

        assertEquals(2, result);
        assertEquals(d2d4, moves.get(0));
        assertEquals(e2e4, moves.get(1));

        assertEquals(hashD4, realMoveToZobrist.read(d2d4));
        assertEquals(hashE4, realMoveToZobrist.read(e2e4));
    }

    private void setupTTableHit(long hash) {
        doAnswer(invocation -> {
            TranspositionEntry entry = invocation.getArgument(1);
            entry.setHash(hash);
            return true;
        }).when(tTable).load(eq(hash), any(TranspositionEntry.class));
    }
}
