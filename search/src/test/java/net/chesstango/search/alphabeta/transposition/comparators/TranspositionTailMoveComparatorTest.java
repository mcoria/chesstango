package net.chesstango.search.alphabeta.transposition.comparators;

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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static net.chesstango.search.Bound.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * @author Mauricio Coria
 */
@ExtendWith(MockitoExtension.class)
public class TranspositionTailMoveComparatorTest {

    private TranspositionTailMoveComparator tailMoveComparator;

    private TTable tTable;

    private MoveToHashMap moveToZobrist;


    @BeforeEach
    public void setup() {
        tTable = new TTableMap();
        moveToZobrist = new MoveToHashMap();
        tailMoveComparator = new TranspositionTailMoveComparator();
        tailMoveComparator.setTTable(tTable);
        tailMoveComparator.setMoveToZobrist(moveToZobrist);
    }

    @Test
    public void testAccept() {
        Visitor visitor = mock(Visitor.class);

        tailMoveComparator.accept(visitor);

        verify(visitor).visit(tailMoveComparator);
        verifyNoMoreInteractions(visitor);
    }

    @Test
    public void testGettersAndSetters() {
        assertEquals(tTable, tailMoveComparator.getTTable());

        TTable newTTable = mock(TTable.class);
        tailMoveComparator.setTTable(newTTable);
        assertEquals(newTTable, tailMoveComparator.getTTable());
    }

    @Test
    public void testGetZobristHashMoveFromCache() {
        Move move = mock(Move.class);
        MoveToHashMap mockMoveToZobrist = mock(MoveToHashMap.class);
        tailMoveComparator.setMoveToZobrist(mockMoveToZobrist);

        when(mockMoveToZobrist.read(move)).thenReturn(12345L);

        long hash = tailMoveComparator.getZobristHashMove(move);

        assertEquals(12345L, hash);
        verify(mockMoveToZobrist).read(move);
        verify(move, never()).getZobristHash();
        verify(mockMoveToZobrist, never()).write(any(), anyLong());
    }

    @Test
    public void testGetZobristHashMoveComputesAndCaches() {
        Move move = mock(Move.class);
        MoveToHashMap mockMoveToZobrist = mock(MoveToHashMap.class);
        tailMoveComparator.setMoveToZobrist(mockMoveToZobrist);

        when(mockMoveToZobrist.read(move)).thenReturn(0L);
        when(move.getZobristHash()).thenReturn(67890L);

        long hash = tailMoveComparator.getZobristHashMove(move);

        assertEquals(67890L, hash);
        verify(mockMoveToZobrist).read(move);
        verify(move).getZobristHash();
        verify(mockMoveToZobrist).write(move, 67890L);
    }

    @Test
    public void testCompareOneEntry() {
        /**
         * Settup
         */
        Game game = Game.from(FEN.START_POSITION);

        Move move = game.getMove(Square.c2, Square.c3);
        long hash = move.getZobristHash();  // El hash de la posicion luego de ejecutar el movimiento

        // Esta es la entrada que resulta luego de ejecutar el movimiento
        TranspositionEntry entry = new TranspositionEntry()
                .setHash(hash)
                .setBound(EXACT)
                .setDraft((byte) 1)
                .setValue(1);

        tTable.save(entry);

        /**
         * Assertions
         */
        game.getPossibleMoves().forEach(otherMove -> {
            if (move.equals(otherMove)) {
                assertEquals(0, tailMoveComparator.compare(move, otherMove));
                assertEquals(0, tailMoveComparator.compare(otherMove, move));
            } else {
                assertTrue(tailMoveComparator.compare(move, otherMove) > 0);
                assertTrue(tailMoveComparator.compare(otherMove, move) < 0);
            }
        });

        // El hash de la posicion luego de ejecutar el movimiento debe estar guardado en el hashmap
        assertEquals(hash, moveToZobrist.read(move));
    }

