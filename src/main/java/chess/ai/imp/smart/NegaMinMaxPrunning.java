/**
 *
 */
package chess.ai.imp.smart;

import chess.board.Color;
import chess.board.Game;
import chess.board.moves.Move;
import chess.board.moves.containers.MoveContainerReader;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class NegaMinMaxPrunning extends AbstractSmart {
    private static final int DEFAULT_MAXLEVEL = 7;
    private final int maxLevel;
    private final GameEvaluator evaluator = new GameEvaluator();
    private Game game = null;
    private boolean keepProcessing;

    public NegaMinMaxPrunning() {
        this(DEFAULT_MAXLEVEL);
    }

    public NegaMinMaxPrunning(int level) {
        this.maxLevel = level;
    }

    @Override
    public Move findBestMove(final Game game) {
        this.game = game;
        this.keepProcessing = true;

        final List<Move> possibleMoves = new ArrayList<Move>();


        int alpha =  GameEvaluator.INFINITE_NEGATIVE;
        final int beta = GameEvaluator.INFINITE_POSITIVE;

        boolean search = true;
        Iterator<Move> possibleMovesIterator = game.getPossibleMoves().iterator();
        while (possibleMovesIterator.hasNext() && search && keepProcessing) {
            Move move = possibleMovesIterator.next();
            game.executeMove(move);

            int currentValue = - negaMinMax(maxLevel - 1, -beta, -alpha);

            if (currentValue > alpha) {
                alpha = currentValue;
                possibleMoves.clear();
                possibleMoves.add(move);
                if (alpha == GameEvaluator.INFINITE_POSITIVE) {
                    search = false;
                }
            } else if (currentValue == alpha) {
                possibleMoves.add(move);
            }

            game.undoMove();
        }
        return selectMove(possibleMoves);
    }

    private Integer negaMinMax(final int currentLevel, int alpha, final int beta) {
        MoveContainerReader possibleMoves = game.getPossibleMoves();
        if (currentLevel == 0 || possibleMoves.size() == 0) {
            return evaluator.evaluate(game);
        } else {
            boolean search = true;
            int maxValue = GameEvaluator.INFINITE_NEGATIVE;
            Iterator<Move> possibleMovesIterator = possibleMoves.iterator();
            while (possibleMovesIterator.hasNext() && search && keepProcessing) {
                Move move = possibleMovesIterator.next();
                game.executeMove(move);

                maxValue = Math.max(maxValue, - negaMinMax(currentLevel - 1, -beta, -alpha));

                alpha = Math.max(maxValue, alpha);

                if (alpha >= beta) {
                    search = false;
                }

                game.undoMove();
            }
            return maxValue;
        }
    }

    @Override
    public void stopProcessing() {
        keepProcessing = false;
    }

}
