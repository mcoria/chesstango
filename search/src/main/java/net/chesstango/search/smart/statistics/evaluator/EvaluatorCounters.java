package net.chesstango.search.smart.statistics.evaluator;

import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.search.Acceptor;
import net.chesstango.search.SearchListener;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.evalcache.EvaluatorCacheArray;

import java.util.Set;

/**
 *
 * @author Mauricio Coria
 */
public class EvaluatorCounters implements Acceptor, SearchListener {
    private long evaluationsCounter;

    @Setter
    @Accessors(chain = true)
    private Set<EvaluatorEntry> evaluations;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void beforeSearch() {
        evaluationsCounter = 0;
    }

    public void increaseEvaluationsCounter() {
        evaluationsCounter++;
    }


    public EvaluatorStatistics getEvaluationStatistics() {
        return new EvaluatorStatistics(evaluationsCounter, evaluations);
    }

}
