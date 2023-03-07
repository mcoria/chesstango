package net.chesstango.evaluation.imp;

import net.chesstango.board.Game;
import net.chesstango.board.Piece;
import net.chesstango.evaluation.GameEvaluator;

/**
 * @author Mauricio Coria
 */
public class GameEvaluatorSimplifiedEvaluator implements GameEvaluator {

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
            case PAWN_WHITE -> 10;
            case PAWN_BLACK -> -10;
            case KNIGHT_WHITE -> 32;
            case KNIGHT_BLACK -> -32;
            case BISHOP_WHITE -> 33;
            case BISHOP_BLACK -> -33;
            case ROOK_WHITE -> 50;
            case ROOK_BLACK -> -50;
            case QUEEN_WHITE -> 90;
            case QUEEN_BLACK -> -90;
            case KING_WHITE -> 2000;
            case KING_BLACK -> -2000;
        };
    }

    protected static int[] PAWN_WHITE_VALUES = {
            0, 0, 0, 0, 0, 0, 0, 0,                 // Rank 1
            5, 10, 10, -20, -20, 10, 10, 5,         // Rank 2
            5, -5, -10, 0, 0, -10, -5, 5,           // Rank 3
            0, 0, 0, 20, 20, 0, 0, 0,               // Rank 4
            5, 5, 10, 25, 25, 10, 5, 5,             // Rank 5
            10, 10, 20, 30, 30, 20, 10, 10,         // Rank 6
            50, 50, 50, 50, 50, 50, 50, 50,         // Rank 7
            0, 0, 0, 0, 0, 0, 0, 0                  // Rank 8
    };

    protected static int[] PAWN_BLACK_VALUES = {
            0, 0, 0, 0, 0, 0, 0, 0,                 // Rank 1
            -50, -50, -50, -50, -50, -50, -50, -50, // Rank 2
            -10, -10, -20, -30, -30, -20, -10, -10, // Rank 3
            -5, -5, -10, -25, -25, -10, -5, -5,     // Rank 4
            0, 0, 0, -20, -20, 0, 0, 0,             // Rank 5
            -5, 5, 10, 0, 0, 10, 5, -5,             // Rank 6
            -5, -10, -10, 20, 20, -10, -10, -5,     // Rank 7
            0, 0, 0, 0, 0, 0, 0, 0,                 // Rank 8
    };

}
