package net.chesstango.search.smart.alphabeta.filters;

import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.moves.MoveContainerReader;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.StopSearchingException;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.StopSearchingListener;
import net.chesstango.search.smart.transposition.TranspositionEntry;

/**
 * @author Mauricio Coria
 */
public class QuiescenceFlowControl implements AlphaBetaFilter, SearchByCycleListener, StopSearchingListener {
    private volatile boolean keepProcessing;

    @Setter
    private AlphaBetaFilter leafNode;

    @Setter
    private AlphaBetaFilter interiorNode;

    private Game game;

    @Override
    public void beforeSearch(SearchByCycleContext context) {
        this.game = context.getGame();
        this.keepProcessing = true;
    }

    @Override
    public void afterSearch() {
    }

    @Override
    public void stopSearching() {
        this.keepProcessing = false;
    }

    @Override
    public long maximize(int currentPly, int alpha, int beta) {
        if (!keepProcessing) {
            throw new StopSearchingException();
        }

        if (game.getStatus().isFinalStatus() || isCurrentPositionQuiet()) {
            leafNode.maximize(currentPly, alpha, beta);
        }

        return interiorNode.maximize(currentPly, alpha, beta);
    }

    @Override
    public long minimize(int currentPly, int alpha, int beta) {
        if (!keepProcessing) {
            throw new StopSearchingException();
        }

        if (game.getStatus().isFinalStatus() || isCurrentPositionQuiet()) {
            leafNode.minimize(currentPly, alpha, beta);
        }

        return interiorNode.minimize(currentPly, alpha, beta);
    }

    private boolean isCurrentPositionQuiet() {
        MoveContainerReader possibleMoves = game.getPossibleMoves();
        return possibleMoves.hasQuietMoves();
    }
}
