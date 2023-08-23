package net.chesstango.search.smart.alphabeta.filters;

import net.chesstango.board.Game;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.BinaryUtils;
import net.chesstango.search.smart.transposition.QTransposition;
import net.chesstango.search.smart.SearchContext;

import java.util.Map;

import static net.chesstango.search.smart.transposition.QTransposition.Type;

/**
 * @author Mauricio Coria
 */
public class QTranspositionTable implements AlphaBetaFilter {
    private AlphaBetaFilter next;
    private Map<Long, QTransposition> maxMap;
    private Map<Long, QTransposition> minMap;
    private Game game;

    @Override
    public void beforeSearch(Game game, int maxDepth) {
        this.game = game;
    }

    @Override
    public void afterSearch(SearchMoveResult result) {
    }

    @Override
    public void beforeSearchByDepth(SearchContext context) {
        this.maxMap = context.getQMaxMap();
        this.minMap = context.getQMinMap();
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
    public long maximize(final int currentPly, final int alpha, final int beta) {
        if (game.getStatus().isInProgress()) {
            long hash = game.getChessPosition().getZobristHash();
            long bestMoveAndValue;

            QTransposition entry = maxMap.get(hash);

            if (entry == null) {
                entry = new QTransposition();

                maxMap.put(hash, entry);

                bestMoveAndValue = next.maximize(currentPly, alpha, beta);
            } else {

                /**
                 * Antes buscabamos en la tabla normal
                 */


                if (entry.qBestMoveAndValue != 0) {
                    // Es un valor exacto
                    if (entry.qType == Type.EXACT) {
                        return entry.qBestMoveAndValue;
                    } else if (entry.qType == Type.LOWER_BOUND && beta <= entry.getQValue()) {
                        return entry.qBestMoveAndValue;
                    } else if (entry.qType == Type.UPPER_BOUND && entry.getQValue() <= alpha) {
                        return entry.qBestMoveAndValue;
                    }
                }
                bestMoveAndValue = next.maximize(currentPly, alpha, beta);
            }

            updateQEntry(entry, alpha, beta, bestMoveAndValue);

            return entry.qBestMoveAndValue;
        }

        return next.maximize(currentPly, alpha, beta);
    }

    @Override
    public long minimize(final int currentPly, final int alpha, final int beta) {
        if (game.getStatus().isInProgress()) {
            long hash = game.getChessPosition().getZobristHash();
            long bestMoveAndValue;

            QTransposition entry = minMap.get(hash);

            if (entry == null) {
                entry = new QTransposition();

                minMap.put(hash, entry);

                bestMoveAndValue = next.minimize(currentPly, alpha, beta);
            } else {

                /**
                 * Antes buscabamos en la tabla normal
                 */

                if (entry.qBestMoveAndValue != 0) {
                    // Es un valor exacto
                    if (entry.qType == Type.EXACT) {
                        return entry.qBestMoveAndValue;
                    } else if (entry.qType == Type.LOWER_BOUND && beta <= entry.getQValue()) {
                        return entry.qBestMoveAndValue;
                    } else if (entry.qType == Type.UPPER_BOUND && entry.getQValue() <= alpha) {
                        return entry.qBestMoveAndValue;
                    }
                }

                bestMoveAndValue = next.minimize(currentPly, alpha, beta);
            }

            updateQEntry(entry, alpha, beta, bestMoveAndValue);

            return entry.qBestMoveAndValue;
        }

        return next.minimize(currentPly, alpha, beta);
    }

    public void setNext(AlphaBetaFilter next) {
        this.next = next;
    }

    protected void updateQEntry(QTransposition entry, int alpha, int beta, long bestMoveAndValue) {
        int value = BinaryUtils.decodeValue(bestMoveAndValue);

        Type type;
        if (beta <= value) {
            type = Type.LOWER_BOUND;
        } else if (value <= alpha) {
            type = Type.UPPER_BOUND;
        } else {
            type = Type.EXACT;
        }

        entry.qBestMoveAndValue = bestMoveAndValue;
        entry.qType = type;
    }
}
