package net.chesstango.search.smart.transposition.visitors;

import net.chesstango.search.Visitor;
import net.chesstango.search.IterativeDeepening;
import net.chesstango.search.NoIterativeDeepening;
import net.chesstango.search.ListenerMediator;
import net.chesstango.search.smart.transposition.TTableArrayPrimitives;

/**
 *
 * @author Mauricio Coria
 */
public class SetTTableHashSizeVisitor implements Visitor {
    /**
     * The hash size in KB
     */
    private final int hashSize;

    public SetTTableHashSizeVisitor(int hashSize) {
        this.hashSize = hashSize;
    }

    @Override
    public void visit(IterativeDeepening iterativeDeepening) {
        ListenerMediator listenerMediator = iterativeDeepening.getListenerMediator();
        listenerMediator.accept(this);
    }

    @Override
    public void visit(NoIterativeDeepening noIterativeDeepening) {
        ListenerMediator listenerMediator = noIterativeDeepening.getListenerMediator();
        listenerMediator.accept(this);
    }

    @Override
    public void visit(TTableArrayPrimitives ttArrayPrimitives) {
        ttArrayPrimitives.setupHashTable(hashSize);
    }

}
