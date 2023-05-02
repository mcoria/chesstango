package net.chesstango.search.smart.alphabeta;

import net.chesstango.board.Game;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MovePromotion;
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
public class Quiescence implements AlphaBetaFilter {
    private boolean keepProcessing;

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
    public long minimize(final int currentPly, final int alpha, final int beta) {
        int minValue = evaluator.evaluate(game);

        if (alpha >= minValue) {
            return encodedMoveAndValue((short) 0, minValue);
        }

        Move bestMove = null;
        boolean search = true;

        List<Move> sortedMoves = moveSorter.sortMoves(game.getPossibleMoves());
        Iterator<Move> moveIterator = sortedMoves.iterator();
        while (moveIterator.hasNext() && search && keepProcessing) {
            Move move = moveIterator.next();

            if (isNotQuiet(move)) {
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
        }
        return encodedMoveAndValue(bestMove == null ? (short) 0 : bestMove.binaryEncoding(), minValue);
    }

    @Override
    public long maximize(final int currentPly, final int alpha, final int beta) {
        int maxValue = evaluator.evaluate(game);

        if (maxValue >= beta) {
            return encodedMoveAndValue((short) 0, maxValue);
        }

        Move bestMove = null;
        boolean search = true;

        List<Move> sortedMoves = moveSorter.sortMoves(game.getPossibleMoves());
        Iterator<Move> moveIterator = sortedMoves.iterator();
        while (moveIterator.hasNext() && search && keepProcessing) {
            Move move = moveIterator.next();

            if (isNotQuiet(move)) {
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
        }
        return encodedMoveAndValue(bestMove == null ? (short) 0 : bestMove.binaryEncoding(), maxValue);
    }

    protected boolean isNotQuiet(Move move) {
        return move.getTo().getPiece() != null ||  // Captura
                move.getFrom().getPiece().isPawn() && !(Cardinal.Sur.equals(move.getMoveDirection()) || Cardinal.Norte.equals(move.getMoveDirection()) ) || // Captura de peon
                move instanceof MovePromotion;     // Promocion
    }

    @Override
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

    private static long encodedMoveAndValue(short move, int value){
        long encodedMoveLng = ((long) move) << 32;

        long encodedValueLng = 0b00000000_00000000_00000000_00000000_00000000_11111111_11111111_11111111_11111111L & value;

        return encodedValueLng | encodedMoveLng;
    }
}
