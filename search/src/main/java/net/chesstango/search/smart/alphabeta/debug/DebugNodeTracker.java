package net.chesstango.search.smart.alphabeta.debug;

import lombok.Getter;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.SearchByDepthListener;
import net.chesstango.search.smart.SearchByWindowsListener;
import net.chesstango.search.smart.alphabeta.debug.model.DebugNode;
import net.chesstango.search.smart.alphabeta.debug.model.NodeTopology;

/**
 * @author Mauricio Coria
 */
public class DebugNodeTracker implements Acceptor, SearchByCycleListener, SearchByDepthListener, SearchByWindowsListener {

    private DebugNode rootNode;

    @Getter
    private DebugNode currentNode;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void beforeSearch() {
        reset();
    }

    @Override
    public void beforeSearchByDepth() {
        reset();
    }

    @Override
    public void beforeSearchByWindows(int alphaBound, int betaBound, int searchByWindowsCycle) {
        reset();
    }

    public DebugNode newNode(NodeTopology topology) {
        DebugNode newNode;
        if (NodeTopology.ROOT.equals(topology)) {
            newNode = createRootNode();
            rootNode = newNode;
        } else {
            newNode = createRegularNode(topology);
            currentNode.getChildNodes().add(newNode);
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

    public void reset() {
        if (currentNode != null) {
            throw new RuntimeException("Still searching?");
        }
        rootNode = null;
    }

    public DebugNode getRootNode() {
        if (currentNode != null) {
            throw new RuntimeException("Still searching?");
        }
        return rootNode;
    }

}
