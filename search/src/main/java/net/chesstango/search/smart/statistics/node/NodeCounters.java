package net.chesstango.search.smart.statistics.node;

import lombok.Setter;
import net.chesstango.search.Acceptor;
import net.chesstango.search.SearchListener;
import net.chesstango.search.Visitor;

import static net.chesstango.search.smart.Constants.MAX_DEPTH;

/**
 * @author Mauricio Coria
 */
public class NodeCounters implements Acceptor, SearchListener {

    private long rootNodeCounter;
    private long interiorNodeCounter;
    private long quiescenceCounter;
    private long leafCounter;
    private long terminalNodeCounter;
    private long loopNodeCounter;
    private long egtbCounter;

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

        this.visitedNodesCounters = new long[MAX_DEPTH];
        this.expectedNodesCounters = new long[MAX_DEPTH];
    }


    public NodeStatistics getNodeStatistics() {
        assert rootNodeCounter + interiorNodeCounter + leafCounter + terminalNodeCounter + loopNodeCounter + egtbCounter > 0;
        return new NodeStatistics(
                rootNodeCounter,
                interiorNodeCounter,
                quiescenceCounter,
                leafCounter,
                terminalNodeCounter,
                loopNodeCounter,
                egtbCounter,
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

    public void increaseExpectedCounter(final int level, final int increment) {
        expectedNodesCounters[level] += increment;
    }

    public void increaseVisitedCounter(final int level) {
        visitedNodesCounters[level]++;
    }
}
