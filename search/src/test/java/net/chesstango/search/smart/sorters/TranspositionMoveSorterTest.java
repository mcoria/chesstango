package net.chesstango.search.smart.sorters;

import net.chesstango.board.Game;
import net.chesstango.board.Piece;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.search.smart.SearchContext;
import net.chesstango.search.smart.transposition.ArrayTTable;
import net.chesstango.search.smart.transposition.TTable;
import net.chesstango.search.smart.transposition.Transposition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Mauricio Coria
 */
public class TranspositionMoveSorterTest {

    private TTable<Transposition> tTable;
    private TranspositionMoveSorter moveSorter;

    @BeforeEach
    public void setup() {
        tTable = new ArrayTTable<>(Transposition.class);
        moveSorter = new TranspositionMoveSorter();
    }

    @Test
    public void testInitial() {
        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);

        short bestMoveEncoded = 0;
        for (Move move : game.getPossibleMoves()) {
            if (Square.c2.equals(move.getFrom().getSquare()) && Square.c3.equals(move.getTo().getSquare())) {
                bestMoveEncoded = move.binaryEncoding();
                break;
            }
        }

        long hash = game.getChessPosition().getZobristHash();

        tTable.write(createTableEntry(hash, bestMoveEncoded));

        initMoveSorter(game);

        Move move;
        List<Move> movesSorted = moveSorter.getSortedMoves();
        Iterator<Move> movesSortedIt = movesSorted.iterator();

        move = movesSortedIt.next();
        assertEquals(Piece.PAWN_WHITE, move.getFrom().getPiece());
        assertEquals(Square.c2, move.getFrom().getSquare());
        assertEquals(Square.c3, move.getTo().getSquare());
    }


    private void initMoveSorter(Game game) {
        moveSorter.beforeSearch(game, 1);

        SearchContext context = new SearchContext(1);
        context.setTTable(tTable);

        moveSorter.beforeSearchByDepth(context);
    }

    private Transposition createTableEntry(long hash, short bestMoveEncoded) {
        Transposition entry = new Transposition(hash);
        entry.setBestMoveAndValue(encodedMoveAndValue(bestMoveEncoded, 1));
        return entry;
    }

    private long encodedMoveAndValue(short move, int value) {
        long encodedMoveLng = ((long) move) << 32;

        long encodedValueLng = 0b00000000_00000000_00000000_00000000_00000000_11111111_11111111_11111111_11111111L & value;

        return encodedValueLng | encodedMoveLng;
    }
}