    @Test
    public void testCompareTwoEntries() {
        /**
         * Settup
         */
        Game game = Game.from(FEN.START_POSITION);

        /**
         * Movimiento 1
         */
        Move move1 = game.getMove(Square.c2, Square.c3);
        long hash1 = move1.getZobristHash();  // El hash de la posicion luego de ejecutar el movimiento

        // Esta es la entrada que resulta luego de ejecutar el movimiento
        TranspositionEntry entry1 = new TranspositionEntry()
                .setHash(hash1)
                .setBound(EXACT)
                .setDraft((byte) 1)
                .setValue(-10);
        tTable.save(entry1);


        /**
         * Movimiento 2
         */
        Move move2 = game.getMove(Square.c2, Square.c4);
        long hash2 = move2.getZobristHash();  // El hash de la posicion luego de ejecutar el movimiento

        // Esta es la entrada que resulta luego de ejecutar el movimiento
        TranspositionEntry entry2 = new TranspositionEntry()
                .setHash(hash2)
                .setBound(EXACT)
                .setDraft((byte) 1)
                .setValue(+10);
        tTable.save(entry2);

        /**
         * Assertions
         */
        game.getPossibleMoves().forEach(otherMove -> {
            if (move1.equals(otherMove)) {
                assertEquals(0, tailMoveComparator.compare(move1, otherMove));
                assertEquals(0, tailMoveComparator.compare(otherMove, move1));

                // Si ambas entradas estan presentes en el tTable, compara Entry.compareTo invertido
                assertEquals(entry1.compareTo(entry2), -tailMoveComparator.compare(move1, move2));

            } else if (move2.equals(otherMove)) {
                assertEquals(0, tailMoveComparator.compare(move2, otherMove));
                assertEquals(0, tailMoveComparator.compare(otherMove, move2));

                // Si ambas entradas estan presentes en el tTable, compara Entry.compareTo invertido
                assertEquals(entry2.compareTo(entry1), -tailMoveComparator.compare(move2, move1));
            } else {
                assertTrue(tailMoveComparator.compare(move1, otherMove) > 0);
                assertTrue(tailMoveComparator.compare(otherMove, move1) < 0);

                assertTrue(tailMoveComparator.compare(move2, otherMove) > 0);
                assertTrue(tailMoveComparator.compare(otherMove, move2) < 0);
            }
        });

        // El hash de la posicion luego de ejecutar el movimiento debe estar guardado en el hashmap
        assertEquals(hash1, moveToZobrist.read(move1));
        assertEquals(hash2, moveToZobrist.read(move2));
    }

    @Test
    public void testCompareNeitherEntryInTable() {
        Game game = Game.from(FEN.START_POSITION);
        Move move1 = game.getMove(Square.e2, Square.e4);
        Move move2 = game.getMove(Square.d2, Square.d4);

        assertEquals(0, tailMoveComparator.compare(move1, move2));
        assertEquals(0, tailMoveComparator.compare(move2, move1));
    }

    @Test
    public void testCompareOnlyMove1InTable() {
        Game game = Game.from(FEN.START_POSITION);
        Move move1 = game.getMove(Square.e2, Square.e4);
        Move move2 = game.getMove(Square.d2, Square.d4);

        long hash1 = move1.getZobristHash();
        tTable.save(new TranspositionEntry()
                .setHash(hash1)
                .setBound(EXACT)
                .setDraft((byte) 1)
                .setValue(50));

        assertEquals(1, tailMoveComparator.compare(move1, move2));
        assertEquals(-1, tailMoveComparator.compare(move2, move1));
    }

    @Test
    public void testCompareOnlyMove2InTable() {
        Game game = Game.from(FEN.START_POSITION);
        Move move1 = game.getMove(Square.e2, Square.e4);
        Move move2 = game.getMove(Square.d2, Square.d4);

        long hash2 = move2.getZobristHash();
        tTable.save(new TranspositionEntry()
                .setHash(hash2)
                .setBound(EXACT)
                .setDraft((byte) 1)
                .setValue(50));

        assertEquals(-1, tailMoveComparator.compare(move1, move2));
        assertEquals(1, tailMoveComparator.compare(move2, move1));
    }

