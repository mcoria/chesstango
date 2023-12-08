package net.chesstango.search.smart;

import net.chesstango.board.Game;
import net.chesstango.search.SearchMoveResult;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class SmartListenerMediator {

    private List<SearchCycleListener> searchCycleListeners = new LinkedList<>();

    private List<SearchByDepthListener> searchByDepthListeners = new LinkedList<>();

    private List<StopSearchListener> stopSearchListeners = new LinkedList<>();

    private List<ResetListener> resetListeners = new LinkedList<>();


    public void triggerBeforeSearch(Game game) {
        searchCycleListeners.forEach(filter -> filter.beforeSearch(game));
    }


    public void triggerAfterSearch(SearchMoveResult result) {
        searchCycleListeners.forEach(filter -> filter.afterSearch(result));
    }


    public void triggerBeforeSearchByDepth(SearchContext context) {
        searchByDepthListeners.forEach(filter -> filter.beforeSearchByDepth(context));
    }


    public void triggerAfterSearchByDepth(SearchMoveResult result) {
        searchByDepthListeners.forEach(filter -> filter.afterSearchByDepth(result));
    }

    public void triggerStopSearching() {
        stopSearchListeners.forEach(StopSearchListener::stopSearching);
    }

    public void triggerReset() {
        resetListeners.forEach(ResetListener::reset);
    }

    public void add(SmartListener listener) {
        if (listener instanceof SearchCycleListener searchCycleListener) {
            searchCycleListeners.add(searchCycleListener);
        }

        if (listener instanceof SearchByDepthListener searchByDepthListener) {
            searchByDepthListeners.add(searchByDepthListener);
        }

        if (listener instanceof StopSearchListener stopSearchListener) {
            stopSearchListeners.add(stopSearchListener);
        }

        if (listener instanceof ResetListener resetListener) {
            resetListeners.add(resetListener);
        }
    }

    public void addAll(List<SmartListener> listeners) {
        listeners.forEach(this::add);
    }
}
