package net.chesstango.search.smart.negamax;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.evaluation.GameEvaluator;

/**
 * @author Mauricio Coria
 */
public class NegaMaxEvaluatorWrapper implements GameEvaluator {

    private final GameEvaluator imp;

    private Game game;

    public NegaMaxEvaluatorWrapper(GameEvaluator gameEvaluator) {
        imp = gameEvaluator;
    }


    @Override
    public int evaluate() {
        return Color.WHITE.equals(game.getChessPosition().getCurrentTurn()) ? imp.evaluate() : -imp.evaluate();
    }

    @Override
    public void setGame(Game game) {
        this.game = game;
        imp.setGame(game);
    }


}
