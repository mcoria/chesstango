package net.chesstango.evaluation.evaluators;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.evaluation.Evaluator;

/**
 * @author Mauricio Coria
 */
public abstract class AbstractEvaluator implements Evaluator {

    protected Game game;

    protected abstract int evaluateNonFinalStatus();

    @Override
    public int evaluate() {
        if (game.getStatus().isFinalStatus()) {
            return evaluateFinalStatus();
        } else {
            return evaluateNonFinalStatus();
        }
    }

    @Override
    public void setGame(Game game) {
        this.game = game;
    }

    protected int evaluateFinalStatus() {
        return switch (game.getStatus()) {
            case MATE -> Color.WHITE.equals(game.getPosition().getCurrentTurn()) ? WHITE_LOST : BLACK_LOST;
            case STALEMATE, DRAW_BY_FIFTY_RULE, DRAW_BY_FOLD_REPETITION -> 0;
            default -> throw new RuntimeException("Game is still in progress");
        };
    }

}
