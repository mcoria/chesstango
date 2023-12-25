package net.chesstango.search.smart;

/**
 * @author Mauricio Coria
 */
public interface SearchByWindowsListener extends SmartListener {
    void beforeSearchByWindows(int alphaBound, int betaBound, int searchByWindowsCycle);

    void afterSearchByWindows(boolean searchByWindowsFinished);
}
