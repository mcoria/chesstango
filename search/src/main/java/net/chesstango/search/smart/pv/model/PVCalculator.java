package net.chesstango.search.smart.pv.model;

import net.chesstango.search.PrincipalVariation;

/**
 * @author Mauricio Coria
 */
public interface PVCalculator {
    /**
     * Los parametros corresponden al segundo movimiento de la PV
     *
     * @param eval
     */
    PrincipalVariation calculatePrincipalVariation(int eval);
}
