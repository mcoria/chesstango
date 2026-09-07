package net.chesstango.search.smart.evaluator.visitors;

import net.chesstango.search.smart.evaluator.EvaluatorCache;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.evaluator.EvaluatorCacheAdapter;
import net.chesstango.search.smart.evaluator.comparators.GameEvaluatorCacheComparator;

/**
 *
 * @author Mauricio Coria
 */
public class LinkEvaluatorCacheVisitor implements Visitor {

    private final EvaluatorCache evaluatorCache;

    public LinkEvaluatorCacheVisitor(EvaluatorCache evaluatorCache) {
        this.evaluatorCache = evaluatorCache;
    }

    @Override
    public void visit(GameEvaluatorCacheComparator gameEvaluatorCacheComparator) {
        gameEvaluatorCacheComparator.setEvaluatorCache(evaluatorCache);
    }

    @Override
    public void visit(EvaluatorCacheAdapter evaluatorCacheAdapter) {
        evaluatorCacheAdapter.setEvaluatorCache(evaluatorCache);
    }

}
