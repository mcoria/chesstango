package net.chesstango.search.smart.evaluator.visitors;

import net.chesstango.search.Visitor;
import net.chesstango.search.smart.evaluator.EvaluatorCache;
import net.chesstango.search.smart.evaluator.comparators.GameEvaluatorCacheComparator;

/**
 *
 * @author Mauricio Coria
 */
public class LinkEvaluatorCacheComparatorVisitor implements Visitor {

    private final EvaluatorCache evaluatorCache;

    public LinkEvaluatorCacheComparatorVisitor(EvaluatorCache evaluatorCache) {
        this.evaluatorCache = evaluatorCache;
    }

    @Override
    public void visit(GameEvaluatorCacheComparator gameEvaluatorCacheComparator) {
        gameEvaluatorCacheComparator.setEvaluatorCache(evaluatorCache);
    }

}
