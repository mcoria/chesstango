package net.chesstango.search.smart.alphabeta.statistics.node;

import lombok.Setter;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.SearchListenerMediator;
import net.chesstango.search.smart.alphabeta.statistics.node.visitors.SetNodeCountersVisitor;

/**
 * @author Mauricio Coria
 */
public class NodeCounters implements SearchByCycleListener {

    private long rootNodeCounter;
    private long interiorNodeCounter;
    private long quiescenceCounter;
    private long terminalNodeCounter;

    private long[] visitedNodesCounters;
    private long[] expectedNodesCounters;

    @Setter
    private SearchListenerMediator searchListenerMediator;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void beforeSearch() {
        this.rootNodeCounter = 0;
        this.terminalNodeCounter = 0;
        this.visitedNodesCounters = new long[30];
        this.expectedNodesCounters = new long[30];

        searchListenerMediator.accept(
                new SetNodeCountersVisitor(this)
        );
    }


    public NodeStatistics getRegularNodeStatistics() {
        return new NodeStatistics(
                rootNodeCounter,
                interiorNodeCounter,
                quiescenceCounter,
                terminalNodeCounter,
                expectedNodesCounters,
                visitedNodesCounters
        );
    }

    public void increaseRootCounter() {
        rootNodeCounter++;
    }

    public void increaseInteriorCounter() {
        interiorNodeCounter++;
    }

    public void increaseQuiescenceCounter() {
        quiescenceCounter++;
    }

    public void increaseTerminalCounter() {
        terminalNodeCounter++;
    }

    public void increaseExpectedCounter(final int level, final int increment) {
        expectedNodesCounters[level] += increment;
    }

    public void increaseVisitedCounter(final int level) {
        visitedNodesCounters[level]++;
    }
}
