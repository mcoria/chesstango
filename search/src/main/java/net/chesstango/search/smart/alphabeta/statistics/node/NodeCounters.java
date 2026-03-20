package net.chesstango.search.smart.alphabeta.statistics.node;

import lombok.Setter;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.SearchListenerMediator;
import net.chesstango.search.smart.alphabeta.statistics.node.visitors.SetNodeCountersVisitor;

import java.util.Arrays;

/**
 * @author Mauricio Coria
 */
public class NodeCounters implements SearchByCycleListener {

    private long rootNodeCounter;
    private long interiorNodeCounter;
    private long quiescenceCounter;
    private long leafCounter;
    private long terminalNodeCounter;
    private long loopNodeCounter;
    private long egtbCounter;

    private long[] visitedRNodesCounters;
    private long[] expectedRNodesCounters;
    private long[] visitedQNodesCounters;
    private long[] expectedQNodesCounters;

    @Setter
    private SearchListenerMediator searchListenerMediator;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void beforeSearch() {
        this.rootNodeCounter = 0;
        this.interiorNodeCounter = 0;
        this.quiescenceCounter = 0;
        this.leafCounter = 0;
        this.terminalNodeCounter = 0;
        this.loopNodeCounter = 0;
        this.egtbCounter = 0;

        this.visitedRNodesCounters = new long[NodeStatistics.MAX_DEPTH];
        this.expectedRNodesCounters = new long[NodeStatistics.MAX_DEPTH];

        this.visitedQNodesCounters = new long[NodeStatistics.MAX_DEPTH];
        this.expectedQNodesCounters = new long[NodeStatistics.MAX_DEPTH];

        searchListenerMediator.accept(
                new SetNodeCountersVisitor(this)
        );
    }


    public NodeStatistics getNodeStatistics() {
        assert Arrays.stream(visitedRNodesCounters).sum() <= Arrays.stream(expectedRNodesCounters).sum();
        assert Arrays.stream(visitedQNodesCounters).sum() <= Arrays.stream(expectedQNodesCounters).sum();
        return new NodeStatistics(
                rootNodeCounter,
                interiorNodeCounter,
                quiescenceCounter,
                leafCounter,
                terminalNodeCounter,
                loopNodeCounter,
                egtbCounter,
                visitedRNodesCounters,
                expectedRNodesCounters,
                visitedQNodesCounters,
                expectedQNodesCounters
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

    public void increaseLeafCounter() {
        leafCounter++;
    }

    public void increaseTerminalCounter() {
        terminalNodeCounter++;
    }

    public void increaseLoopCounter() {
        loopNodeCounter++;
    }

    public void increaseEgtbCounter() {
        egtbCounter++;
    }

    public void increaseExpectedRegularCounter(final int level, final int increment) {
        expectedRNodesCounters[level] += increment;
    }

    public void increaseVisitedRegularCounter(final int level) {
        visitedRNodesCounters[level]++;
    }

    public void increaseExpectedQuiescenceCounter(final int level, final int increment) {
        expectedQNodesCounters[level] += increment;
    }

    public void increaseVisitedQuiescenceCounter(final int level) {
        visitedQNodesCounters[level]++;
    }

}
