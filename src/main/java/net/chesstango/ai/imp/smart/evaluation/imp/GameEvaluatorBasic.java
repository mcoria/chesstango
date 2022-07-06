package net.chesstango.ai.imp.smart.evaluation.imp;

import net.chesstango.ai.imp.smart.evaluation.GameEvaluator;
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
                // If white is on mate then evaluation is INFINITE_NEGATIVE
                evaluation = Color.WHITE.equals(game.getChessPosition().getCurrentTurn()) ? WHITE_LOST : BLACK_LOST;
                break;
            case DRAW:
                evaluation = 0;
                break;
            case CHECK:
            case NO_CHECK:
                evaluation += Color.WHITE.equals(game.getChessPosition().getCurrentTurn()) ? +game.getPossibleMoves().size() : -game.getPossibleMoves().size();
        }
        return evaluation;
    }

}
