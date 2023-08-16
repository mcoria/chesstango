package net.chesstango.evaluation.evaluators;

import net.chesstango.board.Game;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.position.ChessPositionReader;

import java.util.Iterator;

/**
 * @author Mauricio Coria
 * <p>
 * Positions: Balsa_Top50.pgn
 * Depth: 2
 * Time taken: 119855 ms
 *  ___________________________________________________________________________________________________________________________________________________
 * |ENGINE NAME                        |WHITE WON|BLACK WON|WHITE LOST|BLACK LOST|WHITE DRAW|BLACK DRAW|WHITE POINTS|BLACK POINTS|TOTAL POINTS|   WIN %|
 * |   GameEvaluatorSimplifiedEvaluator|       6 |       5 |       17 |       15 |       27 |       30 |       19.5 |       20.0 |  39.5 /100 |   39.5 |
 * |                 GameEvaluatorImp02|      15 |      17 |        5 |        6 |       30 |       27 |       30.0 |       30.5 |  60.5 /100 |   60.5 |
 *  ---------------------------------------------------------------------------------------------------------------------------------------------------
 */
public class EvaluatorSimplifiedEvaluator extends AbstractEvaluator {

    private static final int FACTOR_MATERIAL_DEFAULT = 650;
    private static final int FACTOR_POSITION_DEFAULT = 350;

    private final int material;
    private final int position;

    public EvaluatorSimplifiedEvaluator() {
        this(FACTOR_MATERIAL_DEFAULT, FACTOR_POSITION_DEFAULT);
    }

    public EvaluatorSimplifiedEvaluator(Integer material, Integer position) {
        this.material = material;
        this.position = position;
    }


    @Override
    public int evaluate(final Game game) {
        int evaluation = 0;
        switch (game.getStatus()) {
            case MATE:
            case DRAW:
                evaluation = evaluateFinalStatus(game);
                break;
            case CHECK:
            case NO_CHECK:
                evaluation = material * evaluateByMaterial(game) + position * evaluateByPosition(game);
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
            int[] positionValues = switch (piece) {
                case PAWN_WHITE -> PAWN_WHITE_VALUES;
                case PAWN_BLACK -> PAWN_BLACK_VALUES;
                case KNIGHT_WHITE -> KNIGHT_WHITE_VALUES;
                case KNIGHT_BLACK -> KNIGHT_BLACK_VALUES;
                case BISHOP_WHITE -> BISHOP_WHITE_VALUES;
                case BISHOP_BLACK -> BISHOP_BLACK_VALUES;
                case ROOK_WHITE -> ROOK_WHITE_VALUES;
                case ROOK_BLACK -> ROOK_BLACK_VALUES;
                case QUEEN_WHITE -> QUEEN_WHITE_VALUES;
                case QUEEN_BLACK -> QUEEN_BLACK_VALUES;
                case KING_WHITE -> KING_WHITE_VALUES;
                case KING_BLACK -> KING_BLACK_VALUES;
            };
            evaluation += positionValues[square.toIdx()];
        }
        return evaluation;
    }

    @Override
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

    protected static final int[] PAWN_WHITE_VALUES = {
            0, 0, 0, 0, 0, 0, 0, 0,                 // Rank 1
            5, 10, 10, -20, -20, 10, 10, 5,         // Rank 2
            5, -5, -10, 0, 0, -10, -5, 5,           // Rank 3
            0, 0, 0, 20, 20, 0, 0, 0,               // Rank 4
            5, 5, 10, 25, 25, 10, 5, 5,             // Rank 5
            10, 10, 20, 30, 30, 20, 10, 10,         // Rank 6
            50, 50, 50, 50, 50, 50, 50, 50,         // Rank 7
            0, 0, 0, 0, 0, 0, 0, 0                  // Rank 8
    };

