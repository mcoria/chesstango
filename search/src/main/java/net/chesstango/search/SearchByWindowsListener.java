package net.chesstango.search;

/**
 * @author Mauricio Coria
 */
public interface SearchByWindowsListener extends Listener {
    void beforeSearchByWindows(int alphaBound, int betaBound, int searchByWindowsCycle);

    default void afterSearchByWindows(boolean searchStopped) {
    }
}
