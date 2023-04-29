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
    private Game game;


    @Override
    public void init(Game game, SearchContext context) {
        this.game = game;
        this.maxMap = new HashMap<>();
        this.minMap = new HashMap<>();
    }

    @Override
    public long maximize(int currentPly, int alpha, int beta) {

        long hash = game.getChessPosition().getPositionHash();

        Integer evaluation = maxMap.get(hash);

        if (evaluation == null) {
            evaluation = (int) next.maximize(currentPly, alpha, beta);
            maxMap.put(hash, evaluation);
        }

        return evaluation;
    }

    @Override
    public long minimize(int currentPly, int alpha, int beta) {

        long hash = game.getChessPosition().getPositionHash();

        Integer evaluation = minMap.get(hash);

        if (evaluation == null ) {
            evaluation = (int) next.minimize(currentPly, alpha, beta);
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
