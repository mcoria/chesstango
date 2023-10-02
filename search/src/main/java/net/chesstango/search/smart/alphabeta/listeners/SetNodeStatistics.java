package net.chesstango.search.smart.alphabeta.listeners;

import net.chesstango.board.Game;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.SearchContext;
import net.chesstango.search.smart.SearchLifeCycle;
import net.chesstango.search.smart.statistics.NodeStatistics;

/**
 * @author Mauricio Coria
 */
public class SetNodeStatistics implements SearchLifeCycle {

    private int[] visitedNodesCounters;
    private int[] expectedNodesCounters;
    private int[] visitedNodesCountersQuiescence;
    private int[] expectedNodesCountersQuiescence;

    @Override
    public void beforeSearch(Game game, int maxDepth) {
        this.visitedNodesCounters = new int[30];
        this.expectedNodesCounters = new int[30];
        this.visitedNodesCountersQuiescence = new int[30];
        this.expectedNodesCountersQuiescence = new int[30];
    }

    @Override
    public void beforeSearchByDepth(SearchContext context) {
        context.setVisitedNodesCounters(visitedNodesCounters);
        context.setExpectedNodesCounters(expectedNodesCounters);

        context.setVisitedNodesCountersQuiescence(visitedNodesCountersQuiescence);
        context.setExpectedNodesCountersQuiescence(expectedNodesCountersQuiescence);
    }

    @Override
    public void afterSearchByDepth(SearchMoveResult result) {

    }

    @Override
    public void afterSearch(SearchMoveResult result) {
        if (result != null) {
            result.setRegularNodeStatistics(new NodeStatistics(expectedNodesCounters, visitedNodesCounters));
            result.setQuiescenceNodeStatistics(new NodeStatistics(expectedNodesCountersQuiescence, visitedNodesCountersQuiescence));
        }
        this.visitedNodesCounters = null;
        this.expectedNodesCounters =  null;
        this.visitedNodesCountersQuiescence = null;
        this.expectedNodesCountersQuiescence = null;
    }

    @Override
    public void stopSearching() {
    }

    @Override
    public void reset() {
    }
}
