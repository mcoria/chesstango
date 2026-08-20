package net.chesstango.search.smart;

/**
 * @author Mauricio Coria
 */
@FunctionalInterface
public interface AlphaBetaFunction {
    int search(final int currentPly, final int alpha, final int beta);
}