    protected static final int[] PAWN_BLACK_VALUES = {
            0, 0, 0, 0, 0, 0, 0, 0,                 // Rank 1
            -50, -50, -50, -50, -50, -50, -50, -50, // Rank 2
            -10, -10, -20, -30, -30, -20, -10, -10, // Rank 3
            -5, -5, -10, -25, -25, -10, -5, -5,     // Rank 4
            0, 0, 0, -20, -20, 0, 0, 0,             // Rank 5
            -5, 5, 10, 0, 0, 10, 5, -5,             // Rank 6
            -5, -10, -10, 20, 20, -10, -10, -5,     // Rank 7
            0, 0, 0, 0, 0, 0, 0, 0,                 // Rank 8
    };


    protected static final int[] KNIGHT_WHITE_VALUES = {
            -50, -40, -30, -30, -30, -30, -40, -50, // Rank 1
            -40, -20, 0, 5, 5, 0, -20, -40,         // Rank 2
            -30, 5, 10, 15, 15, 10, 5, -30,         // Rank 3
            -30, 0, 15, 20, 20, 15, 0, -30,         // Rank 4
            -30, 5, 15, 20, 20, 15, 5, -30,         // Rank 5
            -30, 0, 10, 15, 15, 10, 0, -30,         // Rank 6
            -40, -20, 0, 0, 0, 0, -20, -40,         // Rank 7
            -50, -40, -30, -30, -30, -30, -40, -50  // Rank 8
    };

    protected static final int[] KNIGHT_BLACK_VALUES = {
            50, 40, 30, 30, 30, 30, 40, 50,            // Rank 1
            40, 20, 0, 0, 0, 0, 20, 40,                // Rank 2
            30, 0, -10, -15, -15, -10, 0, 30,          // Rank 3
            30, -5, -15, -20, -20, -15, -5, 30,        // Rank 4
            30, 0, -15, -20, -20, -15, 0, 30,          // Rank 5
            30, -5, -10, -15, -15, -10, -5, 30,        // Rank 6
            40, 20, 0, -5, -5, 0, 20, 40,              // Rank 7
            50, 40, 30, 30, 30, 30, 40, 50             // Rank 8
    };

    protected static final int[] BISHOP_WHITE_VALUES = {
            -20, -10, -10, -10, -10, -10, -10, -20,     // Rank 1
            -10, 5, 0, 0, 0, 0, 5, -10,                 // Rank 2
            -10, 10, 10, 10, 10, 10, 10, -10,           // Rank 3
            -10, 0, 10, 10, 10, 10, 0, -10,             // Rank 4
            -10, 5, 5, 10, 10, 5, 5, -10,               // Rank 5
            -10, 0, 5, 10, 10, 5, 0, -10,               // Rank 6
            -10, 0, 0, 0, 0, 0, 0, -10,                 // Rank 7
            -20, -10, -10, -10, -10, -10, -10, -20      // Rank 8
    };

    protected static final int[] BISHOP_BLACK_VALUES = {
            20, 10, 10, 10, 10, 10, 10, 20,             // Rank 1
            10, 0, 0, 0, 0, 0, 0, 10,                   // Rank 2
            10, 0, -5, -10, -10, -5, 0, 10,             // Rank 3
            10, -5, -5, -10, -10, -5, -5, 10,           // Rank 4
            10, 0, -10, -10, -10, -10, 0, 10,           // Rank 5
            10, -10, -10, -10, -10, -10, -10, 10,       // Rank 6
            10, -5, 0, 0, 0, 0, -5, 10,                 // Rank 7
            20, 10, 10, 10, 10, 10, 10, 20              // Rank 8
    };


    protected static final int[] ROOK_WHITE_VALUES = {
            0, 0, 0, 5, 5, 0, 0, 0,              // Rank 1
            -5, 0, 0, 0, 0, 0, 0, -5,             // Rank 2
            -5, 0, 0, 0, 0, 0, 0, -5,             // Rank 3
            -5, 0, 0, 0, 0, 0, 0, -5,             // Rank 4
            -5, 0, 0, 0, 0, 0, 0, -5,             // Rank 5
            -5, 0, 0, 0, 0, 0, 0, -5,             // Rank 6
            5, 10, 10, 10, 10, 10, 10, 5,         // Rank 7
            0, 0, 0, 0, 0, 0, 0, 0                // Rank 8
    };

