package net.chesstango.ai.imp.smart.evaluation.imp;

import net.chesstango.ai.imp.smart.evaluation.GameEvaluator;
import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Mauricio Coria
 */
public class GameEvaluatorImp01 implements GameEvaluator {

    private static final int FACTOR_MATERIAL_DEFAULT = 979;
    private static final int FACTOR_EXPANSION_DEFAULT = 19;
    private static final int FACTOR_ATAQUE_DEFAULT = 2;

    private final int material;
    private final int expansion;
    private final int ataque;

    public GameEvaluatorImp01() {
        this(FACTOR_MATERIAL_DEFAULT, FACTOR_EXPANSION_DEFAULT, FACTOR_ATAQUE_DEFAULT);
    }

    public GameEvaluatorImp01(int material, int expansion, int ataque) {
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
                evaluation = GameEvaluator.evaluateFinalStatus(game);
                break;
            case CHECK:
                // If white is on check then evaluation starts at -1
                evaluation = Color.WHITE.equals(game.getChessPosition().getCurrentTurn()) ? -1 : +1;
            case NO_CHECK:
                evaluation +=  material * GameEvaluator.evaluateByMaterial(game);
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
            origenes.add(move.getFrom().getKey());

            PiecePositioned to = move.getTo();

            territorioExpansion.add(to.getKey());

            if (to.getValue() != null) {
                territorioAtaque.add(to.getKey());
                posiblesCapturasValor += Math.abs(to.getValue().getPieceValue());
            }
        }

        evaluation = origenes.size() + expansion * territorioExpansion.size() + ataque * territorioAtaque.size() + posiblesCapturasValor;

        return (Color.WHITE.equals(game.getChessPosition().getCurrentTurn())) ? +evaluation : -evaluation;
    }

}
