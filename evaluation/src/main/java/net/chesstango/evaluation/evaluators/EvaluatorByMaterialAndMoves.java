package net.chesstango.evaluation.evaluators;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;

/**
 * @author Mauricio Coria
 */
public class EvaluatorByMaterialAndMoves extends AbstractEvaluator {
    private static final int FACTOR_MATERIAL_DEFAULT = 600;
    private static final int FACTOR_MOVE_DEFAULT = 400;
    private final int material;
    private final int legalmoves;

    public EvaluatorByMaterialAndMoves() {
        this(FACTOR_MATERIAL_DEFAULT, FACTOR_MOVE_DEFAULT);
    }

    public EvaluatorByMaterialAndMoves(int material, int legalmoves) {
        this.material = material;
        this.legalmoves = legalmoves;
    }

    @Override
    public int evaluate() {
        int evaluation = 0;
        switch (game.getStatus()) {
            case MATE:
            case STALEMATE:
                evaluation = evaluateFinalStatus(game);
                break;
            case CHECK:
            case NO_CHECK:
                evaluation += material * evaluateByMaterial();
                evaluation += legalmoves * (Color.WHITE.equals(game.getChessPosition().getCurrentTurn()) ? +game.getPossibleMoves().size() : -game.getPossibleMoves().size());
        }
        return evaluation;
    }

    @Override
    public int getPieceValue(Piece piece) {
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
