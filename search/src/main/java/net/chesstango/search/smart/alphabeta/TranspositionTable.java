package net.chesstango.search.smart.alphabeta;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.smart.SearchContext;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;

import static net.chesstango.search.smart.SearchContext.TableEntry;

/**
 * @author Mauricio Coria
 */
public class TranspositionTable implements AlphaBetaFilter {

    private AlphaBetaFilter next;

    private Map<Long, TableEntry> maxMap;

    private Map<Long, TableEntry> minMap;

    private  int maxPly;
    private Game game;

    @Override
    public void init(Game game, SearchContext context) {
        this.game = game;
        this.maxPly = context.getMaxPly();
        this.maxMap = context.getMaxMap();
        this.minMap = context.getMinMap();
    }

    @Override
    public long maximize(int currentPly, int alpha, int beta) {
        long hash = game.getChessPosition().getPositionHash();

        SearchContext.TableEntry entry = maxMap.get(hash);

        int searchDepth = maxPly - currentPly;

        if (entry == null || entry != null && searchDepth > entry.searchDepth ) {
            entry = new TableEntry();
            entry.searchDepth = searchDepth;
            entry.bestMoveAndValue = next.maximize(currentPly, alpha, beta);

            maxMap.put(hash, entry);
        }
        return entry.bestMoveAndValue;
    }

    @Override
    public long minimize(int currentPly, int alpha, int beta) {
        long hash = game.getChessPosition().getPositionHash();

        TableEntry entry = minMap.get(hash);

        int searchDepth = maxPly - currentPly;

        if (entry == null || entry != null && searchDepth > entry.searchDepth ) {
            entry = new TableEntry();
            entry.searchDepth = searchDepth;
            entry.bestMoveAndValue = next.minimize(currentPly, alpha, beta);

            minMap.put(hash, entry);
        }
        return entry.bestMoveAndValue;
    }

    @Override
    public void stopSearching() {
    }

    public void setNext(AlphaBetaFilter next) {
        this.next = next;
    }
}