    protected static final int[] ROOK_BLACK_VALUES = {
            0, 0, 0, 0, 0, 0, 0, 0,                 // Rank 1
            -5, -10, -10, -10, -10, -10, -10, -5,   // Rank 2
            5, 0, 0, 0, 0, 0, 0, 5,                 // Rank 3
            5, 0, 0, 0, 0, 0, 0, 5,                 // Rank 4
            5, 0, 0, 0, 0, 0, 0, 5,                 // Rank 5
            5, 0, 0, 0, 0, 0, 0, 5,                 // Rank 6
            5, 0, 0, 0, 0, 0, 0, 5,                 // Rank 7
            0, 0, 0, -5, -5, 0, 0, 0                // Rank 8
    };

    protected static final int[] QUEEN_WHITE_VALUES = {
            -20, -10, -10, -5, -5, -10, -10, -20,   // Rank 1
            -10, 0, 5, 0, 0, 0, 0, -10,             // Rank 2
            -10, 5, 5, 5, 5, 5, 0, -10,             // Rank 3
            0, 0, 5, 5, 5, 5, 0, -5,                // Rank 4
            -5, 0, 5, 5, 5, 5, 0, -5,               // Rank 5
            -10, 0, 5, 5, 5, 5, 0, -10,             // Rank 6
            -10, 0, 0, 0, 0, 0, 0, -10,             // Rank 7
            -20, -10, -10, -5, -5, -10, -10, -20    // Rank 8
    };

    protected static final int[] QUEEN_BLACK_VALUES = {
            20, 10, 10, 5, 5, 10, 10, 20,     // Rank 1
            10, 0, 0, 0, 0, 0, 0, 10,         // Rank 2
            10, 0, -5, -5, -5, -5, 0, 10,     // Rank 3
            5, 0, -5, -5, -5, -5, 0, 5,       // Rank 4
            0, 0, -5, -5, -5, -5, 0, 5,       // Rank 5
            10, -5, -5, -5, -5, -5, 0, 10,    // Rank 6
            10, 0, -5, 0, 0, 0, 0, 10,        // Rank 7
            20, 10, 10, 5, 5, 10, 10, 20,     // Rank 8
    };

    protected static final int[] KING_WHITE_VALUES = {
            20, 30, 10, 0, 0, 10, 30, 20,                // Rank 1
            20, 20, 0, 0, 0, 0, 20, 20,                  // Rank 2
            -10, -20, -20, -20, -20, -20, -20, -10,      // Rank 3
            -20, -30, -30, -40, -40, -30, -30, -20,      // Rank 4
            -30, -40, -40, -50, -50, -40, -40, -30,      // Rank 5
            -30, -40, -40, -50, -50, -40, -40, -30,      // Rank 6
            -30, -40, -40, -50, -50, -40, -40, -30,      // Rank 7
            -30, -40, -40, -50, -50, -40, -40, -30       // Rank 8
    };

    protected static final int[] KING_BLACK_VALUES = {
            30, 40, 40, 50, 50, 40, 40, 30,         // Rank 1
            30, 40, 40, 50, 50, 40, 40, 30,         // Rank 2
            30, 40, 40, 50, 50, 40, 40, 30,         // Rank 3
            30, 40, 40, 50, 50, 40, 40, 30,         // Rank 4
            20, 30, 30, 40, 40, 30, 30, 20,         // Rank 5
            10, 20, 20, 20, 20, 20, 20, 10,         // Rank 6
            -20, -20, 0, 0, 0, 0, -20, -20,         // Rank 7
            -20, -30, -10, 0, 0, -10, -30, -20     // Rank 8
    };

}