package net.chesstango.evaluation.imp;

import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.board.Color;
import net.chesstango.board.Game;

/**
 * @author Mauricio Coria
 */
public class GameEvaluatorBasic implements GameEvaluator {

    @Override
    public int evaluate(final Game game) {
        int evaluation = 0;
        switch (game.getStatus()) {
            case MATE:
            case DRAW:
                evaluation = GameEvaluator.evaluateFinalStatus(game);
                break;
            case CHECK:
            case NO_CHECK:
                evaluation += 100 * GameEvaluator.evaluateByMaterial(game);
                evaluation += Color.WHITE.equals(game.getChessPosition().getCurrentTurn()) ? +game.getPossibleMoves().size() : -game.getPossibleMoves().size();
        }
        return evaluation;
    }

}