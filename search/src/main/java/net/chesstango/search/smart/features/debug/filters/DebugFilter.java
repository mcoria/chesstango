package net.chesstango.search.smart.features.debug.filters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.features.debug.SearchTracker;
import net.chesstango.search.smart.features.debug.model.DebugNode;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFilter;
import net.chesstango.search.smart.features.transposition.TranspositionEntry;

/**
 * @author Mauricio Coria
 */
public class DebugFilter implements AlphaBetaFilter, SearchByCycleListener {

    private SearchTracker searchTracker;

    @Setter
    @Getter
    private AlphaBetaFilter next;

    private final DebugNode.NodeTopology topology;

    public DebugFilter(DebugNode.NodeTopology topology) {
        this.topology = topology;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void beforeSearch(SearchByCycleContext context) {
        Game game = context.getGame();
        this.searchTracker = context.getSearchTracker();
    }

    @Override
    public long maximize(int currentPly, int alpha, int beta) {
        DebugNode debugNode = createDebugNode("MAX", currentPly, alpha, beta);

        long bestMoveAndValue = next.maximize(currentPly, alpha, beta);

        int currentValue = TranspositionEntry.decodeValue(bestMoveAndValue);

        debugNode.setValue(currentValue);

        if (currentValue <= alpha) {
            debugNode.setType(DebugNode.NodeType.ALL);
        } else if (beta <= currentValue) {
            debugNode.setType(DebugNode.NodeType.CUT);
        } else {
            debugNode.setType(DebugNode.NodeType.PV);
        }

        searchTracker.save();

        return bestMoveAndValue;
    }


    @Override
    public long minimize(int currentPly, int alpha, int beta) {
        DebugNode debugNode = createDebugNode("MIN", currentPly, alpha, beta);

        long bestMoveAndValue = next.minimize(currentPly, alpha, beta);

        int currentValue = TranspositionEntry.decodeValue(bestMoveAndValue);

        debugNode.setValue(currentValue);

        if (currentValue <= alpha) {
            debugNode.setType(DebugNode.NodeType.CUT);
        } else if (beta <= currentValue) {
            debugNode.setType(DebugNode.NodeType.ALL);
        } else {
            debugNode.setType(DebugNode.NodeType.PV);
        }

        searchTracker.save();

        return bestMoveAndValue;
    }

    private DebugNode createDebugNode(String fnString, int currentPly, int alpha, int beta) {
        DebugNode debugNode = searchTracker.newNode(topology, currentPly);

        debugNode.setDebugSearch(fnString, alpha, beta);

        return debugNode;
    }
}
