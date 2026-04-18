package net.chesstango.search.smart.alphabeta.pv.filters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.alphabeta.AlphaBetaFilter;
import net.chesstango.search.smart.alphabeta.pv.PVCalculator;

/**
 * Este filtro se ejecuta UNICAMENTE luego de AlphaBeta de Root Node para capturar tempranamente el PV.
 * Busquedas sucesivas ocacionan que TT se ensucie y no se logre reconstruir PV
 *
 * @author Mauricio Coria
 */
@Setter
@Getter
public class TriggerPVCalculation implements AlphaBetaFilter, Acceptor {

    private AlphaBetaFilter next;

    private PVCalculator pvCalculator;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int alphaBeta(int currentPly, int alpha, int beta) {
        int currentValue = next.alphaBeta(currentPly, alpha, beta);

        return process(alpha, beta, currentValue);
    }


    /**
     * Decodes move/value; reads principal variation if within alpha-beta window
     */
    protected int process(int alpha, int beta, int currentValue) {

        if (alpha < currentValue && currentValue < beta) {
            pvCalculator.calculatePrincipalVariation(currentValue);
        }

        return currentValue;
    }

}
