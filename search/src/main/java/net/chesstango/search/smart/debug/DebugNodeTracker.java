package net.chesstango.search.smart.debug;

import lombok.Getter;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.SearchByDepthListener;
import net.chesstango.search.smart.debug.model.DebugNode;
import net.chesstango.search.smart.debug.model.NodeTopology;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class DebugNodeTracker implements Acceptor, SearchByDepthListener {

    @Getter
    private List<DebugNode> debugNodes;

    @Getter
    private DebugNode currentNode;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void beforeSearchByDepth() {
        debugNodes = new LinkedList<>();
    }

    public DebugNode newNode(NodeTopology topology) {
        DebugNode newNode;
        if (NodeTopology.ROOT.equals(topology)) {
            if (currentNode != null) {
                throw new RuntimeException("Still searching?");
            }
            newNode = createRootNode();
            debugNodes.add(newNode);
        } else {
            newNode = createRegularNode(topology);
            currentNode.addChild(newNode);
        }

        currentNode = newNode;

        return currentNode;
    }

    protected DebugNode createRootNode() {
        assert currentNode == null;
        DebugNode newNode = new DebugNode();
        newNode.setTopology(NodeTopology.ROOT);
        newNode.setParent(null);    // El root no tiene padre
        return newNode;
    }

    protected DebugNode createRegularNode(NodeTopology topology) {
        DebugNode newNode = new DebugNode();
        newNode.setTopology(topology);
        newNode.setParent(currentNode);
        return newNode;
    }

    public void save() {
        currentNode.validate();
        currentNode = currentNode.getParent();
    }

    public DebugNode getRootNode() {
        if (currentNode != null) {
            throw new RuntimeException("Still searching?");
        }
        return debugNodes.getLast();
    }

}
