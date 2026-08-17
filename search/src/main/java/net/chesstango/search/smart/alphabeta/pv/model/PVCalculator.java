package net.chesstango.search.smart.alphabeta.pv.model;

/**
 * @author Mauricio Coria
 */
public interface PVCalculator {
    /**
     * Los parametros corresponden al segundo movimiento de la PV
     *
     * @param eval
     */
    void calculatePrincipalVariation(int eval);
}
