package net.chesstango.search.smart.alphabeta.transposition.comparators;

import net.chesstango.board.Game;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.containers.MoveToHashMap;
import net.chesstango.gardel.fen.FEN;
import net.chesstango.search.smart.alphabeta.transposition.TTable;
import net.chesstango.search.smart.alphabeta.transposition.TTableMap;
import net.chesstango.search.smart.alphabeta.transposition.TranspositionEntry;
import net.chesstango.search.smart.sorters.MoveComparator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static net.chesstango.search.Bound.EXACT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
                assertTrue(tailMoveComparator.compare(move, otherMove) == 0);
                assertTrue(tailMoveComparator.compare(otherMove, move) == 0);
            } else {
                assertTrue(tailMoveComparator.compare(move, otherMove) > 0);
                assertTrue(tailMoveComparator.compare(otherMove, move) < 0);
            }
        });

        // El hash de la posicion luego de ejecutar el movimiento debe estar guardado en el hashmap
        assertEquals(hash, moveToZobrist.read(move));
    }
}
