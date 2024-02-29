package net.chesstango.search.smart.alphabeta.debug;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFilter;
import net.chesstango.search.smart.transposition.TranspositionEntry;

/**
 * @author Mauricio Coria
 */
public class DebugFilter implements AlphaBetaFilter, SearchByCycleListener {

    private SearchTracker searchTracker;

    private Game game;

    @Setter
    @Getter
    private AlphaBetaFilter next;

    private final DebugNode.NodeTopology topology;

    public DebugFilter(DebugNode.NodeTopology topology) {
        this.topology = topology;
    }

    @Override
    public void beforeSearch(SearchByCycleContext context) {
        this.game = context.getGame();
        this.searchTracker = context.getSearchTracker();
    }

    @Override
    public long maximize(int currentPly, int alpha, int beta) {
        DebugNode debugNode = createDebugNode("MAX", currentPly, alpha, beta);

        long bestMoveAndValue = next.maximize(currentPly, alpha, beta);

        int currentValue = TranspositionEntry.decodeValue(bestMoveAndValue);

        debugNode.value = currentValue;

        if (currentValue <= alpha) {
            debugNode.type = DebugNode.NodeType.ALL;
        } else if (beta <= currentValue) {
            debugNode.type = DebugNode.NodeType.CUT;
        } else {
            debugNode.type = DebugNode.NodeType.PV;
        }

        searchTracker.save();

        return bestMoveAndValue;
    }


    @Override
    public long minimize(int currentPly, int alpha, int beta) {
        DebugNode debugNode = createDebugNode("MIN", currentPly, alpha, beta);

        long bestMoveAndValue = next.minimize(currentPly, alpha, beta);

        int currentValue = TranspositionEntry.decodeValue(bestMoveAndValue);

        debugNode.value = currentValue;

        if (currentValue <= alpha) {
            debugNode.type = DebugNode.NodeType.CUT;
        } else if (beta <= currentValue) {
            debugNode.type = DebugNode.NodeType.ALL;
        } else {
            debugNode.type = DebugNode.NodeType.PV;
        }

        searchTracker.save();

        return bestMoveAndValue;
    }

    private DebugNode createDebugNode(String fnString, int currentPly, int alpha, int beta) {
        DebugNode debugNode = searchTracker.newNode(topology, currentPly);

        debugNode.setZobristHash(game.getChessPosition().getZobristHash());

        if (game.getState().getPreviousState() != null) {
            debugNode.selectedMove = game.getState().getPreviousState().getSelectedMove();
        }

        debugNode.setDebugSearch(fnString, alpha, beta);

        return debugNode;
    }
}
