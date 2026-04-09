package net.chesstango.search.smart.alphabeta.pv;

/**
 * @author Mauricio Coria
 */
public interface PVCalculator {
    /**
     * Los parametros corresponden al segundo movimiento de la PV
     * @param bestMove This is the second move of the PV
     * @param bestValue
     */
    void calculatePrincipalVariation(short bestMove, int bestValue);
}
