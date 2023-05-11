package net.chesstango.search.smart.alphabeta.filters;

import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MovePromotion;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.BinaryUtils;
import net.chesstango.search.smart.SearchContext;
import net.chesstango.search.smart.StopProcessingException;
import net.chesstango.search.smart.sorters.MoveSorter;

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
    public void init(Game game, SearchContext context) {
        this.game = game;
        this.keepProcessing = true;
    }

    @Override
    public void close(SearchMoveResult result) {
    }

    @Override
    public long maximize(final int currentPly, final int alpha, final int beta) {
        if (!keepProcessing) {
            throw new StopProcessingException();
        }

        int maxValue = evaluator.evaluate(game);

        if (maxValue >= beta) {
            return BinaryUtils.encodedMoveAndValue((short) 0, maxValue);
        }

        Move bestMove = null;
        boolean search = true;

        List<Move> sortedMoves = moveSorter.getSortedMoves();
        Iterator<Move> moveIterator = sortedMoves.iterator();
        while (moveIterator.hasNext() && search) {
            Move move = moveIterator.next();

            if (isNotQuiet(move)) {
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
        }
        return BinaryUtils.encodedMoveAndValue(bestMove == null ? (short) 0 : bestMove.binaryEncoding(), maxValue);
    }

    @Override
    public long minimize(final int currentPly, final int alpha, final int beta) {
        if (!keepProcessing) {
            throw new StopProcessingException();
        }

        int minValue = evaluator.evaluate(game);

        if (minValue <= alpha) {
            return BinaryUtils.encodedMoveAndValue((short) 0, minValue);
        }

        Move bestMove = null;
        boolean search = true;

        List<Move> sortedMoves = moveSorter.getSortedMoves();
        Iterator<Move> moveIterator = sortedMoves.iterator();
        while (moveIterator.hasNext() && search) {
            Move move = moveIterator.next();

            if (isNotQuiet(move)) {
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
        }
        return BinaryUtils.encodedMoveAndValue(bestMove == null ? (short) 0 : bestMove.binaryEncoding(), minValue);
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
