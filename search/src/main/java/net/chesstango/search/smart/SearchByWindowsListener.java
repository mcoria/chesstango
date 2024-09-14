package net.chesstango.search.smart;

/**
 * @author Mauricio Coria
 */
public interface SearchByWindowsListener extends SearchListener {
    void beforeSearchByWindows(int alphaBound, int betaBound, int searchByWindowsCycle);

    default void afterSearchByWindows(boolean searchByWindowsFinished) {
    }
}
