package net.chesstango.evaluation.evaluators;

import net.chesstango.board.*;
import net.chesstango.board.moves.Move;
import net.chesstango.board.position.PositionReader;

import java.util.Iterator;

/**
 * @author Mauricio Coria
 * <p>
 * <p>
 * Positions: Balsa_v500.pgn
 * Depth: 1
 * Time elapsed: 980000 ms
 * _________________________________________________________________________________________________________________________________________________________
 * |ENGINE NAME                        |WHITE WON|BLACK WON|WHITE LOST|BLACK LOST|WHITE DRAW|BLACK DRAW|WHITE POINTS|BLACK POINTS|     TOTAL POINTS |   WIN %|
 * |                     EvaluatorImp04|    3464 |    3270 |     1704 |     1775 |     5728 |     5851 |     6328.0 |     6195.5 | 12523.5 /  21792 |   57.5 |
 * |    GameEvaluatorByMaterialAndMoves|      13 |       9 |     1666 |     1631 |     1045 |     1084 |      535.5 |      551.0 |  1086.5 /   5448 |   19.9 |
 * |                 GameEvaluatorImp02|     269 |     232 |      533 |      670 |     1922 |     1822 |     1230.0 |     1143.0 |  2373.0 /   5448 |   43.6 |
 * |   GameEvaluatorSimplifiedEvaluator|      42 |      55 |     1023 |     1093 |     1659 |     1576 |      871.5 |      843.0 |  1714.5 /   5448 |   31.5 |
 * |                          Spike 1.4|    1451 |    1408 |       48 |       70 |     1225 |     1246 |     2063.5 |     2031.0 |  4094.5 /   5448 |   75.2 |
 * --------------------------------------------------------------------------------------------------------------------------------------------------------
 * <p>
 * <p>
 * Positions: Balsa_v500.pgn
 * Depth: 2
 * Time elapsed: 88170 ms
 * ___________________________________________________________________________________________________________________________________________________
 * |ENGINE NAME                        |WHITE WON|BLACK WON|WHITE LOST|BLACK LOST|WHITE DRAW|BLACK DRAW|WHITE POINTS|BLACK POINTS|TOTAL POINTS|   WIN %|
 * |                     EvaluatorImp04|      36 |      25 |      423 |      454 |       41 |       21 |       56.5 |       35.5 |  92.0 /1000 |    9.2 |
 * |                          Spike 1.4|     454 |     423 |       25 |       36 |       21 |       41 |      464.5 |      443.5 | 908.0 /1000 |   90.8 |
 * ---------------------------------------------------------------------------------------------------------------------------------------------------
 */
public class EvaluatorImp04 extends AbstractEvaluator {

    private static final int FACTOR_MATERIAL_DEFAULT = 756;
    private static final int FACTOR_POSITION_DEFAULT = 204;
    private static final int FACTOR_EXPANSION_DEFAULT = 27;
    private static final int FACTOR_ATTACK_DEFAULT = 13;

    private final int material;
    private final int position;

    private final int expansion;
    private final int attack;

    public EvaluatorImp04() {
        this(FACTOR_MATERIAL_DEFAULT, FACTOR_POSITION_DEFAULT, FACTOR_EXPANSION_DEFAULT, FACTOR_ATTACK_DEFAULT);
    }

    public EvaluatorImp04(Integer material, Integer position, Integer expansion, Integer attack) {
        this.material = material;
        this.position = position;
        this.expansion = expansion;
        this.attack = attack;
    }


    @Override
    public int evaluateNonFinalStatus() {
        return material * evaluateByMaterial() + position * evaluateByPosition() + evaluateByMoveAndByAttack();
    }

    protected int evaluateByPosition() {
        int evaluation = 0;
        PositionReader positionReader = game.getPosition();
        for (Iterator<PiecePositioned> it = positionReader.iteratorAllPieces(); it.hasNext(); ) {
            PiecePositioned piecePlacement = it.next();
            Piece piece = piecePlacement.getPiece();
            Square square = piecePlacement.getSquare();
            int[] positionValues = getPositionValues(piece);
            evaluation += positionValues[square.idx()];
        }
        return evaluation;
    }

    protected int evaluateByMoveAndByAttack() {
        int evaluationByMoveToEmptySquare = 0;

        int evaluationByAttack = 0;

        for (Move move : game.getPseudoMoves()) {
            PiecePositioned fromPosition = move.getFrom();
            PiecePositioned toPosition = move.getTo();
            Piece piece = fromPosition.getPiece();

            if (toPosition.getPiece() == null) {
                Square toSquare = toPosition.getSquare();
                int[] positionValues = getPositionValues(piece);
                evaluationByMoveToEmptySquare += positionValues[toSquare.idx()];
            } else {
                evaluationByAttack -= getPieceValue(toPosition.getPiece());
            }

        }

        // From white point of view
        return expansion * evaluationByMoveToEmptySquare + attack * evaluationByAttack;
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

    protected int[] getPositionValues(Piece piece) {
        return switch (piece) {
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

    @Override
    public void setGame(Game game) {
        super.setGame(game);
    }


    protected int evaluateByMaterial() {
        int evaluation = 0;

        PositionReader positionReader = game.getPosition();

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