    @Test
    public void testCompareBothInTableDifferentValues() {
        Game game = Game.from(FEN.START_POSITION);
        Move move1 = game.getMove(Square.e2, Square.e4);
        Move move2 = game.getMove(Square.d2, Square.d4);

        long hash1 = move1.getZobristHash();
        long hash2 = move2.getZobristHash();

        // move1 results in an opponent position evaluated at -500 (lower score for opponent -> better for player)
        tTable.save(new TranspositionEntry()
                .setHash(hash1)
                .setBound(EXACT)
                .setDraft((byte) 1)
                .setValue(-500));

        // move2 results in an opponent position evaluated at +200 (higher score for opponent -> worse for player)
        tTable.save(new TranspositionEntry()
                .setHash(hash2)
                .setBound(EXACT)
                .setDraft((byte) 1)
                .setValue(200));

        assertTrue(tailMoveComparator.compare(move1, move2) > 0);
        assertTrue(tailMoveComparator.compare(move2, move1) < 0);
    }

    @Test
    public void testCompareBothInTableSameValueDifferentBounds() {
        Game game = Game.from(FEN.START_POSITION);
        Move moveUpperBound = game.getMove(Square.e2, Square.e4);
        Move moveExact = game.getMove(Square.d2, Square.d4);
        Move moveLowerBound = game.getMove(Square.c2, Square.c4);

        long hashUpper = moveUpperBound.getZobristHash();
        long hashExact = moveExact.getZobristHash();
        long hashLower = moveLowerBound.getZobristHash();

        int sharedValue = 100;

        tTable.save(new TranspositionEntry()
                .setHash(hashUpper)
                .setBound(UPPER_BOUND)
                .setDraft((byte) 1)
                .setValue(sharedValue));

        tTable.save(new TranspositionEntry()
                .setHash(hashExact)
                .setBound(EXACT)
                .setDraft((byte) 1)
                .setValue(sharedValue));

        tTable.save(new TranspositionEntry()
                .setHash(hashLower)
                .setBound(LOWER_BOUND)
                .setDraft((byte) 1)
                .setValue(sharedValue));

        // UPPER_BOUND < EXACT < LOWER_BOUND in Bound enum ordinal.
        // Because comparator returns -entry1.compareTo(entry2):
        // UPPER_BOUND vs EXACT: -(-1) = 1 > 0 -> UPPER_BOUND is preferred over EXACT
        assertTrue(tailMoveComparator.compare(moveUpperBound, moveExact) > 0);
        assertTrue(tailMoveComparator.compare(moveExact, moveUpperBound) < 0);

        // EXACT vs LOWER_BOUND: -(-1) = 1 > 0 -> EXACT is preferred over LOWER_BOUND
        assertTrue(tailMoveComparator.compare(moveExact, moveLowerBound) > 0);
        assertTrue(tailMoveComparator.compare(moveLowerBound, moveExact) < 0);

        // UPPER_BOUND vs LOWER_BOUND: -(-1) = 1 > 0 -> UPPER_BOUND is preferred over LOWER_BOUND
        assertTrue(tailMoveComparator.compare(moveUpperBound, moveLowerBound) > 0);
        assertTrue(tailMoveComparator.compare(moveLowerBound, moveUpperBound) < 0);
    }

    @Test
    public void testCompareBothInTableSameValueSameBound() {
        Game game = Game.from(FEN.START_POSITION);
        Move move1 = game.getMove(Square.e2, Square.e4);
        Move move2 = game.getMove(Square.d2, Square.d4);

        long hash1 = move1.getZobristHash();
        long hash2 = move2.getZobristHash();

        tTable.save(new TranspositionEntry()
                .setHash(hash1)
                .setBound(EXACT)
                .setDraft((byte) 1)
                .setValue(100));

        tTable.save(new TranspositionEntry()
                .setHash(hash2)
                .setBound(EXACT)
                .setDraft((byte) 1)
                .setValue(100));

        assertEquals(0, tailMoveComparator.compare(move1, move2));
        assertEquals(0, tailMoveComparator.compare(move2, move1));
    }

    @Test
    public void testCompareSameMove() {
        Game game = Game.from(FEN.START_POSITION);
        Move move = game.getMove(Square.e2, Square.e4);

        // Not in TT
        assertEquals(0, tailMoveComparator.compare(move, move));

        // In TT
        long hash = move.getZobristHash();
        tTable.save(new TranspositionEntry()
                .setHash(hash)
                .setBound(EXACT)
                .setDraft((byte) 1)
                .setValue(100));

        assertEquals(0, tailMoveComparator.compare(move, move));
    }

