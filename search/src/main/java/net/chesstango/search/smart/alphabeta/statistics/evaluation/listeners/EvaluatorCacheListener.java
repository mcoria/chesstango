package net.chesstango.search.smart.alphabeta.statistics.evaluation.listeners;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.evaluation.EvaluatorCache;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.ResetListener;
import net.chesstango.search.smart.SearchByCycleListener;

/**
 * @author Mauricio Coria
 */
@Setter
public class EvaluatorCacheListener implements SearchByCycleListener, ResetListener {
    @Setter
    @Getter
    @Accessors(chain = true)
    private EvaluatorCache gameEvaluatorCache;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void beforeSearch() {
        gameEvaluatorCache.increaseAge();
    }

    @Override
    public void reset() {
        gameEvaluatorCache.clear();
    }
}
