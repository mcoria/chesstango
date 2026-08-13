package net.chesstango.search.smart.alphabeta.debug.visitors;

import net.chesstango.search.Visitor;
import net.chesstango.search.smart.alphabeta.debug.DebugNodeTrap;
import net.chesstango.search.smart.alphabeta.debug.filters.DebugFilter;

/**
 *
 * @author Mauricio Coria
 */
public class LinkDebugNodeTrapVisitor implements Visitor {
    private final DebugNodeTrap debugNodeTrap;

    public LinkDebugNodeTrapVisitor(DebugNodeTrap debugNodeTrap) {
        this.debugNodeTrap = debugNodeTrap;
    }

    @Override
    public void visit(DebugFilter debugFilter) {
        debugFilter.setDebugNodeTrap(debugNodeTrap);
    }
}
