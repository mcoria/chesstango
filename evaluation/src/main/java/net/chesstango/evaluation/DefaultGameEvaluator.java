package net.chesstango.evaluation;

import net.chesstango.board.Game;
import net.chesstango.board.Piece;
import net.chesstango.evaluation.imp.GameEvaluatorImp02;

public class DefaultGameEvaluator implements GameEvaluator {

    private GameEvaluator imp = new GameEvaluatorImp02();

    @Override
    public int getPieceValue(Game game, Piece piece) {
        return imp.getPieceValue(game, piece);
    }

    @Override
    public int evaluate(Game game) {
        return imp.evaluate(game);
    }
}
