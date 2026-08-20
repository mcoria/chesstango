package net.chesstango.search.smart.debug.traps;

import lombok.Setter;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.debug.DebugNodeTrap;
import net.chesstango.search.smart.debug.model.DebugNode;
import net.chesstango.search.smart.debug.model.NodeTopology;

/**
 * @author Mauricio Coria
 */
public class LeafNodeTrap implements DebugNodeTrap, Acceptor {

    @Setter
    private int maxPly;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean test(DebugNode debugNode) {
        if (NodeTopology.INTERIOR.equals(debugNode.getTopology()) && debugNode.getPly() == maxPly - 1) {
            return debugNode.getChildNodes()
                    .stream()
                    //.filter(childNode -> DebugNode.NodeTopology.HORIZON.equals(childNode.getTopology()))
                    .filter(childNode -> !childNode.getChildNodes().isEmpty()) // HORIZON siempre tiene un solo nodo asociado
                    .map(childNode -> childNode.getChildNodes().getFirst())
                    .filter(childNode -> NodeTopology.LEAF.equals(childNode.getTopology()))
                    .map(DebugNode::getType)
                    .distinct()
                    .count() > 2;
        }
        return false;
    }

    @Override
    public void debugAction(DebugNode debugNode) {
        System.out.printf("%s %d POSIBLE NODO\n", ">\t".repeat(debugNode.getPly()), debugNode.getPly());
    }
}
