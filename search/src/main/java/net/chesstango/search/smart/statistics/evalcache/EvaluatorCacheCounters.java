package net.chesstango.search.smart.statistics.evalcache;

import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.search.Acceptor;
import net.chesstango.search.SearchListener;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.evalcache.EvaluatorCacheArray;

/**
 *
 * @author Mauricio Coria
 */
public class EvaluatorCacheCounters implements Acceptor, SearchListener {
    private long readNodes;
    private long readNodeHits;

    private long readComparators;
    private long readComparatorHits;

    @Setter
    @Accessors(chain = true)
    private EvaluatorCacheArray evaluatorCacheArray;


    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void beforeSearch() {
        readNodes = 0;
        readNodeHits = 0;
        readComparators = 0;
        readComparatorHits = 0;
    }

    public void increaseReadNodes() {
        readNodes++;
    }

    public void increaseReadNodesHits() {
        readNodeHits++;
    }

    public void increaseReadComparators() {
        readComparators++;
    }

    public void increaseReadComparatorHits() {
        readComparatorHits++;
    }

    public EvaluatorCacheStatistics getEvaluatorCacheStatistics() {
        int fillPercentage = evaluatorCacheArray.getFillPercentage();
        return new EvaluatorCacheStatistics(readNodes, readNodeHits, readComparators, readComparatorHits, fillPercentage);
    }

}
