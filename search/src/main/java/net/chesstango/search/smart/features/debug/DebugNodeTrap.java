package net.chesstango.search.smart.features.debug;

import net.chesstango.search.smart.features.debug.model.DebugNode;

import java.io.PrintStream;

/**
 * @author Mauricio Coria
 */
public interface DebugNodeTrap {
    boolean test(DebugNode debugNode);

    void debugAction(DebugNode debugNode, PrintStream debugOut);
}
