package net.chesstango.search.smart.alphabeta.transposition.visitors;

import net.chesstango.search.Visitor;
import net.chesstango.search.smart.IterativeDeepening;
import net.chesstango.search.smart.NoIterativeDeepening;
import net.chesstango.search.smart.SearchListenerMediator;
import net.chesstango.search.smart.alphabeta.transposition.TTableArrayPrimitives;

/**
 *
 * @author Mauricio Coria
 */
public class SetTTableHashSizeVisitor implements Visitor {
    private final int hashSize;

    public SetTTableHashSizeVisitor(int hashSize) {
        this.hashSize = hashSize;
    }

    @Override
    public void visit(IterativeDeepening iterativeDeepening) {
        SearchListenerMediator searchListenerMediator = iterativeDeepening.getSearchListenerMediator();
        searchListenerMediator.accept(this);
    }

    @Override
    public void visit(NoIterativeDeepening noIterativeDeepening) {
        SearchListenerMediator searchListenerMediator = noIterativeDeepening.getSearchListenerMediator();
        searchListenerMediator.accept(this);
    }

    @Override
    public void visit(TTableArrayPrimitives ttArrayPrimitives) {
        ttArrayPrimitives.setupHashTable(hashSize);
    }

}
