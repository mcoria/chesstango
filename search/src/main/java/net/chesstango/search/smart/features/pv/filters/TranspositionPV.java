package net.chesstango.search.smart.features.pv.filters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFilter;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaHelper;
import net.chesstango.search.smart.features.pv.PVReader;

/**
 * Este filtro se ejecuta luego de AlphaBeta para capturar tempranamente el PV.
 * Busquedas sucesivas a PV ocacionan que TT se ensucie y su precision disminuya considerablemente
 *
 * @author Mauricio Coria
 */
public class TranspositionPV implements AlphaBetaFilter {

    @Setter
    @Getter
    private AlphaBetaFilter next;

    @Setter
    private PVReader ttPvReader;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public long maximize(int currentPly, int alpha, int beta) {
        long moveAndValue = next.maximize(currentPly, alpha, beta);

        return process(alpha, beta, moveAndValue);
    }

    @Override
    public long minimize(int currentPly, int alpha, int beta) {
        long moveAndValue = next.minimize(currentPly, alpha, beta);

        return process(alpha, beta, moveAndValue);
    }


    protected long process(int alpha, int beta, long moveAndValue) {
        final short currentMove = AlphaBetaHelper.decodeMove(moveAndValue);
        final int currentValue = AlphaBetaHelper.decodeValue(moveAndValue);

        if (alpha < currentValue && currentValue < beta) {
            ttPvReader.readPrincipalVariation(currentMove, currentValue);
        }

        return moveAndValue;
    }

}
