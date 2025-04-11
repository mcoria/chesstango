package net.chesstango.search.smart.features.debug.traps;

import net.chesstango.search.smart.SearchByDepthContext;
import net.chesstango.search.smart.SearchByDepthListener;
import net.chesstango.search.smart.features.debug.model.DebugNode;
import net.chesstango.search.smart.features.debug.DebugNodeTrap;

import java.io.PrintStream;

/**
 * @author Mauricio Coria
 */
public class LeafNodeTrap implements DebugNodeTrap, SearchByDepthListener {
    private int maxPly;

    @Override
    public boolean test(DebugNode debugNode) {
        if (DebugNode.NodeTopology.INTERIOR.equals(debugNode.getTopology()) && debugNode.getPly() == maxPly - 1) {
            return debugNode.getChildNodes()
                    .stream()
                    .filter(childNode -> DebugNode.NodeTopology.HORIZON.equals(childNode.getTopology()))
                    .filter(childNode -> !childNode.getChildNodes().isEmpty()) // HORIZON siempre tiene un solo nodo asociado
                    .map(childNode -> childNode.getChildNodes().getFirst())
                    .filter(childNode -> DebugNode.NodeTopology.LEAF.equals(childNode.getTopology()))
                    .map(DebugNode::getType)
                    .distinct()
                    .count() > 2;
        }
        return false;
    }

    @Override
    public void debugAction(DebugNode debugNode, PrintStream debugOut) {
        debugOut.printf("%s %d POSIBLE NODO\n", ">\t".repeat(debugNode.getPly()), debugNode.getPly());
    }

    @Override
    public void beforeSearchByDepth(SearchByDepthContext context) {
        maxPly = context.getMaxPly();
    }
}
