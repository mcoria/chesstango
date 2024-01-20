package net.chesstango.search.smart.alphabeta.debug.traps;

import net.chesstango.search.smart.alphabeta.debug.DebugNode;
import net.chesstango.search.smart.alphabeta.debug.DebugNodeTrap;

import java.io.PrintStream;

/**
 * @author Mauricio Coria
 */
public class SampleTrap implements DebugNodeTrap {
    @Override
    public boolean test(DebugNode debugNode) {
        return debugNode.getZobristHash() == 0x02EE763BA4FA755EL;
    }

    @Override
    public void debug(int depth, DebugNode debugNode, PrintStream debugOut) {
        debugOut.printf("%s JAJAJEJEJIJI\n", ">\t".repeat(depth));
    }
}
