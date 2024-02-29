package net.chesstango.search.smart.alphabeta.debug.traps.predicates;

import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.search.smart.alphabeta.debug.DebugNode;

import java.util.function.Predicate;

/**
 * @author Mauricio Coria
 */
@Accessors(chain = true)
@Setter
public class NodeByZobrist implements Predicate<DebugNode> {

    private DebugNode.NodeTopology topology;

    private long zobristHash;

    private int alpha;

    private int beta;

    @Override
    public boolean test(DebugNode debugNode) {
        return debugNode.getTopology() == topology &&
                debugNode.getZobristHash() == zobristHash &&
                debugNode.getAlpha() == alpha &&
                debugNode.getBeta() == beta;
    }
}
