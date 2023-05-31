package net.chesstango.search.smart.alphabeta.listeners;

import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.SearchContext;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

/**
 * @author Mauricio Coria
 */
public class SearchSetup implements net.chesstango.search.smart.SearchLifeCycle {

    private int[] visitedNodesCounters;
    private int[] expectedNodesCounters;
    private int[] visitedNodesQuiescenceCounter;

    private Set<Move>[] distinctMovesPerLevel;

    private Map<Long, SearchContext.TableEntry> maxMap;

    private Map<Long, SearchContext.TableEntry> minMap;

    @Override
    public void initSearch(Game game, int maxDepth) {
        this.visitedNodesCounters = new int[30];
        this.visitedNodesQuiescenceCounter = new int[30];
        this.expectedNodesCounters = new int[30];
        this.distinctMovesPerLevel = new Set[30];
        IntStream.range(0, 30).forEach(i -> this.distinctMovesPerLevel[i] = new HashSet<>());
        this.maxMap = new HashMap<>();
        this.minMap = new HashMap<>();
    }

    @Override
    public void closeSearch(SearchMoveResult result) {
        this.visitedNodesCounters = null;
        this.visitedNodesQuiescenceCounter = null;
        this.expectedNodesCounters = null;
        this.distinctMovesPerLevel = null;
        this.maxMap = null;
        this.minMap = null;
    }

    @Override
    public void init(SearchContext context) {
        context.setVisitedNodesCounters(visitedNodesCounters);
        context.setVisitedNodesQuiescenceCounter(visitedNodesQuiescenceCounter);
        context.setExpectedNodesCounters(expectedNodesCounters);
        context.setDistinctMovesPerLevel(distinctMovesPerLevel);
        context.setMaxMap(maxMap);
        context.setMinMap(minMap);
    }

    @Override
    public void close(SearchMoveResult result) {

    }

    @Override
    public void stopSearching() {

    }

    @Override
    public void reset() {
    }
}
