package net.chesstango.search.smart.alphabeta.debug.traps.predicates;

import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.search.smart.alphabeta.debug.model.DebugNode;
import net.chesstango.search.smart.alphabeta.debug.model.NodeTopology;

import java.util.function.Predicate;

/**
 * @author Mauricio Coria
 */
@Accessors(chain = true)
@Setter
@AllArgsConstructor
public class NodeByZobrist implements Predicate<DebugNode> {

    private NodeTopology topology;

    private int depth;

    private int searchByWindowsCycle;

    private long zobristHash;

    private int ply;

    @Override
    public boolean test(DebugNode debugNode) {
        return debugNode.getTopology() == topology &&
                debugNode.getZobristHash() == zobristHash &&
                debugNode.getDepth() == depth &&
                debugNode.getSearchByWindowsCycle() == searchByWindowsCycle &&
                debugNode.getPly() == ply;
    }
}
