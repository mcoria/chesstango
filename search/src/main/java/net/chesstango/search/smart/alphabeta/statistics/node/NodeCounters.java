package net.chesstango.search.smart.alphabeta.statistics.node;

import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.SearchListenerMediator;
import net.chesstango.search.smart.alphabeta.statistics.node.visitors.SetNodeCountersVisitor;

/**
 * @author Mauricio Coria
 */
public class NodeCounters implements SearchByCycleListener, Acceptor {

    private long[] visitedNodesCounters;
    private long[] expectedNodesCounters;
    private long[] visitedNodesCountersQuiescence;
    private long[] expectedNodesCountersQuiescence;

    @Setter
    private SearchListenerMediator searchListenerMediator;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void beforeSearch() {
        this.visitedNodesCounters = new long[30];
        this.expectedNodesCounters = new long[30];
        this.visitedNodesCountersQuiescence = new long[30];
        this.expectedNodesCountersQuiescence = new long[30];

        searchListenerMediator.accept(
                new SetNodeCountersVisitor(
                        visitedNodesCounters, expectedNodesCounters,
                        visitedNodesCountersQuiescence, expectedNodesCountersQuiescence
                )
        );
    }


    public NodeStatistics getRegularNodeStatistics() {
        return new NodeStatistics(expectedNodesCounters, visitedNodesCounters);
    }

    public NodeStatistics getQuiescenceNodeStatistics() {
        return new NodeStatistics(expectedNodesCountersQuiescence, visitedNodesCountersQuiescence);
    }
}
