package net.chesstango.evaluation.evaluators;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.board.position.ChessPositionReader;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Mauricio Coria
 */
public class EvaluatorImp01 extends AbstractEvaluator {
    private static final int FACTOR_MATERIAL_DEFAULT = 628;
    private static final int FACTOR_EXPANSION_DEFAULT = 288;
    private static final int FACTOR_ATAQUE_DEFAULT = 84;

    private final int material;
    private final int expansion;
    private final int ataque;

    public EvaluatorImp01() {
        this(FACTOR_MATERIAL_DEFAULT, FACTOR_EXPANSION_DEFAULT, FACTOR_ATAQUE_DEFAULT);
    }

    public EvaluatorImp01(int material, int expansion, int ataque) {
        this.material = material;
        this.expansion = expansion;
        this.ataque = ataque;
    }

    @Override
    public int evaluateNonFinalStatus() {
        int evaluation = 0;
        switch (game.getStatus()) {
            case CHECK:
                // If white is on check then evaluation starts at -1
                evaluation = Color.WHITE.equals(game.getPosition().getCurrentTurn()) ? -1 : +1;
            case NO_CHECK:
                evaluation += material * 10 * evaluateByMaterial();
                evaluation += evaluateByMoves();
                break;
            default:
                throw new RuntimeException("Invalid Status");
        }
        return evaluation;
    }

    protected int evaluateByMoves() {
        int evaluation = 0;
        Set<Square> origenes = new HashSet<>();
        Set<Square> territorioExpansion = new HashSet<>();
        Set<Square> territorioAtaque = new HashSet<>();
        int posiblesCapturasValor = 0;
        for (Move move : game.getPossibleMoves()) {
            origenes.add(move.getFrom().getSquare());

            PiecePositioned to = move.getTo();

            territorioExpansion.add(to.getSquare());

            if (to.getPiece() != null) {
                territorioAtaque.add(to.getSquare());
                posiblesCapturasValor += Math.abs(getPieceValue(to.getPiece()));
            }
        }

        evaluation = origenes.size() + expansion * territorioExpansion.size() + ataque * territorioAtaque.size() + posiblesCapturasValor;

        return (Color.WHITE.equals(game.getPosition().getCurrentTurn())) ? +evaluation : -evaluation;
    }


    protected int getPieceValue(Piece piece) {
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
