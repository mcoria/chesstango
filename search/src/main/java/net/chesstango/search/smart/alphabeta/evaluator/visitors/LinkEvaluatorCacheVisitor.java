package net.chesstango.search.smart.alphabeta.evaluator.visitors;

import net.chesstango.evaluation.EvaluatorCacheRead;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.alphabeta.evaluator.comparators.GameEvaluatorCacheComparator;

/**
 *
 * @author Mauricio Coria
 */
public class LinkEvaluatorCacheVisitor implements Visitor {

    private final EvaluatorCacheRead evaluatorCache;

    public LinkEvaluatorCacheVisitor(EvaluatorCacheRead evaluatorCache) {
        this.evaluatorCache = evaluatorCache;
    }

    @Override
    public void visit(GameEvaluatorCacheComparator gameEvaluatorCacheComparator) {
        gameEvaluatorCacheComparator.setEvaluatorCacheRead(evaluatorCache);
    }

}
