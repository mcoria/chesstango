package net.chesstango.search.sorters;

import net.chesstango.search.smart.SearchListener;

/**
 * @author Mauricio Coria
 */
public interface SortListener extends SearchListener {
    void beforeSort(int currentPly);

    default void afterSort(){
    }
}
