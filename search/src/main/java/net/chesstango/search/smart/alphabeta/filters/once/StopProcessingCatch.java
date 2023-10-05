package net.chesstango.search.smart.alphabeta.filters.once;

import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.StopSearchingException;
import net.chesstango.search.smart.SearchContext;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFilter;
import net.chesstango.search.smart.transposition.TranspositionEntry;

/**
 * @author Mauricio Coria
 */
public class StopProcessingCatch implements AlphaBetaFilter {

    @Setter
    private AlphaBetaFilter next;

    @Setter
    private Game game;

    @Setter
    private AlphaBetaFirst alphaBetaFirst;


    @Override
    public void beforeSearch(Game game, int maxDepth) {
        this.game = game;
    }

    @Override
    public void afterSearch(SearchMoveResult result) {

    }

    @Override
    public void beforeSearchByDepth(SearchContext context) {
    }

    @Override
    public void afterSearchByDepth(SearchMoveResult result) {

    }

    @Override
    public void stopSearching() {

    }

    @Override
    public void reset() {

    }

    @Override
    public long maximize(int currentPly, int alpha, int beta) {
        return process(currentPly, alpha, beta, true);
    }

    @Override
    public long minimize(int currentPly, int alpha, int beta) {
        return process(currentPly, alpha, beta, false);
    }

    private long process(int currentPly, int alpha, int beta, boolean maximize) {
        final long startHash = game.getChessPosition().getZobristHash();
        try {
            if (maximize) {
                return next.maximize(currentPly, alpha, beta);
            } else {
                return next.minimize(currentPly, alpha, beta);
            }
        } catch (StopSearchingException re) {
            undoMoves(startHash);
            return TranspositionEntry.encode(alphaBetaFirst.getBestMove(), null, alphaBetaFirst.getBestValue());
        }
    }

    private void undoMoves(long startHash) {
        long currentHash = game.getChessPosition().getZobristHash();
        while (currentHash != startHash) {
            game.undoMove();
            currentHash = game.getChessPosition().getZobristHash();
        }
    }
}
