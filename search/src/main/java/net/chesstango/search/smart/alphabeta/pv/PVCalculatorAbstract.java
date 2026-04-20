package net.chesstango.search.smart.alphabeta.pv;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.search.PrincipalVariation;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.alphabeta.egtb.EndGameTableBase;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public abstract class PVCalculatorAbstract implements PVCalculator, SearchByCycleListener {

    @Setter
    protected Evaluator evaluator;

    @Setter
    protected EndGameTableBase endGameTableBase;

    @Setter
    protected Game game;

    @Getter
    @Setter(AccessLevel.PACKAGE)
    protected boolean pvComplete;

    @Getter
    @Setter(AccessLevel.PACKAGE)
    protected List<PrincipalVariation> principalVariation;

    @Override
    public void beforeSearch() {
        principalVariation = null;
        pvComplete = false;
    }

    protected abstract List<PrincipalVariation> walkPrincipalVariation(List<PrincipalVariation> principalVariationList, int eval);

    @Override
    public void calculatePrincipalVariation(int eval) {
        List<PrincipalVariation> principalVariationList = new ArrayList<>();

        // Cada vez que recalculamos Principal Variation
        this.principalVariation = walkPrincipalVariation(principalVariationList, eval);
        this.pvComplete = validatePrincipalVariation(eval);

        // Rewind game
        principalVariationList.reversed().stream().map(PrincipalVariation::move).forEach(Move::undoMove);
    }


    protected boolean validatePrincipalVariation(int eval) {
        boolean isPVComplete = false;

        int sign = principalVariation.size() % 2 == 0 ? 1 : -1;

        int pvEvaluation = 0;

        // En caso que se llegó a loop
        if (game.getState().getRepetitionCounter() > 1) {
            // Nothing to do
        } else if (endGameTableBase.isProbeAvailable()) {
            pvEvaluation = sign * (Color.WHITE.equals(game.getPosition().getCurrentTurn()) ? endGameTableBase.evaluate() : -endGameTableBase.evaluate());
        } else {
            pvEvaluation = sign * (Color.WHITE.equals(game.getPosition().getCurrentTurn()) ? evaluator.evaluate() : -evaluator.evaluate());
        }

        // No se debe considerar DEPH dado que al entrar en LOOP la cantidad de movimientos PVs puede ser menor a DEPTH
        if (eval == pvEvaluation) {
            isPVComplete = true;
        }

        return isPVComplete;
    }

    protected Move getMove(short moveEncoded) {
        for (Move posibleMove : game.getPossibleMoves()) {
            if (posibleMove.binaryEncoding() == moveEncoded) {
                return posibleMove;
            }
        }
        return null;
    }
}
