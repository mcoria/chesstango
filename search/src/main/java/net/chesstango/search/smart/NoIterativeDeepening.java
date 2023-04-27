package net.chesstango.search.smart;

import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.search.SearchMove;
import net.chesstango.search.SearchMoveResult;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

/**
 * @author Mauricio Coria
 */
public class NoIterativeDeepening implements SearchMove {
    private final AbstractSmart searchMove;

    public NoIterativeDeepening(AbstractSmart searchMove) {
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
        Set<Move>[] distinctMovesPerLevel = new Set[30];
        IntStream.range(0, 30).forEach(i -> distinctMovesPerLevel[i] = new HashSet<>() );

        SearchContext context = new SearchContext(depth, visitedNodesCounters, expectedNodesCounters, distinctMovesPerLevel);

        return searchMove.searchBestMove(game, context);
    }

    @Override
    public void stopSearching() {
        searchMove.stopSearching();
    }
}
