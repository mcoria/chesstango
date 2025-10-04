package net.chesstango.search.smart.features.transposition.filters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFilter;
import net.chesstango.search.smart.features.transposition.TTable;
import net.chesstango.search.smart.features.transposition.TranspositionBound;

/**
 * @author Mauricio Coria
 */
public class TranspositionTableTerminal implements AlphaBetaFilter, SearchByCycleListener {

    @Setter
    private Game game;
    private TTable maxMap;
    private TTable minMap;
    private TTable maxQMap;
    private TTable minQMap;

    @Setter
    @Getter
    private AlphaBetaFilter next;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void beforeSearch(SearchByCycleContext context) {
        this.maxMap = context.getMaxMap();
        this.minMap = context.getMinMap();
        this.maxQMap = context.getQMaxMap();
        this.minQMap = context.getQMinMap();
    }


    @Override
    public long maximize(int currentPly, int alpha, int beta) {
        long bestMoveAndValue = next.maximize(currentPly, alpha, beta);

        long hash = game.getPosition().getZobristHash();
        if (maxMap.read(hash) == null) {
            maxMap.write(hash, 0, bestMoveAndValue, TranspositionBound.EXACT);
        }
        if (maxQMap.read(hash) == null) {
            maxQMap.write(hash, 0, bestMoveAndValue, TranspositionBound.EXACT);
        }

        return bestMoveAndValue;
    }

    @Override
    public long minimize(int currentPly, int alpha, int beta) {
        long bestMoveAndValue = next.minimize(currentPly, alpha, beta);

        long hash = game.getPosition().getZobristHash();
        if (minMap.read(hash) == null) {
            minMap.write(hash, 0, bestMoveAndValue, TranspositionBound.EXACT);
        }
        if (minQMap.read(hash) == null) {
            minQMap.write(hash, 0, bestMoveAndValue, TranspositionBound.EXACT);
        }

        return bestMoveAndValue;
    }
}
