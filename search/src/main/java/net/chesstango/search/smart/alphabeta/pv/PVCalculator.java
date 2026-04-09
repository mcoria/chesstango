package net.chesstango.search.smart.alphabeta.pv;

/**
 * @author Mauricio Coria
 */
public interface PVCalculator {
    /**
     * Los parametros corresponden al segundo movimiento de la PV
     * @param secondMovePV This is the second move of the PV
     * @param bestValue
     */
    void calculatePrincipalVariation(short secondMovePV, int bestValue);
}
