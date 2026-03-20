package net.chesstango.search.smart.alphabeta.statistics.node;

import lombok.Setter;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.SearchByDepthListener;

/**
 * @author Mauricio Coria
 */
public class NodeCounters implements SearchByCycleListener, SearchByDepthListener {

    private long rootNodeCounter;
    private long interiorNodeCounter;
    private long quiescenceCounter;
    private long leafCounter;
    private long terminalNodeCounter;
    private long loopNodeCounter;
    private long egtbCounter;

    private long regularNodeCounter;
    private long[] regularNodeCounters;

    private long[] visitedNodesCounters;
    private long[] expectedNodesCounters;

    @Setter
    private int depth;

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

        this.regularNodeCounters = new long[NodeStatistics.MAX_DEPTH];
        this.visitedNodesCounters = new long[NodeStatistics.MAX_DEPTH];
        this.expectedNodesCounters = new long[NodeStatistics.MAX_DEPTH];
    }

    @Override
    public void beforeSearchByDepth() {
        regularNodeCounter = 0;
    }


    @Override
    public void afterSearchByDepth() {
        regularNodeCounters[depth - 1] = regularNodeCounter;
    }


    public NodeStatistics getNodeStatistics() {
        return new NodeStatistics(
                rootNodeCounter,
                interiorNodeCounter,
                quiescenceCounter,
                leafCounter,
                terminalNodeCounter,
                loopNodeCounter,
                egtbCounter,
                regularNodeCounters,
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

    public void increaseRegularCounter() {
        regularNodeCounter++;
    }

    public void increaseExpectedCounter(final int level, final int increment) {
        expectedNodesCounters[level] += increment;
    }

    public void increaseVisitedCounter(final int level) {
        visitedNodesCounters[level]++;
    }
}
