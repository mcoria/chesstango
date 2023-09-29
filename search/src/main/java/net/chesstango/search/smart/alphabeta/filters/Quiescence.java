package net.chesstango.search.smart.alphabeta.filters;

import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MovePromotion;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.StopSearchingException;
import net.chesstango.search.smart.SearchContext;
import net.chesstango.search.smart.sorters.MoveSorter;
import net.chesstango.search.smart.transposition.TranspositionEntry;
import net.chesstango.search.smart.transposition.TranspositionBound;

import java.util.Iterator;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class Quiescence implements AlphaBetaFilter {
    private volatile boolean keepProcessing;
    private AlphaBetaFilter next;
    private MoveSorter moveSorter;
    private GameEvaluator evaluator;
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
        this.keepProcessing = true;
    }

    @Override
    public void afterSearchByDepth(SearchMoveResult result) {
    }

    @Override
    public void reset() {

    }

    @Override
    public long maximize(final int currentPly, final int alpha, final int beta) {
        if (!keepProcessing) {
            throw new StopSearchingException();
        }

        int maxValue = evaluator.evaluate(game);

        if (maxValue >= beta) {
            return TranspositionEntry.encode(TranspositionBound.LOWER_BOUND, null, maxValue);
        }

        Move bestMove = null;
        boolean search = true;
        TranspositionBound bound = TranspositionBound.EXACT;

        List<Move> sortedMoves = moveSorter.getSortedMoves();
        Iterator<Move> moveIterator = sortedMoves.iterator();
        while (moveIterator.hasNext() && search) {
            Move move = moveIterator.next();

            if (isNotQuiet(move)) {
                game = game.executeMove(move);

                long bestMoveAndValue = next.minimize(currentPly + 1, Math.max(maxValue, alpha), beta);
                int currentValue = TranspositionEntry.decodeValue(bestMoveAndValue);

                if (currentValue > maxValue) {
                    maxValue = currentValue;
                    bestMove = move;
                    if (maxValue >= beta) {
                        search = false;
                        bound = TranspositionBound.LOWER_BOUND;
                    }
                }

                game = game.undoMove();
            }
        }
        return TranspositionEntry.encode(bound, bestMove, maxValue);
    }

    @Override
    public long minimize(final int currentPly, final int alpha, final int beta) {
        if (!keepProcessing) {
            throw new StopSearchingException();
        }

        int minValue = evaluator.evaluate(game);

        if (minValue <= alpha) {
            return TranspositionEntry.encode(TranspositionBound.UPPER_BOUND,null, minValue);
        }

        Move bestMove = null;
        boolean search = true;
        TranspositionBound bound = TranspositionBound.EXACT;

        List<Move> sortedMoves = moveSorter.getSortedMoves();
        Iterator<Move> moveIterator = sortedMoves.iterator();
        while (moveIterator.hasNext() && search) {
            Move move = moveIterator.next();

            if (isNotQuiet(move)) {
                game = game.executeMove(move);

                long bestMoveAndValue = next.maximize(currentPly + 1, alpha, Math.min(minValue, beta));
                int currentValue = TranspositionEntry.decodeValue(bestMoveAndValue);

                if (currentValue < minValue) {
                    minValue = currentValue;
                    bestMove = move;
                    if (minValue <= alpha) {
                        search = false;
                        bound = TranspositionBound.UPPER_BOUND;
                    }
                }

                game = game.undoMove();
            }
        }
        return TranspositionEntry.encode(bound, bestMove, minValue);
    }

    public static boolean isNotQuiet(Move move) {
        return move.getTo().getPiece() != null ||  // Captura
                move.getFrom().getPiece().isPawn() && move.getFrom().getSquare().getFile() != move.getTo().getSquare().getFile() || // Captura de peon
                move instanceof MovePromotion;     // Promocion
    }

    public void stopSearching() {
        this.keepProcessing = false;
    }

    public void setNext(AlphaBetaFilter next) {
        this.next = next;
    }

    public void setMoveSorter(MoveSorter moveSorter) {
        this.moveSorter = moveSorter;
    }

    public void setGameEvaluator(GameEvaluator evaluator) {
        this.evaluator = evaluator;
    }

}
