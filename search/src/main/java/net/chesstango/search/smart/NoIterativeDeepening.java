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

    public NoIterativeDeepening(SearchSmart searchSmartAlgorithm) {
        searchSmart = searchSmartAlgorithm;
    }

    @Override
    public SearchMoveResult search(Game game, int depth) {
        int[] visitedNodesCounters = new int[30];
        int[] expectedNodesCounters = new int[30];
        int[] visitedNodesQuiescenceCounter = new int[30];
        Set<Move>[] distinctMovesPerLevel = new Set[30];
        IntStream.range(0, 30).forEach(i -> distinctMovesPerLevel[i] = new HashSet<>());
        Map<Long, SearchContext.TableEntry> maxMap = new HashMap<>();
        Map<Long, SearchContext.TableEntry> minMap = new HashMap<>();
        Map<Long, SearchContext.TableEntry> qMaxMap = new HashMap<>();
        Map<Long, SearchContext.TableEntry> qMinMap = new HashMap<>();

        SearchContext context = new SearchContext(game,
                depth,
                visitedNodesCounters,
                expectedNodesCounters,
                visitedNodesQuiescenceCounter,
                distinctMovesPerLevel,
                maxMap,
                minMap,
                qMaxMap,
                qMinMap);

        return searchSmart.search(context);
    }

    @Override
    public void stopSearching() {
        searchSmart.stopSearching();
    }

    @Override
    public void reset() {
    }

}
