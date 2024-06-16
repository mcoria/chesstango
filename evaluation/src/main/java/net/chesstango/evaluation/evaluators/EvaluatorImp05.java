package net.chesstango.evaluation.evaluators;

import net.chesstango.board.*;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.containers.MoveList;
import net.chesstango.board.moves.generators.pseudo.MoveGenerator;
import net.chesstango.board.moves.generators.pseudo.MoveGeneratorResult;
import net.chesstango.board.position.ChessPositionReader;

import java.util.Iterator;

/**
 * @author Mauricio Coria
 */
public class EvaluatorImp05 extends AbstractEvaluator {

    private static final int FACTOR_MATERIAL_DEFAULT = 756;
    private static final int FACTOR_POSITION_DEFAULT = 204;
    private static final int FACTOR_EXPANSION_DEFAULT = 27;
    private static final int FACTOR_ATTACK_DEFAULT = 13;

    private final int material;
    private final int position;

    private final int expansion;
    private final int attack;

    private ChessPositionReader positionReader;
    private MoveGenerator pseudoMovesGenerator;

    public EvaluatorImp05() {
        this(FACTOR_MATERIAL_DEFAULT, FACTOR_POSITION_DEFAULT, FACTOR_EXPANSION_DEFAULT, FACTOR_ATTACK_DEFAULT);
    }

    public EvaluatorImp05(Integer material, Integer position, Integer expansion, Integer attack) {
        this.material = material;
        this.position = position;
        this.expansion = expansion;
        this.attack = attack;
    }


    @Override
    public int evaluate() {
        return switch (game.getStatus()) {
            case MATE, STALEMATE, DRAW_BY_FIFTY_RULE, DRAW_BY_FOLD_REPETITION -> evaluateFinalStatus(game);
            case CHECK, NO_CHECK ->
                    material * evaluateByMaterial() + position * evaluateByPosition() + evaluateByMoveAndByAttack();
            default -> throw new RuntimeException(String.format("Unknown game status %s", game.getStatus()));
        };
    }

    protected int evaluateByPosition() {
        int evaluation = 0;
        ChessPositionReader positionReader = game.getChessPosition();
        for (Iterator<PiecePositioned> it = positionReader.iteratorAllPieces(); it.hasNext(); ) {
            PiecePositioned piecePlacement = it.next();
            Piece piece = piecePlacement.getPiece();
            Square square = piecePlacement.getSquare();
            int[] positionValues = getPositionValues(piece);
            evaluation += Color.WHITE.equals(piece.getColor()) ? positionValues[square.toIdx()] : -positionValues[square.getMirrorSquare().toIdx()];
        }
        return evaluation;
    }

    protected int evaluateByMoveAndByAttack() {
        int evaluationByMoveToEmptySquare = 0;

        int evaluationByAttack = 0;

        Iterator<PiecePositioned> iteratorAllPieces = positionReader.iteratorAllPieces();

        while (iteratorAllPieces.hasNext()) {
            PiecePositioned piecePositioned = iteratorAllPieces.next();

            MoveGeneratorResult generationResult = pseudoMovesGenerator.generatePseudoMoves(piecePositioned);

            MoveList pseudoMoves = generationResult.getPseudoMoves();

            for (Move move : pseudoMoves) {
                PiecePositioned fromPosition = move.getFrom();
                PiecePositioned toPosition = move.getTo();
                Piece piece = fromPosition.getPiece();

                if (toPosition.getPiece() == null) {
                    Square toSquare = toPosition.getSquare();
                    int[] positionValues = getPositionValues(piece);
                    evaluationByMoveToEmptySquare += Color.WHITE.equals(piece.getColor()) ? positionValues[toSquare.toIdx()] : -positionValues[toSquare.getMirrorSquare().toIdx()];
                } else {
                    evaluationByAttack -= getPieceValue(toPosition.getPiece());
                }

            }
        }

        // From white point of view
        return expansion * evaluationByMoveToEmptySquare + attack * evaluationByAttack;
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

    protected int[] getPositionValues(Piece piece) {
        return switch (piece) {
            case PAWN_WHITE, PAWN_BLACK -> PAWN_WHITE_VALUES;
            case KNIGHT_WHITE, KNIGHT_BLACK -> KNIGHT_WHITE_VALUES;
            case BISHOP_WHITE, BISHOP_BLACK -> BISHOP_WHITE_VALUES;
            case ROOK_WHITE, ROOK_BLACK -> ROOK_WHITE_VALUES;
            case QUEEN_WHITE, QUEEN_BLACK -> QUEEN_WHITE_VALUES;
            case KING_WHITE, KING_BLACK -> KING_WHITE_VALUES;
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
                pseudoMovesGenerator = moveGenerator;
            }

        });
    }

}
