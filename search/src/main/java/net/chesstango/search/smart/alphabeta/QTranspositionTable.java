package net.chesstango.search.smart.alphabeta;

import net.chesstango.board.Game;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.SearchContext;

import java.util.Map;

import static net.chesstango.search.smart.SearchContext.TableEntry;

/**
 * @author Mauricio Coria
 */
public class QTranspositionTable implements AlphaBetaFilter {

    private AlphaBetaFilter next;

    private Map<Long, TableEntry> qMaxMap;
    private Map<Long, TableEntry> qMinMap;
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
        return process(currentPly, alpha, beta, true);
    }

    @Override
    public long minimize(final int currentPly, final int alpha, final int beta) {
        return process(currentPly, alpha, beta, false);
    }

    @Override
    public void stopSearching() {
    }

    public void setNext(AlphaBetaFilter next) {
        this.next = next;
    }


    private long process(int currentPly, int alpha, int beta, boolean maximize) {
        if(game.getStatus().isInProgress()) {
            long hash = game.getChessPosition().getPositionHash();

            SearchContext.TableEntry entry = maximize ? qMaxMap.get(hash) : qMinMap.get(hash);

            if (entry == null) {
                entry = searchAndUpdate(new TableEntry(), currentPly, alpha, beta, maximize);

                if(maximize) {
                    qMaxMap.put(hash, entry);
                } else {
                    qMinMap.put(hash, entry);
                }
            } else {
                // Es un valor exacto
                if (entry.exact) {
                    entry = entry;
                } else {
                    if(maximize){
                        if(entry.value >= beta){
                            entry = entry;
                        } else {
                            entry = searchAndUpdate(entry, currentPly, alpha, beta, true);
                        }
                    } else {
                        if(entry.value <= alpha){
                            entry = entry;
                        } else {
                            entry = searchAndUpdate(entry, currentPly, alpha, beta, false);
                        }
                    }
                }
            }

            return entry.bestMoveAndValue;
        }

        return maximize ? next.maximize(currentPly, alpha, beta) : next.minimize(currentPly, alpha, beta);
    }

    private TableEntry searchAndUpdate(TableEntry entry, int currentPly, int alpha, int beta, boolean maximize) {
        long bestMoveAndValue = maximize ? next.maximize(currentPly, alpha, beta) : next.minimize(currentPly, alpha, beta);
        entry.bestMoveAndValue = bestMoveAndValue;
        entry.searchDepth = currentPly;
        entry.alpha = alpha;
        entry.beta = beta;
        entry.value = (int) (0b00000000_00000000_00000000_00000000_00000000_11111111_11111111_11111111_11111111L & bestMoveAndValue);
        entry.exact =  entry.value > alpha && entry.value < beta;
        return entry;
    }
}
