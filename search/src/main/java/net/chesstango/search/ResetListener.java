package net.chesstango.search;

/**
 * @author Mauricio Coria
 */
public interface ResetListener extends SearchListener {

    /**
     * Reset internal buffers
     */
    void reset();
}
