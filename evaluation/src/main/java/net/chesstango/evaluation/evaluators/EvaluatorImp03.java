package net.chesstango.evaluation.evaluators;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.position.ChessPositionReader;

import java.util.Iterator;

/**
 * @author Mauricio Coria
 *
 *  Positions: Balsa_v2724.pgn
 *  Depth: 1
 *  Time elapsed: 1066937 ms
 *  Time elapsed: 355648 ms
 *  _______________________________________________________________________________________________________________________________________________________
 * |ENGINE NAME                        |WHITE WON|BLACK WON|WHITE LOST|BLACK LOST|WHITE DRAW|BLACK DRAW|WHITE POINTS|BLACK POINTS|  TOTAL POINTS  |   WIN %|
 * |                 GameEvaluatorImp03|    1910 |    1846 |     5827 |     5982 |     5873 |     5782 |     4846.5 |     4737.0 | 9583.5 / 27220 |   35.2 |
 * |            GameEvaluatorByMaterial|     422 |     409 |      560 |      596 |     1742 |     1719 |     1293.0 |     1268.5 | 2561.5 / 5448  |   47.0 |
 * |    GameEvaluatorByMaterialAndMoves|      32 |      45 |     1106 |     1165 |     1586 |     1514 |      825.0 |      802.0 | 1627.0 / 5448  |   29.9 |
 * |                 GameEvaluatorImp01|     973 |     968 |      161 |      120 |     1590 |     1636 |     1768.0 |     1786.0 | 3554.0 / 5448  |   65.2 |
 * |                 GameEvaluatorImp02|    1896 |    1747 |       18 |       29 |      810 |      948 |     2301.0 |     2221.0 | 4522.0 / 5448  |   83.0 |
 * |                          Spike 1.4|    2659 |    2658 |        1 |        0 |       54 |       56 |     2686.0 |     2686.0 | 5372.0 / 5428  |   99.0 |
 *  -------------------------------------------------------------------------------------------------------------------------------------------------------
 */
public class EvaluatorImp03 extends AbstractEvaluator {
    private static final int FACTOR_MATERIAL_DEFAULT = 500;
    private static final int FACTOR_MATERIAL_COLOR_DEFAULT = 500;
    private final int material;
    private final int material_color;

    public EvaluatorImp03() {
        this(FACTOR_MATERIAL_DEFAULT, FACTOR_MATERIAL_COLOR_DEFAULT);
    }

    public EvaluatorImp03(Integer material, Integer material_color) {
        this.material = material;
        this.material_color = material_color;
    }

    @Override
    public int evaluateNonFinalStatus() {
        int evaluation = 0;
        switch (game.getStatus()) {
            case CHECK:
                // If white is on check then evaluation starts at -1
                evaluation = Color.WHITE.equals(game.getPosition().getCurrentTurn()) ? -1 : +1;
            case NO_CHECK:
                evaluation += material * evaluateByMaterial();
                evaluation += material_color * evaluateByColor();
                break;
            default:
                throw new RuntimeException("Invalid Status");
        }
        return evaluation;
    }

    protected int evaluateByColor() {
        int evaluation = 0;
        ChessPositionReader positionReader = game.getPosition();
        for (Iterator<PiecePositioned> it = positionReader.iteratorAllPieces(); it.hasNext(); ) {
            PiecePositioned piecePlacement = it.next();
            evaluation += Color.WHITE.equals(piecePlacement.getPiece().getColor()) ? +1 : -1;
        }
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
