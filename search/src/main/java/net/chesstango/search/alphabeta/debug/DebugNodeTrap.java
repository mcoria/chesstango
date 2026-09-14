package net.chesstango.search.alphabeta.debug;

import net.chesstango.search.alphabeta.debug.model.DebugNode;

/**
 * @author Mauricio Coria
 */
public interface DebugNodeTrap {
    boolean test(DebugNode debugNode);

    void debugAction(DebugNode debugNode);
}
