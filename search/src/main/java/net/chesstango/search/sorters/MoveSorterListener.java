package net.chesstango.search.sorters;

import net.chesstango.search.Listener;

/**
 * @author Mauricio Coria
 */
public interface MoveSorterListener extends Listener {
    void beforeSort(int currentPly);

    default void afterSort(){
    }
}
