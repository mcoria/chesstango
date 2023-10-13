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

import java.util.Iterator;
import java.util.List;

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

        Move bestMove = null;
        Move secondBestMove = null;
        for (Move move : game.getPossibleMoves()) {
            if (Square.c2.equals(move.getFrom().getSquare()) && Square.c3.equals(move.getTo().getSquare())) {
                bestMove = move;
            } else if (Square.g1.equals(move.getFrom().getSquare()) && Square.f3.equals(move.getTo().getSquare())) {
                secondBestMove = move;
            }
        }

        long hash = game.getChessPosition().getZobristHash();

        TranspositionEntry tableEntry = maxMap.allocate(hash);

        updateTableEntry(hash, tableEntry, bestMove, secondBestMove);

        initMoveSorter(game);

        Move move;
        List<Move> movesSorted = moveSorter.getSortedMoves();
        Iterator<Move> movesSortedIt = movesSorted.iterator();

        move = movesSortedIt.next();
        assertEquals(Piece.PAWN_WHITE, move.getFrom().getPiece());
        assertEquals(Square.c2, move.getFrom().getSquare());
        assertEquals(Square.c3, move.getTo().getSquare());

        move = movesSortedIt.next();
        assertEquals(Piece.KNIGHT_WHITE, move.getFrom().getPiece());
        assertEquals(Square.g1, move.getFrom().getSquare());
        assertEquals(Square.f3, move.getTo().getSquare());
    }

    private void initMoveSorter(Game game) {
        moveSorter.beforeSearch(game);

        SearchContext context = new SearchContext(1);
        context.setMaxMap(maxMap);
        context.setMinMap(minMap);
        context.setQMaxMap(qMaxMap);
        context.setQMinMap(qMinMap);

        moveSorter.beforeSearchByDepth(context);
    }

    private void updateTableEntry(long hash, TranspositionEntry entry, Move bestMove, Move secondBestMove) {
        long bestMoveAndValue = TranspositionEntry.encode(bestMove, secondBestMove, 1);
        entry.hash = hash;
        entry.movesAndValue = bestMoveAndValue;
    }

}
