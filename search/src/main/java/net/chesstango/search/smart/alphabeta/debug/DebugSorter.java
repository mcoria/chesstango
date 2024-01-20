package net.chesstango.search.smart.alphabeta.debug;

import lombok.Setter;
import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.move.SimpleMoveEncoder;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.sorters.MoveSorter;

import java.util.List;

/**
 * @author Mauricio Coria
 */
public class DebugSorter implements MoveSorter, SearchByCycleListener {
    private final SimpleMoveEncoder simpleMoveEncoder = new SimpleMoveEncoder();

    @Setter
    private MoveSorter moveSorterImp;

    private SearchTracker searchTracker;

    @Override
    public List<Move> getSortedMoves() {

        searchTracker.sortingON();

        List<Move> sortedMoves = moveSorterImp.getSortedMoves();

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


    private String getMoveListAsString(List<Move> moves) {
        StringBuilder sb = new StringBuilder();
        for (Move move : moves) {
            sb.append(simpleMoveEncoder.encode(move));
            sb.append(" ");
        }
        return sb.toString();
    }
}
