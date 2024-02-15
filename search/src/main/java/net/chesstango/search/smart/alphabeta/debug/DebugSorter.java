package net.chesstango.search.smart.alphabeta.debug;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.move.SimpleMoveEncoder;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.sorters.MoveSorter;
import net.chesstango.search.smart.transposition.TranspositionEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Mauricio Coria
 */
public class DebugSorter implements MoveSorter, SearchByCycleListener {
    private final SimpleMoveEncoder simpleMoveEncoder = new SimpleMoveEncoder();

    @Setter
    @Getter
    private MoveSorter moveSorterImp;

    private SearchTracker searchTracker;
    private Game game;

    @Override
    public Iterable<Move> getOrderedMoves() {

        searchTracker.sortingON();

        Iterable<Move> sortedMoves = moveSorterImp.getOrderedMoves();

        searchTracker.trackSortedMoves(convertMoveListToStringList(sortedMoves));

        trackComparatorsReads(sortedMoves);

        searchTracker.sortingOFF();

        return sortedMoves;
    }

    @Override
    public void beforeSearch(SearchByCycleContext context) {
        game = context.getGame();
        searchTracker = context.getSearchTracker();
    }


    private List<String> convertMoveListToStringList(Iterable<Move> moves) {
        List<String> sortedMovesStr = new ArrayList<>();
        for (Move move : moves) {
            sortedMovesStr.add(simpleMoveEncoder.encode(move));
        }
        return sortedMovesStr;
    }

    public void trackComparatorsReads(Iterable<Move> moves) {
        Map<Long, DebugNodeTT> sorterReads = searchTracker.getSorterReads();

        final long positionHash = game.getChessPosition().getZobristHash();
        final DebugNodeTT positionEntry = sorterReads.get(positionHash);
        short bestMoveEncoded = 0;
        if (positionEntry != null) {
            bestMoveEncoded = TranspositionEntry.decodeBestMove(positionEntry.getMovesAndValue());
        }

        for (Move move : moves) {
            final long zobristHashMove = game.getChessPosition().getZobristHash(move);

            if (bestMoveEncoded == move.binaryEncoding() && positionEntry != null) {
                positionEntry.setMove(simpleMoveEncoder.encode(move));
            } else {
                DebugNodeTT moveEntry = sorterReads.get(zobristHashMove);
                if (moveEntry != null) {
                    moveEntry.setMove(simpleMoveEncoder.encode(move));
                }
            }
        }
    }
}
