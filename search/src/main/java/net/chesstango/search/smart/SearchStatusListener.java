package net.chesstango.search.smart;

/**
 * @author Mauricio Coria
 */
public interface SearchStatusListener {
    void info(int depth, int selDepth, String pv);
}
