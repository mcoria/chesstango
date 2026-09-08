package net.chesstango.search.smart.evalcache.visitors;

import net.chesstango.search.Visitor;
import net.chesstango.search.smart.evalcache.EvaluatorCache;
import net.chesstango.search.smart.evalcache.EvaluatorCacheAdapter;

/**
 *
 * @author Mauricio Coria
 */
public class LinkEvaluatorCacheNodeVisitor implements Visitor {

    private final EvaluatorCache evaluatorCache;

    public LinkEvaluatorCacheNodeVisitor(EvaluatorCache evaluatorCache) {
        this.evaluatorCache = evaluatorCache;
    }

    @Override
    public void visit(EvaluatorCacheAdapter evaluatorCacheAdapter) {
        evaluatorCacheAdapter.setEvaluatorCache(evaluatorCache);
    }

}
