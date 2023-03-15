package net.chesstango.search.smart;

import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.search.SearchMove;
import net.chesstango.search.SearchMoveResult;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AlgoWrapper implements SearchMove {
    private final AbstractSmart searchMove;


    public AlgoWrapper(AbstractSmart searchMove) {
        this.searchMove = searchMove;
    }

    @Override
    public SearchMoveResult searchBestMove(Game game) {
        return searchBestMove(game, 10);
    }

    @Override
    public SearchMoveResult searchBestMove(Game game, int depth) {

        int[] visitedNodesCounters = new int[30];
        int[] expectedNodesCounters = new int[30];
        List<Set<Move>> distinctMovesPerLevel = new ArrayList<>(visitedNodesCounters.length);
        for (int i = 0; i < 30; i++) {
            distinctMovesPerLevel.add(new HashSet<>());
        }

        SearchContext context = new SearchContext(depth)
                .setVisitedNodesCounters(visitedNodesCounters)
                .setDistinctMovesPerLevel(distinctMovesPerLevel)
                .setExpectedNodesCounters(expectedNodesCounters);

        return searchMove.searchBestMove(game, context);
    }

    @Override
    public void stopSearching() {
        searchMove.stopSearching();
    }
}
