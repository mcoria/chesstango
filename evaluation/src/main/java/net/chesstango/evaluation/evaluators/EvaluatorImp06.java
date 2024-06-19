package net.chesstango.evaluation.evaluators;

import net.chesstango.board.*;
import net.chesstango.board.moves.generators.pseudo.MoveGenerator;
import net.chesstango.board.position.ChessPositionReader;

import java.util.Iterator;

/**
 * @author Mauricio Coria
 */
public class EvaluatorImp06 extends AbstractEvaluator {

    private static final int WEIGH_MATERIAL_DEFAULT = 902;
    private static final int WEIGH_MG_DEFAULT = 45;
    private static final int WEIGH_EG_DEFAULT = 43;

    private static final int WEIGH_KNIGHT_PAIR = 0;
    private static final int WEIGH_BISHOP_PAIR = 0;
    private static final int WEIGH_ROOK_PAIR = 0;

    private int wgMaterial;
    private int wgMidGame;
    private int wgEndGame;
    private int wgKnightPair;
    private int wgBishopPair;
    private int wgRookPair;

    private ChessPositionReader positionReader;

    public EvaluatorImp06() {
        this(new int[]{WEIGH_MATERIAL_DEFAULT, WEIGH_MG_DEFAULT, WEIGH_EG_DEFAULT, WEIGH_KNIGHT_PAIR, WEIGH_BISHOP_PAIR, WEIGH_ROOK_PAIR});
    }

    public EvaluatorImp06(int[] weighs) {
        setWeighs(weighs);
    }

    protected void setWeighs(int[] weighs) {
        this.wgMaterial = weighs[0];
        this.wgMidGame = weighs[1];
        this.wgEndGame = weighs[2];
        this.wgKnightPair = weighs[3];
        this.wgBishopPair = weighs[4];
        this.wgRookPair = weighs[5];
    }


