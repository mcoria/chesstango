package net.chesstango.search.smart.alphabeta.filters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.*;
import net.chesstango.search.smart.transposition.TranspositionEntry;

/**
 * @author Mauricio Coria
 */
public class DebugTree implements AlphaBetaFilter, SearchByCycleListener, SearchByDepthListener {

    private SearchTracker searchTracker;

    private Game game;

    @Setter
    @Getter
    private GameEvaluator gameEvaluator;

    @Setter
    @Getter
    private AlphaBetaFilter next;
    private int maxPly;

    private final SearchTracker.NodeType nodeType;

    public DebugTree(SearchTracker.NodeType nodeType) {
        this.nodeType = nodeType;
    }

    @Override
    public void beforeSearch(SearchByCycleContext context) {
        this.game = context.getGame();
        this.searchTracker = context.getSearchTracker();
    }

    @Override
    public void afterSearch() {

    }

    @Override
    public void beforeSearchByDepth(SearchByDepthContext context) {
        this.maxPly = context.getMaxPly();
    }

    @Override
    public void afterSearchByDepth(SearchMoveResult result) {

    }

    @Override
    public long maximize(int currentPly, int alpha, int beta) {
        return debugSearch(next::maximize, "MAX", currentPly, alpha, beta);
    }


    @Override
    public long minimize(int currentPly, int alpha, int beta) {
        return debugSearch(next::minimize, "MIN", currentPly, alpha, beta);
    }

    private long debugSearch(AlphaBetaFunction fn, String fnString, int currentPly, int alpha, int beta) {

        searchTracker.newNode(nodeType);

        searchTracker.setZobristHash(game.getChessPosition().getZobristHash());

        if (game.getState().getPreviousState() != null) {
            Move currentMove = game.getState().getPreviousState().getSelectedMove();

            searchTracker.setSelectedMove(currentMove);
        }

        searchTracker.setDebugSearch(fnString, alpha, beta);

        if (maxPly <= currentPly) {
            searchTracker.setStandingPat(gameEvaluator.evaluate());
        }

        long bestMoveAndValue = fn.search(currentPly, alpha, beta);

        int currentValue = TranspositionEntry.decodeValue(bestMoveAndValue);

        searchTracker.setValue(currentValue);

        searchTracker.save();

        return bestMoveAndValue;
    }
}
