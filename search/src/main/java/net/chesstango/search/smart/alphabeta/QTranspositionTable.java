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

    private Map<Long, Long> qMaxMap;
    private Map<Long, Long> qMinMap;
    private Game game;


    @Override
    public void init(Game game, SearchContext context) {
        this.game = game;
        this.qMaxMap = context.getQMaxMap();
        this.qMinMap = context.getQMinMap();
    }

    @Override
    public long maximize(int currentPly, int alpha, int beta) {
        long hash = game.getChessPosition().getPositionHash();

        Long bestMoveAndValue = qMaxMap.get(hash);

        if (bestMoveAndValue == null) {

            bestMoveAndValue = next.maximize(currentPly, alpha, beta);

            qMaxMap.put(hash, bestMoveAndValue);
        }
        return bestMoveAndValue;
    }

    @Override
    public long minimize(int currentPly, int alpha, int beta) {
        long hash = game.getChessPosition().getPositionHash();

        Long bestMoveAndValue = qMinMap.get(hash);

        if (bestMoveAndValue == null) {

            bestMoveAndValue = next.minimize(currentPly, alpha, beta);

            qMinMap.put(hash, bestMoveAndValue);
        }
        return bestMoveAndValue;
    }

    @Override
    public void stopSearching() {
    }

    public void setNext(AlphaBetaFilter next) {
        this.next = next;
    }
}
