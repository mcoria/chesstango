package net.chesstango.search.smart.alphabeta.debug;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.move.SimpleMoveEncoder;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.sorters.MoveSorter;

/**
 * @author Mauricio Coria
 */
public class DebugSorter implements MoveSorter, SearchByCycleListener {
    private final SimpleMoveEncoder simpleMoveEncoder = new SimpleMoveEncoder();

    @Setter
    @Getter
    private MoveSorter moveSorterImp;

    private SearchTracker searchTracker;

    @Override
    public Iterable<Move> getOrderedMoves() {

        searchTracker.sortingON();

        Iterable<Move> sortedMoves = moveSorterImp.getOrderedMoves();

        String sortedMovesStr = getMoveListAsString(sortedMoves);

        searchTracker.trackSortedMoves(sortedMovesStr);

        searchTracker.sortingOFF();

        return sortedMoves;
    }

    @Override
    public void beforeSearch(SearchByCycleContext context) {
        searchTracker = context.getSearchTracker();
    }

    @Override
    public void afterSearch() {

    }


    private String getMoveListAsString(Iterable<Move> moves) {
        StringBuilder sb = new StringBuilder();
        for (Move move : moves) {
            sb.append(simpleMoveEncoder.encode(move));
            sb.append(" ");
        }
        return sb.toString();
    }
}