    @Test
    public void testCompareWithHashCollision() {
        Game game = Game.from(FEN.START_POSITION);
        Move move1 = game.getMove(Square.e2, Square.e4);
        Move move2 = game.getMove(Square.d2, Square.d4);

        long hash1 = move1.getZobristHash();
        long hash2 = move2.getZobristHash();

        TTable mockTTable = mock(TTable.class);
        tailMoveComparator.setTTable(mockTTable);

        // move1 has a hash mismatch in TT (collision: load returns true, but hash does not match)
        doAnswer(invocation -> {
            TranspositionEntry entry = invocation.getArgument(1);
            entry.setHash(999L);
            return true;
        }).when(mockTTable).load(eq(hash1), any(TranspositionEntry.class));

        // move2 is a valid hit (hash matches)
        doAnswer(invocation -> {
            TranspositionEntry entry = invocation.getArgument(1);
            entry.setHash(hash2);
            entry.setBound(EXACT);
            entry.setValue(10);
            return true;
        }).when(mockTTable).load(eq(hash2), any(TranspositionEntry.class));

        // move1 is treated as not loaded due to collision; move2 is loaded -> move2 preferred
        assertEquals(-1, tailMoveComparator.compare(move1, move2));
        assertEquals(1, tailMoveComparator.compare(move2, move1));

        // If move2 is not in TT either (load returns false)
        when(mockTTable.load(eq(hash2), any(TranspositionEntry.class))).thenReturn(false);
        assertEquals(0, tailMoveComparator.compare(move1, move2));
        assertEquals(0, tailMoveComparator.compare(move2, move1));
    }

    @Test
    public void testSortListOfMoves() {
        Game game = Game.from(FEN.START_POSITION);

        Move moveBest = game.getMove(Square.e2, Square.e4);
        Move moveGood = game.getMove(Square.d2, Square.d4);
        Move moveSameValUpper = game.getMove(Square.c2, Square.c4);
        Move moveSameValExact = game.getMove(Square.g1, Square.f3);
        Move moveSameValLower = game.getMove(Square.b1, Square.c3);
        Move moveWorstTT = game.getMove(Square.a2, Square.a3);
        Move moveNoTT1 = game.getMove(Square.h2, Square.h3);
        Move moveNoTT2 = game.getMove(Square.b2, Square.b3);

        tTable.save(new TranspositionEntry().setHash(moveBest.getZobristHash()).setBound(EXACT).setValue(-500));
        tTable.save(new TranspositionEntry().setHash(moveGood.getZobristHash()).setBound(EXACT).setValue(0));
        tTable.save(new TranspositionEntry().setHash(moveSameValUpper.getZobristHash()).setBound(UPPER_BOUND).setValue(100));
        tTable.save(new TranspositionEntry().setHash(moveSameValExact.getZobristHash()).setBound(EXACT).setValue(100));
        tTable.save(new TranspositionEntry().setHash(moveSameValLower.getZobristHash()).setBound(LOWER_BOUND).setValue(100));
        tTable.save(new TranspositionEntry().setHash(moveWorstTT.getZobristHash()).setBound(EXACT).setValue(500));

        List<Move> moves = new ArrayList<>(List.of(
                moveNoTT1,
                moveWorstTT,
                moveSameValLower,
                moveBest,
                moveNoTT2,
                moveSameValExact,
                moveGood,
                moveSameValUpper
        ));

        // Sorters sort with comparator.reversed() to order descending (highest priority first)
        moves.sort(tailMoveComparator.reversed());

        assertEquals(moveBest, moves.get(0));
        assertEquals(moveGood, moves.get(1));
        assertEquals(moveSameValUpper, moves.get(2));
        assertEquals(moveSameValExact, moves.get(3));
        assertEquals(moveSameValLower, moves.get(4));
        assertEquals(moveWorstTT, moves.get(5));
        assertTrue(moves.get(6) == moveNoTT1 || moves.get(6) == moveNoTT2);
        assertTrue(moves.get(7) == moveNoTT1 || moves.get(7) == moveNoTT2);
    }
}
