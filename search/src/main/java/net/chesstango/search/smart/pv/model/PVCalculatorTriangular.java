package net.chesstango.search.smart.pv.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.search.Acceptor;
import net.chesstango.search.PrincipalVariation;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.egtb.EndGameTableBase;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class PVCalculatorTriangular implements PVCalculator, SearchByCycleListener, Acceptor {

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

    @Setter
    private TriangularPVTable trianglePV;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void beforeSearch() {
        principalVariation = null;
        pvComplete = false;
    }

    @Override
    public void calculatePrincipalVariation(int eval) {
        // Cada vez que recalculamos Principal Variation
        this.principalVariation = walkPrincipalVariation();

        this.pvComplete = validatePrincipalVariation(eval);

        // Rewind game
        principalVariation
                .reversed()
                .stream()
                .map(PrincipalVariation::move)
                .forEach(Move::undoMove);
    }


    protected List<PrincipalVariation> walkPrincipalVariation() {
        // Comenzar de ROOT + 1
        int pvMoveCounter = 1;

        Move[] pvMoves = trianglePV.getRootPV();

        List<PrincipalVariation> principalVariationList = new ArrayList<>(pvMoves.length);

        // First PV move
        while (pvMoveCounter < pvMoves.length) {
            long currentHash = game.getPosition().getZobristHash();

            Move currentMove = pvMoves[pvMoveCounter++];

            principalVariationList.add(new PrincipalVariation(currentHash, currentMove));

            currentMove.executeMove();
        }

        return principalVariationList;
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

}
