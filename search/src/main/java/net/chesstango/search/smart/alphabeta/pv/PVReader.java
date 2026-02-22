package net.chesstango.search.smart.alphabeta.pv;

/**
 * @author Mauricio Coria
 */
public interface PVReader {
    /**
     * Los parametros corresponden al segundo movimiento de la PV
     * @param bestMove
     * @param bestValue
     */
    void readPrincipalVariation(short bestMove, int bestValue);
}
