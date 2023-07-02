package net.chesstango.evaluation.evaluators;

import net.chesstango.board.*;
import net.chesstango.board.moves.Move;

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
    public int evaluate(final Game game) {
        int evaluation = 0;
        switch (game.getStatus()) {
            case MATE:
            case DRAW:
                evaluation = evaluateFinalStatus(game);
                break;
            case CHECK:
                // If white is on check then evaluation starts at -1
                evaluation = Color.WHITE.equals(game.getChessPosition().getCurrentTurn()) ? -1 : +1;
            case NO_CHECK:
                evaluation += material * 10 * evaluateByMaterial(game);
                evaluation += evaluateByMoves(game);
        }
        return evaluation;
    }

    protected int evaluateByMoves(final Game game) {
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

        return (Color.WHITE.equals(game.getChessPosition().getCurrentTurn())) ? +evaluation : -evaluation;
    }

    @Override
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
}
