package net.chesstango.search.smart.alphabeta.filters.once;

import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.search.MoveEvaluation;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.StopSearchingException;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.SearchByDepthContext;
import net.chesstango.search.smart.SearchByDepthListener;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFilter;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFunction;
import net.chesstango.search.smart.transposition.TranspositionEntry;

import java.util.Objects;

/**
 * @author Mauricio Coria
 */
public class StopProcessingCatch implements AlphaBetaFilter, SearchByCycleListener, SearchByDepthListener {

    @Setter
    private AlphaBetaFilter next;

    @Setter
    private Game game;

    private MoveEvaluation lastBestMoveEvaluation;

    @Override
    public void beforeSearch(SearchByCycleContext context) {
        this.game = context.getGame();
        this.lastBestMoveEvaluation = null;
    }

    @Override
    public void afterSearch() {
    }

    @Override
    public void beforeSearchByDepth(SearchByDepthContext context) {
        this.lastBestMoveEvaluation = context.getLastBestMoveEvaluation();
    }

    @Override
    public void afterSearchByDepth(SearchMoveResult result) {
    }

    @Override
    public long maximize(int currentPly, int alpha, int beta) {
        return process(currentPly, alpha, beta, next::maximize, false);
    }

    @Override
    public long minimize(int currentPly, int alpha, int beta) {
        return process(currentPly, alpha, beta, next::minimize, true);
    }

    private long process(int currentPly, int alpha, int beta, AlphaBetaFunction fn, boolean naturalOrderSort) {
        final long startHash = game.getChessPosition().getZobristHash();

        try {
            return fn.search(currentPly, alpha, beta);
        } catch (StopSearchingException re) {
            undoMoves(startHash);
        }

        /**
         * Podria retornar algo mejor que el resultado de la busqueda anterior
         */
        if (Objects.nonNull(lastBestMoveEvaluation)) {
            Move bestMove = lastBestMoveEvaluation.move();
            int bestValue = lastBestMoveEvaluation.evaluation();
            return TranspositionEntry.encode(bestMove, bestValue);
        }

        throw new RuntimeException("Stopped too early");
    }

    private void undoMoves(long startHash) {
        long currentHash = game.getChessPosition().getZobristHash();
        while (currentHash != startHash) {
            game.undoMove();
            currentHash = game.getChessPosition().getZobristHash();
        }
    }
}
