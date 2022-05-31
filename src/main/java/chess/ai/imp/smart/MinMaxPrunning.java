/**
 *
 */
package chess.ai.imp.smart;

import java.util.*;

import chess.board.Color;
import chess.board.Game;
import chess.board.moves.Move;
import chess.board.moves.containers.MoveContainerReader;

/**
 * @author Mauricio Coria
 *
 */
public class MinMaxPrunning extends AbstractSmart {

    private static final int DEFAULT_MAXLEVEL = 5;

    private final int maxLevel;

    private final GameEvaluator evaluator = new GameEvaluator();

	private Game game = null;

    public  MinMaxPrunning(){
        this(DEFAULT_MAXLEVEL);
    }

    public  MinMaxPrunning(int level){
        this.maxLevel = level;
    }

    @Override
    public Move findBestMove(final Game game) {
		this.game = game;
        final List<Move> possibleMoves = new ArrayList<Move>();
        final boolean minOrMax = Color.WHITE.equals(game.getChessPositionReader().getCurrentTurn()) ? false : true;

        int bestValue = minOrMax ? Integer.MAX_VALUE : Integer.MIN_VALUE;
        boolean search = true;
        Iterator<Move> possibleMovesIterator = game.getPossibleMoves().iterator();
        while (possibleMovesIterator.hasNext() && search) {
            Move move = possibleMovesIterator.next();
            game.executeMove(move);

            int currentValue = minOrMax ? maximize(maxLevel - 1, Integer.MIN_VALUE, bestValue) :
                    						minimize(maxLevel - 1, bestValue, Integer.MAX_VALUE);

            if (minOrMax && currentValue < bestValue || !minOrMax && currentValue > bestValue) {
                bestValue = currentValue;
                possibleMoves.clear();
                if (minOrMax && bestValue == Integer.MIN_VALUE || !minOrMax && bestValue == Integer.MAX_VALUE) {
                    search = false;
                }
            }

            if (currentValue == bestValue) {
                possibleMoves.add(move);
            }
            game.undoMove();
        }
        return selectMove(possibleMoves);
    }

    private int minimize(final int currentLevel, final int alpha, int beta) {
        MoveContainerReader possibleMoves = game.getPossibleMoves();
        if (currentLevel == 0 || possibleMoves.size() == 0) {
            return evaluator.evaluate(game, maxLevel - currentLevel);
        } else {
            int minValue = Integer.MAX_VALUE;
            boolean search = true;
            Iterator<Move> possibleMovesIterator = possibleMoves.iterator();
            while (possibleMovesIterator.hasNext() && search) {
                Move move = possibleMovesIterator.next();
                game.executeMove(move);

                minValue = Math.min(minValue, maximize(currentLevel - 1, alpha, beta));
                beta = Math.min(beta, minValue);

                if (alpha >= beta) {
                    search = false;
                }
                game.undoMove();
            }
            return minValue;
        }
    }

    private int maximize(final int currentLevel, int alpha, final int beta) {
        MoveContainerReader possibleMoves = game.getPossibleMoves();
        if (currentLevel == 0 || possibleMoves.size() == 0) {
            return evaluator.evaluate(game, maxLevel - currentLevel);
        } else {
            int maxValue = Integer.MIN_VALUE;
            boolean search = true;
            Iterator<Move> possibleMovesIterator = possibleMoves.iterator();
            while (possibleMovesIterator.hasNext() && search) {
                Move move = possibleMovesIterator.next();
                game.executeMove(move);

                maxValue = Math.max(maxValue, minimize(currentLevel - 1, alpha, beta));
                alpha = Math.max(alpha, maxValue);

                if (alpha >= beta) {
                    search = false;
                }
                game.undoMove();
            }
            return maxValue;
        }
    }


}
