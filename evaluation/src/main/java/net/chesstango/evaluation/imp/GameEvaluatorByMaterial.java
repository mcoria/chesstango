package net.chesstango.evaluation.imp;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.Piece;
import net.chesstango.evaluation.GameEvaluator;

/**
 * @author Mauricio Coria
 */
public class GameEvaluatorByMaterial implements GameEvaluator {
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
                evaluation = evaluateByMaterial(game);
        }
        return evaluation;
    }

    @Override
    public int getPieceValue(Game game, Piece piece) {
        return switch (piece) {
            case PAWN_WHITE -> 1;
            case PAWN_BLACK -> -1;
            case KNIGHT_WHITE -> 3;
            case KNIGHT_BLACK -> -3;
            case BISHOP_WHITE -> 3;
            case BISHOP_BLACK -> -3;
            case ROOK_WHITE -> 5;
            case ROOK_BLACK -> -5;
            case QUEEN_WHITE -> 9;
            case QUEEN_BLACK -> -9;
            case KING_WHITE -> 10;
            case KING_BLACK -> -10;
        };
    }

}
