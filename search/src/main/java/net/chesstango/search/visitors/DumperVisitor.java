package net.chesstango.search.visitors;

import net.chesstango.search.IterativeDeepening;
import net.chesstango.search.ListenerMediator;
import net.chesstango.search.NoIterativeDeepening;
import net.chesstango.search.Visitor;
import net.chesstango.search.alphabeta.transposition.listeners.TTDump;

/**
 * Esta clase recorre ella misma toda la estructura
 *
 * @author Mauricio Coria
 */
public class DumperVisitor implements Visitor {

    @Override
    public void visit(NoIterativeDeepening noIterativeDeepening) {
        ListenerMediator listenerMediator = noIterativeDeepening.getListenerMediator();
        listenerMediator.accept(this);
    }

    @Override
    public void visit(IterativeDeepening iterativeDeepening) {
        ListenerMediator listenerMediator = iterativeDeepening.getListenerMediator();
        listenerMediator.accept(this);
    }

    @Override
    public void visit(TTDump ttDump) {
        ttDump.dumpTable("dump.bin");
    }

}
