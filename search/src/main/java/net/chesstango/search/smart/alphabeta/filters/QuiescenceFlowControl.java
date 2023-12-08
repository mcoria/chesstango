package net.chesstango.search.smart.alphabeta.filters;

import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.moves.MoveContainerReader;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.StopSearchingException;
import net.chesstango.search.smart.SearchContext;
import net.chesstango.search.smart.SearchCycleListener;
import net.chesstango.search.smart.StopSearchListener;
import net.chesstango.search.smart.transposition.TranspositionEntry;

/**
 * @author Mauricio Coria
 */
public class QuiescenceFlowControl implements AlphaBetaFilter, SearchCycleListener, StopSearchListener {
    private volatile boolean keepProcessing;

    @Setter
    private GameEvaluator gameEvaluator;

    @Setter
    private AlphaBetaFilter next;
    private Game game;

    @Override
    public void beforeSearch(Game game) {
        this.game = game;
        this.keepProcessing = true;
    }

    @Override
    public void afterSearch(SearchMoveResult result) {
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
        if (!game.getStatus().isInProgress()) {
            return TranspositionEntry.encode(gameEvaluator.evaluate());
        }

        if (isCurrentPositionQuiet()) {
            return TranspositionEntry.encode(gameEvaluator.evaluate());
        }

        return next.maximize(currentPly, alpha, beta);
    }

    @Override
    public long minimize(int currentPly, int alpha, int beta) {
        if (!keepProcessing) {
            throw new StopSearchingException();
        }
        if (!game.getStatus().isInProgress()) {
            return TranspositionEntry.encode(gameEvaluator.evaluate());
        }

        if (isCurrentPositionQuiet()) {
            return TranspositionEntry.encode(gameEvaluator.evaluate());
        }

        return next.minimize(currentPly, alpha, beta);
    }

    private boolean isCurrentPositionQuiet() {
        MoveContainerReader possibleMoves = game.getPossibleMoves();
        return possibleMoves.hasQuietMoves();
    }
}
