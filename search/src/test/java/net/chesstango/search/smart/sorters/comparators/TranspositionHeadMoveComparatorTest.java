package net.chesstango.search.smart.sorters.comparators;

import net.chesstango.board.Game;
import net.chesstango.board.Piece;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MoveContainerReader;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.transposition.MapTTable;
import net.chesstango.search.smart.transposition.TTable;
import net.chesstango.search.smart.transposition.TranspositionBound;
import net.chesstango.search.smart.transposition.TranspositionEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Mauricio Coria
 */
public class TranspositionHeadMoveComparatorTest {

    private TTable maxMap;
    private TTable minMap;
    private TranspositionHeadMoveComparator headMoveComparator;

    @BeforeEach
    public void setup() {
        maxMap = new MapTTable();
        minMap = new MapTTable();
        headMoveComparator = new TranspositionHeadMoveComparator(SearchByCycleContext::getMaxMap, SearchByCycleContext::getMinMap);
    }

    @Test
    public void testInitial() {
        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);

        Move bestMove = null;
        for (Move move : game.getPossibleMoves()) {
            if (Square.c2.equals(move.getFrom().getSquare()) && Square.c3.equals(move.getTo().getSquare())) {
                bestMove = move;
            }
        }

        long hash = game.getChessPosition().getZobristHash();

        long bestMoveAndValue = TranspositionEntry.encode(bestMove, 1);

        maxMap.write(hash, 1, bestMoveAndValue, TranspositionBound.EXACT);

        initMoveSorter(game);

        List<Move> movesSorted = getSortedMoves(game);
        Iterator<Move> movesSortedIt = movesSorted.iterator();

        Move move = movesSortedIt.next();
        assertEquals(Piece.PAWN_WHITE, move.getFrom().getPiece());
        assertEquals(Square.c2, move.getFrom().getSquare());
        assertEquals(Square.c3, move.getTo().getSquare());
    }

    private List<Move> getSortedMoves(Game game) {
        List<Move> movesList = new LinkedList<>();

        MoveContainerReader moves = game.getPossibleMoves();
        moves.forEach(movesList::add);

        movesList.sort(headMoveComparator.reversed());

        return movesList;
    }

    private void initMoveSorter(Game game) {
        SearchByCycleContext searchByCycleContext = new SearchByCycleContext(game);
        searchByCycleContext.setMaxMap(maxMap);
        searchByCycleContext.setMinMap(minMap);

        headMoveComparator.beforeSearch(searchByCycleContext);
    }

}
