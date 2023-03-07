package net.chesstango.evaluation.imp;

import net.chesstango.board.Piece;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.board.Color;
import net.chesstango.board.Game;

/**
 * @author Mauricio Coria
 */
public class GameEvaluatorByMaterialAndMoves implements GameEvaluator {
    private static final int FACTOR_MATERIAL_DEFAULT = 600;
    private static final int FACTOR_MOVE_DEFAULT = 400;
    private final int material;
    private final int legalmoves;

    public GameEvaluatorByMaterialAndMoves() {
        this(FACTOR_MATERIAL_DEFAULT, FACTOR_MOVE_DEFAULT);
    }

    public GameEvaluatorByMaterialAndMoves(int material, int legalmoves) {
        this.material = material;
        this.legalmoves = legalmoves;
    }

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
                evaluation += material * evaluateByMaterial(game);
                evaluation += legalmoves * (Color.WHITE.equals(game.getChessPosition().getCurrentTurn()) ? +game.getPossibleMoves().size() : -game.getPossibleMoves().size());
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
