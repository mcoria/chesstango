package net.chesstango.search.smart.features.debug.traps;

import net.chesstango.search.smart.features.debug.DebugNodeTrap;
import net.chesstango.search.smart.features.debug.model.DebugNode;

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
    public void debugAction(DebugNode debugNode, PrintStream debugOut) {
        debugOut.printf("%s JAJAJEJEJIJI\n", ">\t".repeat(debugNode.getPly()));
    }
}
