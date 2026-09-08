package net.chesstango.search.smart.evalcache.listeners;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.search.smart.evalcache.EvaluatorCacheArray;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Visitor;
import net.chesstango.search.ResetListener;
import net.chesstango.search.SearchListener;

/**
 * @author Mauricio Coria
 */
@Setter
public class EvaluatorCacheListener implements Acceptor, SearchListener, ResetListener {
    @Setter
    @Getter
    @Accessors(chain = true)
    private EvaluatorCacheArray gameEvaluatorCacheArray;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void beforeSearch() {
        gameEvaluatorCacheArray.increaseAge();
    }

    @Override
    public void reset() {
        gameEvaluatorCacheArray.clear();
    }
}
