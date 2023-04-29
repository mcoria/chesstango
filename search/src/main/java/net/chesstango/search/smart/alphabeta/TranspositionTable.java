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

    private final Deque<TableEntry> stackTableEntry = new ArrayDeque<>();

    private  int maxPly;

    @Override
    public void init(Game game, SearchContext context) {
        maxPly = context.getMaxPly();
        maxMap = context.getMaxMap();
        minMap = context.getMinMap();

        long hash = game.getChessPosition().getPositionHash();

        TableEntry entry = new TableEntry();

        entry.searchDepth = maxPly;
        if(Color.WHITE.equals( game.getChessPosition().getCurrentTurn() )) {
            entry.evaluation = GameEvaluator.INFINITE_NEGATIVE;
            maxMap.put(hash, entry);
        } else {
            entry.evaluation = GameEvaluator.INFINITE_POSITIVE;
            minMap.put(hash, entry);
        }

        stackTableEntry.push(entry);
    }

    @Override
    public long maximize(Game game, int currentPly, int alpha, int beta) {
        TableEntry parentElement = stackTableEntry.peekFirst();

        long hash = game.getChessPosition().getPositionHash();

        SearchContext.TableEntry entry = maxMap.get(hash);

        int searchDepth = maxPly - currentPly;

        if (entry == null || entry != null && searchDepth > entry.searchDepth ) {
            entry = new TableEntry();
            entry.searchDepth = searchDepth;
            entry.evaluation = GameEvaluator.INFINITE_NEGATIVE;

            stackTableEntry.push(entry);
            entry.evaluation = (int) next.maximize(game, currentPly, alpha, beta);
            stackTableEntry.pop();

            maxMap.put(hash, entry);
        }

        if(parentElement.evaluation > entry.evaluation){
            Move move = game.getState().getPreviosState().getSelectedMove();
            parentElement.evaluation = entry.evaluation;
            parentElement.bestMove = move;
        }

        return entry.evaluation;
    }

    @Override
    public long minimize(Game game, int currentPly, int alpha, int beta) {
        TableEntry parentElement = stackTableEntry.peekFirst();

        long hash = game.getChessPosition().getPositionHash();

        TableEntry entry = minMap.get(hash);

        int searchDepth = maxPly - currentPly;

        if (entry == null || entry != null && searchDepth > entry.searchDepth ) {
            entry = new TableEntry();
            entry.searchDepth = searchDepth;
            entry.evaluation = GameEvaluator.INFINITE_POSITIVE;

            stackTableEntry.push(entry);
            entry.evaluation = (int) next.minimize(game, currentPly, alpha, beta);
            stackTableEntry.pop();

            minMap.put(hash, entry);
        }

        if(parentElement.evaluation < entry.evaluation){
            Move move = game.getState().getPreviosState().getSelectedMove();
            parentElement.evaluation = entry.evaluation;
            parentElement.bestMove = move;
        }

        return entry.evaluation;
    }

    @Override
    public void stopSearching() {
    }

    public void setNext(AlphaBetaFilter next) {
        this.next = next;
    }
}
