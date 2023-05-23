package net.chesstango.search.smart.alphabeta.filters;

import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.StopSearchingException;
import net.chesstango.search.smart.BinaryUtils;
import net.chesstango.search.smart.SearchContext;
import net.chesstango.search.smart.sorters.MoveSorter;

import java.util.Iterator;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class AlphaBeta implements AlphaBetaFilter {
    private volatile boolean keepProcessing;

    private GameEvaluator evaluator;

    private AlphaBetaFilter next;

    private AlphaBetaFilter quiescence;

    private MoveSorter moveSorter;

    private int maxPly;
    private Game game;

    @Override
    public void init(SearchContext context) {
        this.game = context.getGame();
        this.maxPly = context.getMaxPly();
        this.keepProcessing = true;
    }

    @Override
    public void close(SearchMoveResult result) {
    }

    @Override
    public long maximize(final int currentPly, final int alpha, final int beta) {
        if (!keepProcessing) {
            throw new StopSearchingException();
        }
        if (!game.getStatus().isInProgress()) {
            return BinaryUtils.encodedMoveAndValue((short) 0, evaluator.evaluate(game));
        }
        if (currentPly == maxPly) {
            return quiescence.maximize(currentPly, alpha, beta);
        } else {
            Move bestMove = null;
            boolean search = true;
            int maxValue = GameEvaluator.INFINITE_NEGATIVE;

            List<Move> sortedMoves = moveSorter.getSortedMoves();
            Iterator<Move> moveIterator = sortedMoves.iterator();
            while (moveIterator.hasNext() && search) {
                Move move = moveIterator.next();

                game = game.executeMove(move);

                long bestMoveAndValue = next.minimize(currentPly + 1, Math.max(maxValue, alpha), beta);
                int currentValue = BinaryUtils.decodeValue(bestMoveAndValue);
                if (currentValue > maxValue) {
                    maxValue = currentValue;
                    bestMove = move;
                    if (maxValue >= beta) {
                        search = false;
                    }
                }

                game = game.undoMove();
            }
            return BinaryUtils.encodedMoveAndValue(bestMove.binaryEncoding(), maxValue);
        }
    }

    @Override
    public long minimize(final int currentPly, final int alpha, final int beta) {
        if (!keepProcessing) {
            throw new StopSearchingException();
        }
        if (!game.getStatus().isInProgress()) {
            return BinaryUtils.encodedMoveAndValue((short) 0, evaluator.evaluate(game));
        }
        if (currentPly == maxPly) {
            return quiescence.minimize(currentPly, alpha, beta);
        } else {
            Move bestMove = null;
            boolean search = true;
            int minValue = GameEvaluator.INFINITE_POSITIVE;

            List<Move> sortedMoves = moveSorter.getSortedMoves();
            Iterator<Move> moveIterator = sortedMoves.iterator();
            while (moveIterator.hasNext() && search) {
                Move move = moveIterator.next();

                game = game.executeMove(move);

                long bestMoveAndValue = next.maximize(currentPly + 1, alpha, Math.min(minValue, beta));
                int currentValue = BinaryUtils.decodeValue(bestMoveAndValue);
                if (currentValue < minValue) {
                    minValue = currentValue;
                    bestMove = move;
                    if (minValue <= alpha) {
                        search = false;
                    }
                }

                game = game.undoMove();
            }
            return BinaryUtils.encodedMoveAndValue(bestMove.binaryEncoding(), minValue);
        }
    }

    public void stopSearching() {
        this.keepProcessing = false;
    }

    public void setMoveSorter(MoveSorter moveSorter) {
        this.moveSorter = moveSorter;
    }

    public void setQuiescence(AlphaBetaFilter quiescence) {
        this.quiescence = quiescence;
    }

    public void setGameEvaluator(GameEvaluator evaluator) {
        this.evaluator = evaluator;
    }

    public void setNext(AlphaBetaFilter next) {
        this.next = next;
    }

}
