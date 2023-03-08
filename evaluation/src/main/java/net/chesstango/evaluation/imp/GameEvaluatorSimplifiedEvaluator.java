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
 * Time taken: 107636 ms
 *  ___________________________________________________________________________________________________________________________________________________
 * |ENGINE NAME                        |WHITE WON|BLACK WON|WHITE LOST|BLACK LOST|WHITE DRAW|BLACK DRAW|WHITE POINTS|BLACK POINTS|TOTAL POINTS|   WIN %|
 * |   GameEvaluatorSimplifiedEvaluator|       0 |       0 |       25 |       27 |       25 |       23 |       12.5 |       11.5 |  24.0 /100 |   24.0 |
 * |                 GameEvaluatorImp02|      27 |      25 |        0 |        0 |       23 |       25 |       38.5 |       37.5 |  76.0 /100 |   76.0 |
 *  ---------------------------------------------------------------------------------------------------------------------------------------------------
 *
 */
public class GameEvaluatorSimplifiedEvaluator implements GameEvaluator {

    private static final int FACTOR_MATERIAL_DEFAULT = 500;
    private static final int FACTOR_POSITION_DEFAULT = 500;

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
