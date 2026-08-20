package net.chesstango.search.smart.transposition.comparators;

import net.chesstango.board.Game;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.containers.MoveToHashMap;
import net.chesstango.gardel.fen.FEN;
import net.chesstango.search.smart.transposition.TTable;
import net.chesstango.search.smart.transposition.TTableMap;
import net.chesstango.search.smart.transposition.TranspositionEntry;
import net.chesstango.search.sorters.MoveComparator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static net.chesstango.search.Bound.EXACT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * @author Mauricio Coria
 */
@ExtendWith(MockitoExtension.class)
public class TranspositionTailMoveComparatorTest {

    private TranspositionTailMoveComparator tailMoveComparator;

    private TTable tTable;

    private MoveToHashMap moveToZobrist;

    @Mock
    private MoveComparator next;

    @BeforeEach
    public void setup() {
        tTable = new TTableMap();
        moveToZobrist = new MoveToHashMap();
        tailMoveComparator = new TranspositionTailMoveComparator();
        tailMoveComparator.setTTable(tTable);
        tailMoveComparator.setMoveToZobrist(moveToZobrist);
        tailMoveComparator.setNext(next);
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

        when(next.compare(move, move)).thenReturn(0);
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


        when(next.compare(any(Move.class), any(Move.class))).thenReturn(0);
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
}
