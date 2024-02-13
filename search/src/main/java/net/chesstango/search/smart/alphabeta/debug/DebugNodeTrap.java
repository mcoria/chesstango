package net.chesstango.search.smart.alphabeta.debug;

import java.io.PrintStream;

/**
 * @author Mauricio Coria
 */
public interface DebugNodeTrap {
    boolean test(DebugNode debugNode);

    void debug(DebugNode debugNode, PrintStream debugOut);
}
