package net.chesstango.evaluation.evaluators;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.position.ChessPositionReader;

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
    public int evaluateNonFinalStatus() {
        int evaluation = 0;
        evaluation += material * evaluateByMaterial();
        evaluation += legalmoves * (Color.WHITE.equals(game.getPosition().getCurrentTurn()) ? +game.getPossibleMoves().size() : -game.getPossibleMoves().size());
        return evaluation;
    }


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

    protected int evaluateByMaterial() {
        int evaluation = 0;

        ChessPositionReader positionReader = game.getPosition();

        long whitePositions = positionReader.getPositions(Color.WHITE);

        long blackPositions = positionReader.getPositions(Color.BLACK);

        evaluation += Long.bitCount(whitePositions & positionReader.getRookPositions()) * getPieceValue(Piece.ROOK_WHITE);
        evaluation += Long.bitCount(whitePositions & positionReader.getKnightPositions()) * getPieceValue(Piece.KNIGHT_WHITE);
        evaluation += Long.bitCount(whitePositions & positionReader.getBishopPositions()) * getPieceValue(Piece.BISHOP_WHITE);
        evaluation += Long.bitCount(whitePositions & positionReader.getQueenPositions()) * getPieceValue(Piece.QUEEN_WHITE);
        evaluation += Long.bitCount(whitePositions & positionReader.getPawnPositions()) * getPieceValue(Piece.PAWN_WHITE);

        evaluation += Long.bitCount(blackPositions & positionReader.getRookPositions()) * getPieceValue(Piece.ROOK_BLACK);
        evaluation += Long.bitCount(blackPositions & positionReader.getKnightPositions()) * getPieceValue(Piece.KNIGHT_BLACK);
        evaluation += Long.bitCount(blackPositions & positionReader.getBishopPositions()) * getPieceValue(Piece.BISHOP_BLACK);
        evaluation += Long.bitCount(blackPositions & positionReader.getQueenPositions()) * getPieceValue(Piece.QUEEN_BLACK);
        evaluation += Long.bitCount(blackPositions & positionReader.getPawnPositions()) * getPieceValue(Piece.PAWN_BLACK);

        return evaluation;
    }
}
