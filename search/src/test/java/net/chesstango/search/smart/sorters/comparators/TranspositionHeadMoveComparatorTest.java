package net.chesstango.search.smart.sorters.comparators;

import net.chesstango.board.Game;
import net.chesstango.board.Piece;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.containers.MoveContainerReader;
import net.chesstango.board.moves.containers.MoveToHashMap;
import net.chesstango.gardel.fen.FEN;
import net.chesstango.gardel.fen.FENParser;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.features.transposition.TTableMap;
import net.chesstango.search.smart.features.transposition.TTable;
import net.chesstango.search.smart.features.transposition.TranspositionBound;
import net.chesstango.search.smart.features.transposition.TranspositionEntry;
import net.chesstango.search.smart.features.transposition.comparators.TranspositionHeadMoveComparator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

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
        maxMap = new TTableMap();
        minMap = new TTableMap();
        headMoveComparator = new TranspositionHeadMoveComparator(SearchByCycleContext::getMaxMap, SearchByCycleContext::getMinMap);
        headMoveComparator.setNext(new DefaultMoveComparator());
    }

    @Test
    public void testInitial() {
        Game game = Game.from(FEN.of(FENParser.INITIAL_FEN));

        Move bestMove = null;
        for (Move move : game.getPossibleMoves()) {
            if (Square.c2.equals(move.getFrom().square()) && Square.c3.equals(move.getTo().square())) {
                bestMove = move;
            }
        }

        long hash = game.getPosition().getZobristHash();

        long bestMoveAndValue = TranspositionEntry.encode(bestMove, 1);

        maxMap.write(hash, 1, bestMoveAndValue, TranspositionBound.EXACT);

        initMoveSorter(game);

        List<Move> movesSorted = getSortedMoves(game);
        Iterator<Move> movesSortedIt = movesSorted.iterator();

        Move move = movesSortedIt.next();
        assertEquals(Piece.PAWN_WHITE, move.getFrom().piece());
        assertEquals(Square.c2, move.getFrom().square());
        assertEquals(Square.c3, move.getTo().square());
    }

    private List<Move> getSortedMoves(Game game) {
        List<Move> movesList = new LinkedList<>();

        MoveContainerReader<? extends Move> moves = game.getPossibleMoves();
        moves.forEach(movesList::add);

        MoveToHashMap moveToZobrist = new MoveToHashMap();
        headMoveComparator.beforeSort(0, moveToZobrist);
        movesList.sort(headMoveComparator.reversed());
        headMoveComparator.afterSort(0, moveToZobrist);

        return movesList;
    }

    private void initMoveSorter(Game game) {
        SearchByCycleContext searchByCycleContext = new SearchByCycleContext();
        searchByCycleContext.setMaxMap(maxMap);
        searchByCycleContext.setMinMap(minMap);

        headMoveComparator.setGame(game);
        headMoveComparator.beforeSearch(searchByCycleContext);
    }

}
