package net.chesstango.search.smart.alphabeta;

import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.MoveSorter;
import net.chesstango.search.smart.SearchContext;

import java.util.Iterator;
import java.util.List;
import java.util.Queue;

/**
 * @author Mauricio Coria
 */
public class AlphaBeta implements AlphaBetaFilter {
    protected boolean keepProcessing;

    private GameEvaluator evaluator;

    private AlphaBetaFilter next;

    private AlphaBetaFilter quiescence;

    private MoveSorter moveSorter;

    private int maxPly;
    private Game game;

    @Override
    public void init(Game game, SearchContext context) {
        this.game = game;
        this.maxPly = context.getMaxPly();
        this.keepProcessing = true;
    }

    @Override
    public void close(SearchMoveResult result) {

    }

    @Override
    public long minimize(final int currentPly, final int alpha, final int beta) {
        if (!game.getStatus().isInProgress()) {
            return encodedMoveAndValue((short) 0, evaluator.evaluate(game));
        }
        if (currentPly == maxPly) {
            return quiescence.minimize(currentPly, alpha, beta);
        } else {
            Move bestMove = null;
            boolean search = true;
            int minValue = GameEvaluator.INFINITE_POSITIVE;

            List<Move> sortedMoves = moveSorter.sortMoves(game.getPossibleMoves());
            Iterator<Move> moveIterator = sortedMoves.iterator();
            while (moveIterator.hasNext() && search && keepProcessing) {
                Move move = moveIterator.next();

                game = game.executeMove(move);

                long bestMoveAndValue = next.maximize(currentPly + 1, alpha, Math.min(minValue, beta));

                int currentValue = (int) (0b00000000_00000000_00000000_00000000_00000000_11111111_11111111_11111111_11111111L & bestMoveAndValue);

                if (currentValue < minValue) {
                    minValue = currentValue;
                    bestMove = move;
                    if (alpha >= minValue) {
                        search = false;
                    }
                }

                game = game.undoMove();
            }
            return encodedMoveAndValue(bestMove.binaryEncoding(), minValue);
        }
    }

    @Override
    public long maximize(final int currentPly, final int alpha, final int beta) {
        if (!game.getStatus().isInProgress()) {
            return encodedMoveAndValue((short) 0, evaluator.evaluate(game));
        }
        if (currentPly == maxPly) {
            return quiescence.maximize(currentPly, alpha, beta);
        } else {
            Move bestMove = null;
            boolean search = true;
            int maxValue = GameEvaluator.INFINITE_NEGATIVE;

            List<Move> sortedMoves = moveSorter.sortMoves(game.getPossibleMoves());
            Iterator<Move> moveIterator = sortedMoves.iterator();
            while (moveIterator.hasNext() && search && keepProcessing) {
                Move move = moveIterator.next();

                game = game.executeMove(move);

                long bestMoveAndValue = next.minimize(currentPly + 1, Math.max(maxValue, alpha), beta);

                int currentValue = (int) (0b00000000_00000000_00000000_00000000_00000000_11111111_11111111_11111111_11111111L & bestMoveAndValue);

                if (currentValue > maxValue) {
                    maxValue = currentValue;
                    bestMove = move;
                    if (maxValue >= beta) {
                        search = false;
                    }
                }

                game = game.undoMove();
            }
            return encodedMoveAndValue(bestMove.binaryEncoding(), maxValue);
        }
    }

    @Override
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

    private static long encodedMoveAndValue(short move, int value){
        long encodedMoveLng = ((long) move) << 32;

        long encodedValueLng = 0b00000000_00000000_00000000_00000000_00000000_11111111_11111111_11111111_11111111L & value;

        return encodedValueLng | encodedMoveLng;
    }
}
