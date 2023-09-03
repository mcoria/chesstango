package net.chesstango.search.smart.sorters;

import net.chesstango.board.Game;
import net.chesstango.board.Piece;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.search.smart.SearchContext;
import net.chesstango.search.smart.transposition.MapTTable;
import net.chesstango.search.smart.transposition.TTable;
import net.chesstango.search.smart.transposition.TranspositionEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Mauricio Coria
 */
public class TranspositionEntryMoveSorterTest {

    private TTable maxMap;
    private TTable minMap;
    private TTable qMaxMap;
    private TTable qMinMap;
    private TranspositionMoveSorter moveSorter;

    @BeforeEach
    public void setup() {
        maxMap = new MapTTable();
        minMap = new MapTTable();
        qMaxMap = new MapTTable();
        qMinMap = new MapTTable();
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


        TranspositionEntry tableEntry = maxMap.allocate(hash);

        updateTableEntry(hash, tableEntry, bestMoveEncoded);


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
        context.setMaxMap(maxMap);
        context.setMinMap(minMap);
        context.setQMaxMap(qMaxMap);
        context.setQMinMap(qMinMap);

        moveSorter.beforeSearchByDepth(context);
    }

    private TranspositionEntry updateTableEntry(long hash, TranspositionEntry entry, short bestMoveEncoded) {
        long bestMoveAndValue = encodedMoveAndValue(bestMoveEncoded, 1);
        entry.hash = hash;
        entry.bestMoveAndValue = bestMoveAndValue;
        //entry.value = (int) (0b00000000_00000000_00000000_00000000_00000000_11111111_11111111_11111111_11111111L & bestMoveAndValue);
        return entry;
    }

    private long encodedMoveAndValue(short move, int value) {
        long encodedMoveLng = ((long) move) << 32;

        long encodedValueLng = 0b00000000_00000000_00000000_00000000_00000000_11111111_11111111_11111111_11111111L & value;

        return encodedValueLng | encodedMoveLng;
    }
}
