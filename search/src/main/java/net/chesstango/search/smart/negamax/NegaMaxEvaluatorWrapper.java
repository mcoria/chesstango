package net.chesstango.search.smart.negamax;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.evaluation.Evaluator;

/**
 * @author Mauricio Coria
 */
public class NegaMaxEvaluatorWrapper implements Evaluator {

    private final Evaluator imp;

    private Game game;

    public NegaMaxEvaluatorWrapper(Evaluator evaluator) {
        imp = evaluator;
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
