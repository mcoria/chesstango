package net.chesstango.search.smart;

/**
 * @author Mauricio Coria
 */
public interface ResetListener extends SearchListener {

    /**
     * Reset internal buffers
     */
    void reset();
}
