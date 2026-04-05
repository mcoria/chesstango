package net.chesstango.search.smart.sorters;

/**
 * @author Mauricio Coria
 */
public interface SortListener {
    void beforeSort(int currentPly);

    void afterSort(int currentPly);
}
