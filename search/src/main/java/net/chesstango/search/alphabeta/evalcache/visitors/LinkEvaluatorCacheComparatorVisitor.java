package net.chesstango.search.alphabeta.evalcache.visitors;

import net.chesstango.search.Visitor;
import net.chesstango.search.alphabeta.evalcache.EvaluatorCache;
import net.chesstango.search.alphabeta.evalcache.comparators.EvaluatorCacheComparator;

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
    public void visit(EvaluatorCacheComparator evaluatorCacheComparator) {
        evaluatorCacheComparator.setEvaluatorCache(evaluatorCache);
    }

}
