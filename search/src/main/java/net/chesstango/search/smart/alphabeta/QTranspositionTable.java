package net.chesstango.search.smart.alphabeta;

import net.chesstango.board.Game;
import net.chesstango.search.SearchMoveResult;
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
    public void close(SearchMoveResult result) {

    }

    @Override
    public long maximize(final int currentPly, final int alpha, final int beta) {
        if(game.getStatus().isInProgress()) {
            long hash = game.getChessPosition().getPositionHash();

            Long bestMoveAndValue = qMaxMap.get(hash);

            if (bestMoveAndValue == null) {

                bestMoveAndValue = next.maximize(currentPly, alpha, beta);

                if(bestMoveAndValue > alpha && bestMoveAndValue < beta) {
                    qMaxMap.put(hash, bestMoveAndValue);
                }
            }
            return bestMoveAndValue;
        }
        return next.maximize(currentPly, alpha, beta);
    }

    @Override
    public long minimize(final int currentPly, final int alpha, final int beta) {
        if(game.getStatus().isInProgress()) {
            long hash = game.getChessPosition().getPositionHash();

            Long bestMoveAndValue = qMinMap.get(hash);

            if (bestMoveAndValue == null) {

                bestMoveAndValue = next.minimize(currentPly, alpha, beta);

                if(bestMoveAndValue > alpha && bestMoveAndValue < beta) {
                    qMinMap.put(hash, bestMoveAndValue);
                }
            }
            return bestMoveAndValue;
        }
        return next.minimize(currentPly, alpha, beta);
    }

    @Override
    public void stopSearching() {
    }

    public void setNext(AlphaBetaFilter next) {
        this.next = next;
    }
}
