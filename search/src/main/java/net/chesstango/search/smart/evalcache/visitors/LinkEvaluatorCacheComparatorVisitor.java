package net.chesstango.search.smart.evalcache.visitors;

import net.chesstango.search.Visitor;
import net.chesstango.search.smart.evalcache.EvaluatorCache;
import net.chesstango.search.smart.evalcache.comparators.GameEvaluatorCacheComparator;

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
