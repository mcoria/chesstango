package net.chesstango.search.smart.alphabeta.filters;

import net.chesstango.board.Game;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.BinaryUtils;
import net.chesstango.search.smart.SearchContext;
import net.chesstango.search.smart.transposition.TTable;
import net.chesstango.search.smart.transposition.Transposition;
import net.chesstango.search.smart.transposition.TranspositionType;

/**
 * @author Mauricio Coria
 */
public class TranspositionTable implements AlphaBetaFilter {
    private AlphaBetaFilter next;
    private TTable<Transposition> tTable;

    private Game game;
    private int maxPly;

    @Override
    public void beforeSearch(Game game, int maxDepth) {
        this.game = game;
    }

    @Override
    public void afterSearch(SearchMoveResult result) {

    }

    @Override
    public void beforeSearchByDepth(SearchContext context) {
        this.maxPly = context.getMaxPly();
        this.tTable = context.getTTable();
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
        int searchDepth = maxPly - currentPly;

        if (searchDepth > 0 && game.getStatus().isInProgress()) {
            long hash = game.getChessPosition().getZobristHash();
            long bestMoveAndValue;

            Transposition entry = new Transposition();

            if (!tTable.read(hash, entry)) {
                bestMoveAndValue = next.maximize(currentPly, alpha, beta);
            } else {
                if (entry.getBestMoveAndValue() != 0) {
                    if (searchDepth <= entry.getSearchDepth()) {
                        // Es un valor exacto
                        if (entry.getType() == TranspositionType.EXACT) {
                            return entry.getBestMoveAndValue();
                        } else if (entry.getType() == TranspositionType.LOWER_BOUND && beta <= entry.getValue()) {
                            return entry.getBestMoveAndValue();
                        } else if (entry.getType() == TranspositionType.UPPER_BOUND && entry.getValue() <= alpha) {
                            return entry.getBestMoveAndValue();
                        }
                    }
                }
                bestMoveAndValue = next.maximize(currentPly, alpha, beta);
            }

            updateEntry(entry, hash, searchDepth, alpha, beta, bestMoveAndValue);

            return bestMoveAndValue;
        }

        return next.maximize(currentPly, alpha, beta);
    }

    @Override
    public long minimize(final int currentPly, final int alpha, final int beta) {
        int searchDepth = maxPly - currentPly;

        if (searchDepth > 0 && game.getStatus().isInProgress()) {
            long hash = game.getChessPosition().getZobristHash();
            long bestMoveAndValue;

            Transposition entry = new Transposition();

            if (!tTable.read(hash, entry)) {
                bestMoveAndValue = next.minimize(currentPly, alpha, beta);
            } else {
                if (entry.getBestMoveAndValue() != 0) {
                    if (searchDepth <= entry.getSearchDepth()) {
                        // Es un valor exacto
                        if (entry.getType() == TranspositionType.EXACT) {
                            return entry.getBestMoveAndValue();
                        } else if (entry.getType() == TranspositionType.LOWER_BOUND && beta <= entry.getValue()) {
                            return entry.getBestMoveAndValue();
                        } else if (entry.getType() == TranspositionType.UPPER_BOUND && entry.getValue() <= alpha) {
                            return entry.getBestMoveAndValue();
                        }
                    }
                }
                bestMoveAndValue = next.minimize(currentPly, alpha, beta);
            }

            updateEntry(entry, hash, searchDepth, alpha, beta, bestMoveAndValue);

            return bestMoveAndValue;
        }

        return next.minimize(currentPly, alpha, beta);
    }

    public void setNext(AlphaBetaFilter next) {
        this.next = next;
    }

    protected void updateEntry(Transposition entry, long hash, int searchDepth, int alpha, int beta, long bestMoveAndValue) {

        int value = BinaryUtils.decodeValue(bestMoveAndValue);

        TranspositionType type;
        if (beta <= value) {
            type = TranspositionType.LOWER_BOUND;
        } else if (value <= alpha) {
            type = TranspositionType.UPPER_BOUND;
        } else {
            type = TranspositionType.EXACT;
        }

        entry.setHash(hash);
        entry.setSearchDepth(searchDepth);
        entry.setBestMoveAndValue(bestMoveAndValue);
        entry.setType(type);

        tTable.write(hash, entry);
    }
}
