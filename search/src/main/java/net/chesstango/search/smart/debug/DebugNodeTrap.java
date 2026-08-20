package net.chesstango.search.smart.debug;

import net.chesstango.search.smart.debug.model.DebugNode;

/**
 * @author Mauricio Coria
 */
public interface DebugNodeTrap {
    boolean test(DebugNode debugNode);

    void debugAction(DebugNode debugNode);
}