    @Override
    public int evaluate() {
        if (game.getStatus().isFinalStatus()) {
            return evaluateFinalStatus();
        } else {
            return wgMaterial * evaluateByMaterial() + evaluateByPST() + evaluateByPairsTapered();
        }
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

    protected int evaluateByPairsTapered() {
        final int numberOfPieces = Long.bitCount(positionReader.getAllPositions());
        return wgEndGame * (32 - numberOfPieces) * evaluateByPairs();
    }

    protected int evaluateByPairs() {
        final long whitePositions = positionReader.getPositions(Color.WHITE);
        final long blackPositions = positionReader.getPositions(Color.BLACK);
        return wgKnightPair * evaluateByKnightPair(whitePositions, blackPositions) +
                wgBishopPair * evaluateByBishopPair(whitePositions, blackPositions) +
                wgRookPair * evaluateByRookPair(whitePositions, blackPositions);
    }


    protected int evaluateByKnightPair(long whitePositions, long blackPositions) {
        int evaluation = 0;

        long knightPositions = positionReader.getKnightPositions();

        if (Long.bitCount(whitePositions & knightPositions) > 1) {
            evaluation++;
        }

        if (Long.bitCount(blackPositions & knightPositions) > 1) {
            evaluation--;
        }

        return evaluation;
    }

    protected int evaluateByBishopPair(long whitePositions, long blackPositions) {
        int evaluation = 0;

        long bishopPositions = positionReader.getBishopPositions();

        if (Long.bitCount(whitePositions & bishopPositions) > 1) {
            evaluation++;
        }

        if (Long.bitCount(blackPositions & bishopPositions) > 1) {
            evaluation--;
        }

        return evaluation;
    }

    protected int evaluateByRookPair(long whitePositions, long blackPositions) {
        int evaluation = 0;

        long rookPositions = positionReader.getRookPositions();

        if (Long.bitCount(whitePositions & rookPositions) > 1) {
            evaluation++;
        }

        if (Long.bitCount(blackPositions & rookPositions) > 1) {
            evaluation--;
        }

        return evaluation;
    }


    protected int evaluateByPST() {
        int evaluation = 0;

        final int numberOfPieces = Long.bitCount(positionReader.getAllPositions());

        for (Iterator<PiecePositioned> it = positionReader.iteratorAllPieces(); it.hasNext(); ) {
            PiecePositioned piecePlacement = it.next();
            Piece piece = piecePlacement.getPiece();
            Square square = piecePlacement.getSquare();

            int[] mgPositionValues = getMgPositionValues(piece);
            int mgValue = Color.WHITE.equals(piece.getColor()) ? mgPositionValues[square.toIdx()] : -mgPositionValues[square.getMirrorSquare().toIdx()];

            int[] egPositionValues = getEgPositionValues(piece);
            int egValue = Color.WHITE.equals(piece.getColor()) ? egPositionValues[square.toIdx()] : -egPositionValues[square.getMirrorSquare().toIdx()];

            evaluation += wgMidGame * (numberOfPieces - 2) * mgValue + wgEndGame * (32 - numberOfPieces) * egValue;
        }
        return evaluation;
    }


    protected int[] getMgPositionValues(Piece piece) {
        return switch (piece) {
            case PAWN_WHITE, PAWN_BLACK -> MG_PAWN_TBL;
            case KNIGHT_WHITE, KNIGHT_BLACK -> MG_KNIGHT_TBL;
            case BISHOP_WHITE, BISHOP_BLACK -> MG_BISHOP_TBL;
            case ROOK_WHITE, ROOK_BLACK -> MG_ROOK_TBL;
            case QUEEN_WHITE, QUEEN_BLACK -> MG_QUEEN_TBL;
            case KING_WHITE, KING_BLACK -> MG_KING_TBL;
        };
    }

    protected int[] getEgPositionValues(Piece piece) {
        return switch (piece) {
            case PAWN_WHITE, PAWN_BLACK -> EG_PAWN_TBL;
            case KNIGHT_WHITE, KNIGHT_BLACK -> EG_KNIGHT_TBL;
            case BISHOP_WHITE, BISHOP_BLACK -> EG_BISHOP_TBL;
            case ROOK_WHITE, ROOK_BLACK -> EG_ROOK_TBL;
            case QUEEN_WHITE, QUEEN_BLACK -> EG_QUEEN_TBL;
            case KING_WHITE, KING_BLACK -> EG_KING_TBL;
        };
    }

    protected static final int[] MG_PAWN_TBL = new int[]{
            0, 0, 0, 0, 0, 0, 0, 0,                 // Rank 1
            -35, -1, -20, -23, -15, 24, 38, -22,    // Rank 2
            -26, -4, -4, -10, 3, 3, 33, -12,        // Rank 3
            -27, -2, -5, 12, 17, 6, 10, -25,        // Rank 4
            -14, 13, 6, 21, 23, 12, 17, -23,        // Rank 5
            -6, 7, 26, 31, 65, 56, 25, -20,         // Rank 6
            98, 134, 61, 95, 68, 126, 34, -11,      // Rank 7
            0, 0, 0, 0, 0, 0, 0, 0,                 // Rank 8
    };

    protected static final int[] EG_PAWN_TBL = new int[]{
            0, 0, 0, 0, 0, 0, 0, 0,                 // Rank 1
            13, 8, 8, 10, 13, 0, 2, -7,             // Rank 2
            4, 7, -6, 1, 0, -5, -1, -8,             // Rank 3
            13, 9, -3, -7, -7, -8, 3, -1,           // Rank 4
            32, 24, 13, 5, -2, 4, 17, 17,           // Rank 5
            94, 100, 85, 67, 56, 53, 82, 84,        // Rank 6
            178, 173, 158, 134, 147, 132, 165, 187, // Rank 7
            0, 0, 0, 0, 0, 0, 0, 0,                 // Rank 8
    };


    protected static final int[] MG_KNIGHT_TBL = {
            -105, -21, -58, -33, -17, -28, -19, -23,   // Rank 1
            -29, -53, -12, -3, -1, 18, -14, -19,    // Rank 2
            -23, -9, 12, 10, 19, 17, 25, -16,    // Rank 3
            -13, 4, 16, 13, 28, 19, 21, -8,    // Rank 4
            -9, 17, 19, 53, 37, 69, 18, 22,     // Rank 5
            -47, 60, 37, 65, 84, 129, 73, 44,    // Rank 6
            -73, -41, 72, 36, 23, 62, 7, -17,    // Rank 7
            -167, -89, -34, -49, 61, -97, -15, -107,   // Rank 8
    };

    protected static final int[] EG_KNIGHT_TBL = {
            -29, -51, -23, -15, -22, -18, -50, -64, // Rank 1
            -42, -20, -10, -5, -2, -20, -23, -44, // Rank 2
            -23, -3, -1, 15, 10, -3, -20, -22, // Rank 3
            -18, -6, 16, 25, 16, 17, 4, -18, // Rank 4
            -17, 3, 22, 22, 22, 11, 8, -18, // Rank 5
            -24, -20, 10, 9, -1, -9, -19, -41, // Rank 6
            -25, -8, -25, -2, -9, -25, -24, -52, // Rank 7
            -58, -38, -13, -28, -31, -27, -63, -99, // Rank 8
    };


    protected static final int[] MG_BISHOP_TBL = {
            -33, -3, -14, -21, -13, -12, -39, -21, // Rank 1
            4, 15, 16, 0, 7, 21, 33, 1,   // Rank 2
            0, 15, 15, 15, 14, 27, 18, 10,   // Rank 3
            -6, 13, 13, 26, 34, 12, 10, 4,  // Rank 4
            -4, 5, 19, 50, 37, 37, 7, -2,  // Rank 5
            -16, 37, 43, 40, 35, 50, 37, -2, // Rank 6
            -26, 16, -18, -13, 30, 59, 18, -47, // Rank 7
            -29, 4, -82, -37, -25, -42, 7, -8, // Rank 8
    };

    protected static final int[] EG_BISHOP_TBL = {
            -23, -9, -23, -5, -9, -16, -5, -17,  // Rank 1
            -14, -18, -7, -1, 4, -9, -15, -27,  // Rank 2
            -12, -3, 8, 10, 13, 3, -7, -15,  // Rank 3
            -6, 3, 13, 19, 7, 10, -3, -9,   // Rank 4
            -3, 9, 12, 9, 14, 10, 3, 2,   // Rank 5
            2, -8, 0, -1, -2, 6, 0, 4,    // Rank 6
            -8, -4, 7, -12, -3, -13, -4, -14,   // Rank 7
            -14, -21, -11, -8, -7, -9, -17, -24,  // Rank 8
    };


    protected static final int[] MG_ROOK_TBL = {
            -19, -13, 1, 17, 16, 7, -37, -26,   // Rank 1
            -44, -16, -20, -9, -1, 11, -6, -71,   // Rank 2
            -45, -25, -16, -17, 3, 0, -5, -33,   // Rank 3
            -36, -26, -12, -1, 9, -7, 6, -23,   // Rank 4
            -24, -11, 7, 26, 24, 35, -8, -20,   // Rank 5
            -5, 19, 26, 36, 17, 45, 61, 16,    // Rank 6
            27, 32, 58, 62, 80, 67, 26, 44,    // Rank 7
            32, 42, 32, 51, 63, 9, 31, 43,    // Rank 8
    };

    protected static final int[] EG_ROOK_TBL = {
            -9, 2, 3, -1, -5, -13, 4, -20,  // Rank 1
            -6, -6, 0, 2, -9, -9, -11, -3,  // Rank 2
            -4, 0, -5, -1, -7, -12, -8, -16,  // Rank 3
            3, 5, 8, 4, -5, -6, -8, -11,   // Rank 4
            4, 3, 13, 1, 2, 1, -1, 2,   // Rank 5
            7, 7, 7, 5, 4, -3, -5, -3,   // Rank 6
            11, 13, 13, 11, -3, 3, 8, 3,  // Rank 7
            13, 10, 18, 15, 12, 12, 8, 5,  // Rank 8
    };


    protected static final int[] MG_QUEEN_TBL = {
            -1, -18, -9, 10, -15, -25, -31, -50,      // Rank 1
            -35, -8, 11, 2, 8, 15, -3, 1,     // Rank 2
            -14, 2, -11, -2, -5, 2, 14, 5,     // Rank 3
            -9, -26, -9, -10, -2, -4, 3, -3,      // Rank 4
            -27, -27, -16, -16, -1, 17, -2, 1,     // Rank 5
            -13, -17, 7, 8, 29, 56, 47, 57,     // Rank 6
            -24, -39, -5, 1, -16, 57, 28, 54,     // Rank 7
            -28, 0, 29, 12, 59, 44, 43, 45,     // Rank 8
    };

    protected static final int[] EG_QUEEN_TBL = {
            -33, -28, -22, -43, -5, -32, -20, -41,     // Rank 1
            -22, -23, -30, -16, -16, -23, -36, -32,     // Rank 2
            -16, -27, 15, 6, 9, 17, 10, 5,     // Rank 3
            -18, 28, 19, 47, 31, 34, 39, 23,     // Rank 4
            3, 22, 24, 45, 57, 40, 57, 36,     // Rank 5
            -20, 6, 9, 49, 47, 35, 19, 9,     // Rank 6
            -17, 20, 32, 41, 58, 25, 30, 0,     // Rank 7
            -9, 22, 22, 27, 27, 19, 10, 20,      // Rank 8
    };


    protected static final int[] MG_KING_TBL = {
            -15, 36, 12, -54, 8, -28, 24, 14,     // Rank 1
            1, 7, -8, -64, -43, -16, 9, 8,     // Rank 2
            -14, -14, -22, -46, -44, -30, -15, -27,     // Rank 3
            -49, -1, -27, -39, -46, -44, -33, -51,     // Rank 4
            -17, -20, -12, -27, -30, -25, -14, -36,     // Rank 5
            -9, 24, 2, -16, -20, 6, 22, -22,      // Rank 6
            29, -1, -20, -7, -8, -4, -38, -29,      // Rank 7
            -65, 23, 16, -15, -56, -34, 2, 13,     // Rank 8
    };

    protected static final int[] EG_KING_TBL = {
            -53, -34, -21, -11, -28, -14, -24, -43,     // Rank 1
            -27, -11, 4, 13, 14, 4, -5, -17,     // Rank 2
            -19, -3, 11, 21, 23, 16, 7, -9,     // Rank 3
            -18, -4, 21, 24, 27, 23, 9, -11,     // Rank 4
            -8, 22, 24, 27, 26, 33, 26, 3,      // Rank 5
            10, 17, 23, 15, 20, 45, 44, 13,      // Rank 6
            -12, 17, 14, 17, 17, 38, 23, 11,     // Rank 7
            -74, -35, -18, -18, -11, 15, 4, -17,     // Rank 8
    };


    @Override
    public void setGame(Game game) {
        super.setGame(game);
        game.accept(new GameVisitor() {
            @Override
            public void visit(ChessPositionReader chessPositionReader) {
                positionReader = chessPositionReader;
            }

            @Override
            public void visit(GameStateReader gameState) {
            }

            @Override
            public void visit(MoveGenerator moveGenerator) {
            }

        });
    }

    @Override
    protected int evaluateByMaterial() {
        int evaluation = 0;

        ChessPositionReader positionReader = game.getChessPosition();

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
