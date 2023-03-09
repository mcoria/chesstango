package net.chesstango.search.smartnegamax;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.evaluation.GameEvaluator;

public class NegaMaxEvaluatorWrapper implements GameEvaluator {

    private final GameEvaluator imp;

    public NegaMaxEvaluatorWrapper(GameEvaluator gameEvaluator) {
        imp = gameEvaluator;
    }


    @Override
    public int evaluate(Game game) {
        return Color.WHITE.equals(game.getChessPosition().getCurrentTurn()) ? imp.evaluate(game) : -imp.evaluate(game);
    }


}
