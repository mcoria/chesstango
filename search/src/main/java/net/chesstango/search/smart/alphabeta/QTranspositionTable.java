package net.chesstango.search.smart.alphabeta;

import net.chesstango.board.Game;
import net.chesstango.search.smart.SearchContext;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Mauricio Coria
 */
public class QTranspositionTable implements AlphaBetaFilter {

    private AlphaBetaFilter next;

    private Map<Long, Integer> maxMap;

    private Map<Long, Integer> minMap;


    @Override
    public void init(Game game, SearchContext context) {
        maxMap = new HashMap<>();
        minMap = new HashMap<>();
    }

    @Override
    public long maximize(Game game, int currentPly, int alpha, int beta) {

        long hash = game.getChessPosition().getPositionHash();

        Integer evaluation = maxMap.get(hash);

        if (evaluation == null) {
            evaluation = (int) next.maximize(game, currentPly, alpha, beta);
            maxMap.put(hash, evaluation);
        }

        return evaluation;
    }

    @Override
    public long minimize(Game game, int currentPly, int alpha, int beta) {

        long hash = game.getChessPosition().getPositionHash();

        Integer evaluation = minMap.get(hash);

        if (evaluation == null ) {
            evaluation = (int) next.minimize(game, currentPly, alpha, beta);
            minMap.put(hash, evaluation);
        }

        return evaluation;
    }

    @Override
    public void stopSearching() {
    }

    public void setNext(AlphaBetaFilter next) {
        this.next = next;
    }
}
