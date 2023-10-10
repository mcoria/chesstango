package net.chesstango.search.smart.alphabeta.filters.once;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.SearchContext;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFilter;
import net.chesstango.search.smart.sorters.MoveComparator;
import net.chesstango.search.smart.transposition.TranspositionEntry;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * @author Mauricio Coria
 */
public class AlphaBetaFirst implements AlphaBetaFilter {
    private static final MoveComparator moveComparator = new MoveComparator();

    @Setter
    private AlphaBetaFilter next;

    @Getter
    private Move currentMove;
    private Game game;
    private List<Move> sortedMoves;

    @Override
    public long maximize(int currentPly, int alpha, int beta) {
        boolean search = true;
        int maxValue = GameEvaluator.INFINITE_NEGATIVE;
        Move bestMove = null;

        Iterator<Move> moveIterator = sortedMoves.iterator();
        while (moveIterator.hasNext() && search) {
            currentMove = moveIterator.next();
            game = game.executeMove(currentMove);
            long bestMoveAndValue = next.minimize(1, Math.max(maxValue, alpha), beta);
            int currentValue = TranspositionEntry.decodeValue(bestMoveAndValue);
            if (currentValue > maxValue) {
                maxValue = currentValue;
                bestMove = currentMove;
                if (maxValue >= beta) {
                    search = false;
                }
            }
            game = game.undoMove();
        }

        return TranspositionEntry.encode(bestMove, null, maxValue);
    }

    @Override
    public long minimize(int currentPly, int alpha, int beta) {
        boolean search = true;
        int minValue = GameEvaluator.INFINITE_POSITIVE;
        Move bestMove = null;

        Iterator<Move> moveIterator = sortedMoves.iterator();
        while (moveIterator.hasNext() && search) {
            currentMove = moveIterator.next();
            game = game.executeMove(currentMove);
            long bestMoveAndValue = next.maximize(1, alpha, Math.min(minValue, beta));
            int currentValue = TranspositionEntry.decodeValue(bestMoveAndValue);
            if (currentValue < minValue) {
                minValue = currentValue;
                bestMove = currentMove;
                if (minValue <= alpha) {
                    search = false;
                }
            }
            game = game.undoMove();
        }
        return TranspositionEntry.encode(bestMove, null, minValue);
    }


    @Override
    public void stopSearching() {
    }

    @Override
    public void beforeSearch(Game game, int maxDepth) {
        this.game = game;
    }

    @Override
    public void afterSearch(SearchMoveResult result) {
    }

    @Override
    public void reset() {
    }

    @Override
    public void beforeSearchByDepth(SearchContext context) {
        currentMove = null;
        sortedMoves = createSortedMoves(context.getLastBestMove());
    }

    @Override
    public void afterSearchByDepth(SearchMoveResult result) {
    }


    private List<Move> createSortedMoves(Move lastBestMove) {
        List<Move> sortedMoves = new LinkedList<>();
        if (lastBestMove != null) {
            sortedMoves.add(lastBestMove);
        }

        List<Move> unsortedMoveList = new LinkedList<>();
        for (Move move : game.getPossibleMoves()) {
            if (!Objects.equals(move, lastBestMove)) {
                unsortedMoveList.add(move);
            }
        }

        unsortedMoveList.sort(moveComparator.reversed());

        sortedMoves.addAll(unsortedMoveList);

        return sortedMoves;
    }


}
