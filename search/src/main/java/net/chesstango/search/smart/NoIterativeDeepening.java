package net.chesstango.search.smart;

import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.search.SearchMove;
import net.chesstango.search.SearchMoveResult;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

/**
 * @author Mauricio Coria
 */
public class NoIterativeDeepening implements SearchMove {
    private final SearchSmart searchSmart;
    private Map<Long, SearchContext.TableEntry> maxMap;
    private Map<Long, SearchContext.TableEntry> minMap;

    public NoIterativeDeepening(SearchSmart searchSmartAlgorithm) {
        this.searchSmart = searchSmartAlgorithm;
        this.maxMap = new HashMap<>();
        this.minMap = new HashMap<>();
    }

    @Override
    public SearchMoveResult search(Game game, int depth) {
        int[] visitedNodesCounters = new int[30];
        int[] expectedNodesCounters = new int[30];
        int[] visitedNodesQuiescenceCounter = new int[30];
        Set<Move>[] distinctMovesPerLevel = new Set[30];
        IntStream.range(0, 30).forEach(i -> distinctMovesPerLevel[i] = new HashSet<>());

        searchSmart.initSearch(game, depth);

        SearchContext context = new SearchContext(game,
                depth,
                visitedNodesCounters,
                expectedNodesCounters,
                visitedNodesQuiescenceCounter,
                distinctMovesPerLevel,
                maxMap,
                minMap);

        SearchMoveResult result = searchSmart.search(context);

        searchSmart.closeSearch(result);

        return result;
    }

    @Override
    public void stopSearching() {
        this.searchSmart.stopSearching();
    }

    @Override
    public void reset() {
        this.maxMap = new HashMap<>();
        this.minMap = new HashMap<>();
    }

}
