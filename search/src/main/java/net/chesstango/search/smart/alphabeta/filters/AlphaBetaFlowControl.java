package net.chesstango.search.smart.alphabeta.filters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.StopSearchingException;
import net.chesstango.search.smart.*;
import net.chesstango.search.smart.transposition.TranspositionEntry;

/**
 * @author Mauricio Coria
 */
public class AlphaBetaFlowControl implements AlphaBetaFilter, SearchByCycleListener, SearchByDepthListener, StopSearchingListener {
    private volatile boolean keepProcessing;

    @Setter
    @Getter
    private AlphaBetaFilter interiorNode;

    @Setter
    @Getter
    private AlphaBetaFilter horizonNode;

    @Setter
    @Getter
    private AlphaBetaFilter terminalNode;

    @Setter
    @Getter
    private AlphaBetaFilter loopNode;

    private int maxPly;
    private Game game;

    @Override
    public void beforeSearch(SearchByCycleContext context) {
        this.game = context.getGame();
        this.keepProcessing = true;
    }

    @Override
    public void beforeSearchByDepth(SearchByDepthContext context) {
        this.maxPly = context.getMaxPly();
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

        if (game.getState().getRepetitionCounter() > 1) {
            return loopNode.maximize(currentPly, alpha, beta);
        }

        if (game.getStatus().isFinalStatus()) {
            return terminalNode.maximize(currentPly, alpha, beta);
        }

        if (currentPly == maxPly) {
            return horizonNode.maximize(currentPly, alpha, beta);
        }

        return interiorNode.maximize(currentPly, alpha, beta);
    }

    @Override
    public long minimize(int currentPly, int alpha, int beta) {
        if (!keepProcessing) {
            throw new StopSearchingException();
        }

        if (game.getState().getRepetitionCounter() > 1) {
            return loopNode.minimize(currentPly, alpha, beta);
        }

        if (game.getStatus().isFinalStatus()) {
            return terminalNode.minimize(currentPly, alpha, beta);
        }

        if (currentPly == maxPly) {
            return horizonNode.minimize(currentPly, alpha, beta);
        }

        return interiorNode.minimize(currentPly, alpha, beta);
    }
}
