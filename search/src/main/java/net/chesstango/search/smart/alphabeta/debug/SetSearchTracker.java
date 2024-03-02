package net.chesstango.search.smart.alphabeta.debug;

import net.chesstango.search.SearchByDepthResult;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.*;

/**
 * @author Mauricio Coria
 */
public class SetSearchTracker implements SearchByCycleListener, SearchByDepthListener, SearchByWindowsListener {
    private SearchTracker searchTracker;
    private final DebugNodeTrap debugNodeTrap;

    public SetSearchTracker(DebugNodeTrap debugNodeTrap) {
        this.debugNodeTrap = debugNodeTrap;
    }

    @Override
    public void beforeSearch(SearchByCycleContext context) {
        searchTracker = new SearchTracker();
        searchTracker.setGame(context.getGame());

        context.setSearchTracker(searchTracker);

        if (debugNodeTrap != null && debugNodeTrap instanceof SearchByCycleListener debugNodeTrapSearchByCycleListener) {
            debugNodeTrapSearchByCycleListener.beforeSearch(context);
        }
    }

    @Override
    public void afterSearch(SearchMoveResult searchMoveResult) {
        if (debugNodeTrap != null && debugNodeTrap instanceof SearchByCycleListener debugNodeTrapSearchByCycleListener) {
            debugNodeTrapSearchByCycleListener.afterSearch(searchMoveResult);
        }
    }

    @Override
    public void beforeSearchByDepth(SearchByDepthContext context) {
        if (debugNodeTrap != null && debugNodeTrap instanceof SearchByDepthListener debugNodeTrapSearchByDepthListener) {
            debugNodeTrapSearchByDepthListener.beforeSearchByDepth(context);
        }
        searchTracker.reset();
    }

    @Override
    public void afterSearchByDepth(SearchByDepthResult result) {
        if (debugNodeTrap != null && debugNodeTrap instanceof SearchByDepthListener debugNodeTrapSearchByDepthListener) {
            debugNodeTrapSearchByDepthListener.afterSearchByDepth(result);
        }
    }

    @Override
    public void beforeSearchByWindows(int alphaBound, int betaBound, int searchByWindowsCycle) {
        searchTracker.reset();
    }

}
