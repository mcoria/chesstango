package net.chesstango.search.smart.alphabeta.debug.filters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.alphabeta.AlphaBetaFilter;
import net.chesstango.search.smart.alphabeta.debug.SearchTracker;
import net.chesstango.search.smart.alphabeta.debug.model.DebugNode;
import net.chesstango.search.smart.alphabeta.pv.model.TriangularPVTable;

/**
 * @author Mauricio Coria
 */
@Setter
public class DebugFilter implements AlphaBetaFilter, Acceptor {

    private final DebugNode.NodeTopology topology;

    @Getter
    private AlphaBetaFilter next;

    private SearchTracker searchTracker;

    private Game game;

    private TriangularPVTable trianglePV;

    public DebugFilter(DebugNode.NodeTopology topology) {
        this.topology = topology;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int alphaBeta(int currentPly, int alpha, int beta) {
        DebugNode debugNode = searchTracker.newNode(topology, currentPly);

        debugNode.setDebugSearch(game.getPosition().getCurrentTurn().toString(), alpha, beta);

        int currentValue = next.alphaBeta(currentPly, alpha, beta);

        debugNode.setValue(currentValue);
        debugNode.setPV(trianglePV.getPV(currentPly));

        if (currentValue <= alpha) {
            debugNode.setType(DebugNode.NodeType.ALL);
        } else if (beta <= currentValue) {
            debugNode.setType(DebugNode.NodeType.CUT);
        } else {
            debugNode.setType(DebugNode.NodeType.PV);
        }

        searchTracker.save();

        return currentValue;
    }
}
