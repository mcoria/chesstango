package net.chesstango.search.alphabeta.statistics.sorter;

import net.chesstango.search.Acceptor;
import net.chesstango.search.SearchListener;
import net.chesstango.search.Visitor;

import static net.chesstango.search.alphabeta.Constants.MAX_DEPTH;

/**
 * @author Mauricio Coria
 */
public class SorterCounters implements Acceptor, SearchListener {
    private final int[] sortingArray = new int[MAX_DEPTH];

    private long failHighCounter;
    private long failHighFirstCounter;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void beforeSearch() {
        failHighFirstCounter = 0;
        failHighCounter = 0;
    }

    public void resetIndex(int currentPly) {
        sortingArray[currentPly] = -1;
    }

    public void increaseIndex(int currentPly) {
        sortingArray[currentPly]++;
    }

    public void increaseFailHighCounter(int currentPly) {
        failHighCounter++;
        if (sortingArray[currentPly] == 0) {
            failHighFirstCounter++;
        }
    }

    public SorterStatistics getSorterStatistics() {
        return new SorterStatistics(failHighFirstCounter, failHighCounter);
    }
}
