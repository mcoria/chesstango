package net.chesstango.evaluation.evaluators;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.position.ChessPositionReader;

/**
 * @author Mauricio Coria
 * <p>
 * MatchByDepth = 3
 * Balsa_Top50
 * ___________________________________________________________________________________________________________________________________________________
 * |ENGINE NAME                        |WHITE WON|BLACK WON|WHITE LOST|BLACK LOST|WHITE DRAW|BLACK DRAW|WHITE POINTS|BLACK POINTS|TOTAL POINTS|   WIN %|
 * |       EvaluatorByMaterialImbalance|      94 |     128 |       31 |       32 |      375 |      340 |      281.5 |      298.0 | 579.5 /1000 |   58.0 |
 * |          EvaluatorByMaterialPieces|      32 |      31 |      128 |       94 |      340 |      375 |      202.0 |      218.5 | 420.5 /1000 |   42.1 |
 * ---------------------------------------------------------------------------------------------------------------------------------------------------
 */
public class EvaluatorByMaterialImbalance extends AbstractEvaluator {


    @Override
    public int evaluateNonFinalStatus() {
        return evaluateByMaterial();
    }


    private static final long BISHOP_PARES = 0xAA55AA55AA55AA55L;

    private static final long BISHOP_IMPARES = 0x55AA55AA55AA55AAL;


    protected int evaluateByMaterial() {
        int evaluation = 0;

        ChessPositionReader positionReader = game.getChessPosition();

        long whitePositions = positionReader.getPositions(Color.WHITE);

        long blackPositions = positionReader.getPositions(Color.BLACK);


        /**
         * Whites
         */
        evaluation += Long.bitCount(whitePositions & positionReader.getRookPositions()) * getPieceValue(Piece.ROOK_WHITE);
        evaluation += Long.bitCount(whitePositions & positionReader.getKnightPositions()) * getPieceValue(Piece.KNIGHT_WHITE);
        evaluation += Long.bitCount(whitePositions & positionReader.getQueenPositions()) * getPieceValue(Piece.QUEEN_WHITE);
        evaluation += Long.bitCount(whitePositions & positionReader.getPawnPositions()) * getPieceValue(Piece.PAWN_WHITE);

        long whiteBishopPositions = whitePositions & positionReader.getBishopPositions();
        if (whiteBishopPositions != 0) {
            if ((BISHOP_PARES & whiteBishopPositions) != 0) {
                evaluation += getPieceValue(Piece.BISHOP_WHITE) + Long.bitCount(blackPositions & BISHOP_PARES);
            }

            if ((BISHOP_IMPARES & whiteBishopPositions) != 0) {
                evaluation += getPieceValue(Piece.BISHOP_WHITE) + Long.bitCount(blackPositions & BISHOP_IMPARES);
            }
        }


        /**
         * Blacks
         */
        evaluation += Long.bitCount(blackPositions & positionReader.getRookPositions()) * getPieceValue(Piece.ROOK_BLACK);
        evaluation += Long.bitCount(blackPositions & positionReader.getKnightPositions()) * getPieceValue(Piece.KNIGHT_BLACK);
        evaluation += Long.bitCount(blackPositions & positionReader.getQueenPositions()) * getPieceValue(Piece.QUEEN_BLACK);
        evaluation += Long.bitCount(blackPositions & positionReader.getPawnPositions()) * getPieceValue(Piece.PAWN_BLACK);

        long blackBishopPositions = blackPositions & positionReader.getBishopPositions();
        if (blackBishopPositions != 0) {
            if ((BISHOP_PARES & blackBishopPositions) != 0) {
                evaluation += getPieceValue(Piece.BISHOP_BLACK) - Long.bitCount(whitePositions & BISHOP_PARES);
            }

            if ((BISHOP_IMPARES & blackBishopPositions) != 0) {
                evaluation += getPieceValue(Piece.BISHOP_BLACK) - Long.bitCount(whitePositions & BISHOP_IMPARES);
            }
        }

        return evaluation;
    }


    public int getPieceValue(Piece piece) {
        return switch (piece) {
            case PAWN_WHITE -> 100;
            case PAWN_BLACK -> -100;
            case KNIGHT_WHITE -> 320;
            case KNIGHT_BLACK -> -320;
            case BISHOP_WHITE -> 330;
            case BISHOP_BLACK -> -330;
            case ROOK_WHITE -> 500;
            case ROOK_BLACK -> -500;
            case QUEEN_WHITE -> 900;
            case QUEEN_BLACK -> -900;
            case KING_WHITE -> 20000;
            case KING_BLACK -> -20000;
        };
    }

}
