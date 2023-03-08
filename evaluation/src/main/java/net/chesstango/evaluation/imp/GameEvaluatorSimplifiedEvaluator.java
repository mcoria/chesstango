package net.chesstango.evaluation.imp;

import net.chesstango.board.Game;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.position.ChessPositionReader;
import net.chesstango.evaluation.GameEvaluator;

import java.util.Iterator;

/**
 * @author Mauricio Coria
 *
 * Positions: Balsa_Top50.pgn
 * Depth: 1
 * Time taken: 90929 ms
 *  ___________________________________________________________________________________________________________________________________________________
 * |ENGINE NAME                        |WHITE WON|BLACK WON|WHITE LOST|BLACK LOST|WHITE DRAW|BLACK DRAW|WHITE POINTS|BLACK POINTS|TOTAL POINTS|   WIN %|
 * |   GameEvaluatorSimplifiedEvaluator|       0 |       0 |       38 |        0 |       16 |       12 |        8.0 |        6.0 |  14.0 /100 |   14.0 |
 * |                 GameEvaluatorImp02|      38 |      34 |        0 |        0 |       12 |       16 |       44.0 |       42.0 |  86.0 /100 |   86.0 |
 *  ---------------------------------------------------------------------------------------------------------------------------------------------------
 *
 */
public class GameEvaluatorSimplifiedEvaluator implements GameEvaluator {

    private static final int FACTOR_MATERIAL_DEFAULT = 404;
    private static final int FACTOR_POSITION_DEFAULT = 596;

    private final int material;
    private final int position;

    public GameEvaluatorSimplifiedEvaluator() {
        this(FACTOR_MATERIAL_DEFAULT, FACTOR_POSITION_DEFAULT);
    }

    public GameEvaluatorSimplifiedEvaluator(Integer material, Integer position) {
        this.material = material;
        this.position = position;
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
                evaluation = material * evaluateByMaterial(game);
                evaluation += position * evaluateByPosition(game);
        }
        return evaluation;
    }

    protected int evaluateByPosition(Game game) {
        int evaluation = 0;
        ChessPositionReader positionReader = game.getChessPosition();
        for (Iterator<PiecePositioned> it = positionReader.iteratorAllPieces(); it.hasNext(); ) {
            PiecePositioned piecePlacement = it.next();
            Piece piece = piecePlacement.getPiece();
            Square square = piecePlacement.getSquare();
            int[] positionValues = switch (piece){
                case PAWN_WHITE -> PAWN_WHITE_VALUES;
                case PAWN_BLACK -> PAWN_BLACK_VALUES;
                default -> null;
            };
            if(positionValues != null) {
                evaluation += positionValues[square.toIdx()];
            }
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
