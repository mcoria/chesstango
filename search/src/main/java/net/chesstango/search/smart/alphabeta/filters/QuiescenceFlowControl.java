package net.chesstango.search.smart.alphabeta.filters;

import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.moves.MoveContainerReader;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.StopSearchingException;
import net.chesstango.search.smart.SearchContext;
import net.chesstango.search.smart.transposition.TranspositionEntry;

/**
 * @author Mauricio Coria
 */
public class QuiescenceFlowControl implements AlphaBetaFilter {
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
    public void beforeSearchByDepth(SearchContext context) {
    }

    @Override
    public void afterSearchByDepth(SearchMoveResult result) {
    }

    @Override
    public void afterSearch(SearchMoveResult result) {
    }

    @Override
    public void stopSearching() {
        this.keepProcessing = false;
    }

    @Override
    public void reset() {
    }

    @Override
    public long maximize(int currentPly, int alpha, int beta) {
        if (!keepProcessing) {
            throw new StopSearchingException();
        }
        if (!game.getStatus().isInProgress()) {
            return TranspositionEntry.encode(gameEvaluator.evaluate(game));
        }

        if (isCurrentPositionQuiet()) {
            return TranspositionEntry.encode(gameEvaluator.evaluate(game));
        }

        return next.maximize(currentPly, alpha, beta);
    }

    @Override
    public long minimize(int currentPly, int alpha, int beta) {
        if (!keepProcessing) {
            throw new StopSearchingException();
        }
        if (!game.getStatus().isInProgress()) {
            return TranspositionEntry.encode(gameEvaluator.evaluate(game));
        }

        if (isCurrentPositionQuiet()) {
            return TranspositionEntry.encode(gameEvaluator.evaluate(game));
        }

        return next.minimize(currentPly, alpha, beta);
    }

    private boolean isCurrentPositionQuiet() {
        MoveContainerReader possibleMoves = game.getPossibleMoves();
        return possibleMoves.hasQuietMoves();
    }
}
