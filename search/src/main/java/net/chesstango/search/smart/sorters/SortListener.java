package net.chesstango.search.smart.sorters;

import net.chesstango.search.smart.SearchListener;

/**
 * @author Mauricio Coria
 */
public interface SortListener extends SearchListener {
    void beforeSort(int currentPly);

    void afterSort();
}
