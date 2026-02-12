package net.chesstango.search.smart.features.pv;

/**
 * @author Mauricio Coria
 */
public interface PVReader {
    void readPrincipalVariation(short bestMove, int bestValue);
}
