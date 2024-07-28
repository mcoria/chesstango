package net.chesstango.search.smart.alphabeta.filters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.features.transposition.TranspositionEntry;
import net.chesstango.search.smart.sorters.MoveSorter;

import java.util.Iterator;

/**
 * @author Mauricio Coria
 */
public class AlphaBetaRootExplorer implements AlphaBetaFilter, SearchByCycleListener {

    @Setter
    @Getter
    private AlphaBetaFilter next;

    @Setter
    @Getter
    private MoveSorter moveSorter;

    private Move exploreMove;

    protected Game game;


    @Override
    public void beforeSearch(SearchByCycleContext context) {
        this.game = context.getGame();
        this.exploreMove = context.getExploreMove();
    }

    @Override
    public long maximize(final int currentPly, final int alpha, final int beta) {
        boolean search = true;
        Move bestMove = exploreMove;
        int maxValue = exploreMove(currentPly, alpha, beta, next::minimize);

        Iterable<Move> sortedMoves = moveSorter.getOrderedMoves(currentPly);
        Iterator<Move> moveIterator = sortedMoves.iterator();
        while (moveIterator.hasNext() && search) {
            Move move = moveIterator.next();
            if (!move.equals(exploreMove)) {
                game = game.executeMove(move);
                long bestMoveAndValue = next.minimize(currentPly + 1, maxValue - 1, maxValue + 1);
                int currentValue = TranspositionEntry.decodeValue(bestMoveAndValue);
                if (currentValue > maxValue) {
                    maxValue = currentValue;
                    bestMove = move;
                    search = false;
                }
                game = game.undoMove();
            }
        }
        return TranspositionEntry.encode(bestMove, maxValue);
    }

    @Override
    public long minimize(final int currentPly, final int alpha, final int beta) {
        boolean search = true;
        Move bestMove = exploreMove;
        int minValue = exploreMove(currentPly, alpha, beta, next::maximize);

        Iterable<Move> sortedMoves = moveSorter.getOrderedMoves(currentPly);
        Iterator<Move> moveIterator = sortedMoves.iterator();
        while (moveIterator.hasNext() && search) {
            Move move = moveIterator.next();
            if (!move.equals(exploreMove)) {
                game = game.executeMove(move);
                long bestMoveAndValue = next.maximize(currentPly + 1, minValue - 1, minValue + 1);
                int currentValue = TranspositionEntry.decodeValue(bestMoveAndValue);
                if (currentValue < minValue) {
                    minValue = currentValue;
                    bestMove = move;
                    search = false;
                }
                game = game.undoMove();
            }
        }
        return TranspositionEntry.encode(bestMove, minValue);
    }


    public int exploreMove(final int currentPly, final int alpha, final int beta, AlphaBetaFunction alphaBetaFn) {
        game = game.executeMove(exploreMove);
        long bestMoveAndValue = alphaBetaFn.search(currentPly + 1, alpha, beta);
        int currentValue = TranspositionEntry.decodeValue(bestMoveAndValue);
        game = game.undoMove();
        return currentValue;
    }

}
