package net.chesstango.search.smart.alphabeta.filters;

import net.chesstango.board.Game;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.BinaryUtils;
import net.chesstango.search.smart.SearchContext;
import net.chesstango.search.smart.transposition.QTransposition;
import net.chesstango.search.smart.transposition.TTable;
import net.chesstango.search.smart.transposition.TranspositionType;

/**
 * @author Mauricio Coria
 */
public class QTranspositionTable implements AlphaBetaFilter {
    private AlphaBetaFilter next;
    private TTable<QTransposition> tTable;
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
        this.tTable = context.getQTTable();
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

            QTransposition entry = tTable.read(hash);

            if (entry == null) {
                bestMoveAndValue = next.maximize(currentPly, alpha, beta);
            } else {

                /**
                 * Antes buscabamos en la tabla normal
                 */


                if (entry.getBestMoveAndValue() != 0) {
                    // Es un valor exacto
                    if (entry.getType() == TranspositionType.EXACT) {
                        return entry.getBestMoveAndValue();
                    } else if (entry.getType() == TranspositionType.LOWER_BOUND && beta <= entry.getValue()) {
                        return entry.getBestMoveAndValue();
                    } else if (entry.getType() == TranspositionType.UPPER_BOUND && entry.getValue() <= alpha) {
                        return entry.getBestMoveAndValue();
                    }
                }
                bestMoveAndValue = next.maximize(currentPly, alpha, beta);
            }

            entry = updateQEntry(hash, entry, alpha, beta, bestMoveAndValue);

            return entry.getBestMoveAndValue();
        }

        return next.maximize(currentPly, alpha, beta);
    }

    @Override
    public long minimize(final int currentPly, final int alpha, final int beta) {
        if (game.getStatus().isInProgress()) {
            long hash = game.getChessPosition().getZobristHash();
            long bestMoveAndValue;

            QTransposition entry = tTable.read(hash);

            if (entry == null) {
                bestMoveAndValue = next.minimize(currentPly, alpha, beta);
            } else {

                /**
                 * Antes buscabamos en la tabla normal
                 */

                if (entry.getBestMoveAndValue() != 0) {
                    // Es un valor exacto
                    if (entry.getType() == TranspositionType.EXACT) {
                        return entry.getBestMoveAndValue();
                    } else if (entry.getType() == TranspositionType.LOWER_BOUND && beta <= entry.getValue()) {
                        return entry.getBestMoveAndValue();
                    } else if (entry.getType() == TranspositionType.UPPER_BOUND && entry.getValue() <= alpha) {
                        return entry.getBestMoveAndValue();
                    }
                }

                bestMoveAndValue = next.minimize(currentPly, alpha, beta);
            }

            entry = updateQEntry(hash, entry, alpha, beta, bestMoveAndValue);

            return entry.getBestMoveAndValue();
        }

        return next.minimize(currentPly, alpha, beta);
    }

    public void setNext(AlphaBetaFilter next) {
        this.next = next;
    }

    protected QTransposition updateQEntry(long hash, QTransposition entry, int alpha, int beta, long bestMoveAndValue) {
        int value = BinaryUtils.decodeValue(bestMoveAndValue);

        TranspositionType type;
        if (beta <= value) {
            type = TranspositionType.LOWER_BOUND;
        } else if (value <= alpha) {
            type = TranspositionType.UPPER_BOUND;
        } else {
            type = TranspositionType.EXACT;
        }

        if (entry == null) {
            entry = new QTransposition(hash);
        }

        entry.setBestMoveAndValue(bestMoveAndValue);
        entry.setType(type);

        tTable.write(entry);

        return entry;
    }
}
