package net.chesstango.search.smart.statistics.sorter;

import net.chesstango.search.Acceptor;
import net.chesstango.search.SearchListener;
import net.chesstango.search.Visitor;

/**
 * @author Mauricio Coria
 */
public class SorterCounters implements Acceptor, SearchListener {
    private long failHighFirstCounter;
    private long failHighCounter;


    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void beforeSearch() {
        failHighFirstCounter = 0;
        failHighCounter = 0;
    }

    public void increaseFailHighFirstCounter() {
        failHighFirstCounter++;
    }

    public void increaseFailHighCounter() {
        failHighCounter++;
    }

    public SorterStatistics getSorterStatistics() {
        return new SorterStatistics(failHighFirstCounter, failHighCounter);
    }
}
