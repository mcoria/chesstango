package net.chesstango.search.smart.features.transposition.filters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.SearchByDepthContext;
import net.chesstango.search.smart.SearchByDepthListener;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFilter;
import net.chesstango.search.smart.features.transposition.TTable;
import net.chesstango.search.smart.features.transposition.TranspositionBound;
import net.chesstango.search.smart.features.transposition.TranspositionEntry;

/**
 * @author Mauricio Coria
 */
public class TranspositionTableRoot implements AlphaBetaFilter, SearchByCycleListener, SearchByDepthListener {

    @Setter
    @Getter
    private AlphaBetaFilter next;
    protected TTable maxMap;
    protected TTable minMap;
    protected Game game;
    protected int maxPly;

    @Override
    public void beforeSearch(SearchByCycleContext context) {
        this.game = context.getGame();
        this.maxMap = context.getMaxMap();
        this.minMap = context.getMinMap();
    }

    @Override
    public void beforeSearchByDepth(SearchByDepthContext context) {
        this.maxPly = context.getMaxPly();
    }


    @Override
    public long maximize(final int currentPly, final int alpha, final int beta) {
        long moveAndValue = next.maximize(currentPly, alpha, beta);

        long hash = game.getChessPosition().getZobristHash();

        updateEntry(maxMap, hash, maxPly, alpha, beta, moveAndValue);

        return moveAndValue;
    }

    @Override
    public long minimize(final int currentPly, final int alpha, final int beta) {
        long moveAndValue = next.minimize(currentPly, alpha, beta);

        long hash = game.getChessPosition().getZobristHash();

        updateEntry(minMap, hash, maxPly, alpha, beta, moveAndValue);

        return moveAndValue;
    }

    protected void updateEntry(TTable table, long hash, int depth, int alpha, int beta, long moveAndValue) {
        int value = TranspositionEntry.decodeValue(moveAndValue);

        //TranspositionBound transpositionBound;
        if (beta <= value) {
            //transpositionBound = TranspositionBound.LOWER_BOUND;
        } else if (value <= alpha) {
            //transpositionBound = TranspositionBound.UPPER_BOUND;
        } else {
            table.write(hash, depth, moveAndValue, TranspositionBound.EXACT);
        }
    }
}
