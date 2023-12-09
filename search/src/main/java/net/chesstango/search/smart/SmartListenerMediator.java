package net.chesstango.search.smart;

import net.chesstango.board.Game;
import net.chesstango.search.SearchMoveResult;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class SmartListenerMediator {

    private List<SearchByCycleListener> searchByCycleListeners = new LinkedList<>();

    private List<SearchByDepthListener> searchByDepthListeners = new LinkedList<>();

    private List<StopSearchingListener> stopSearchingListeners = new LinkedList<>();

    private List<ResetListener> resetListeners = new LinkedList<>();


    public void triggerBeforeSearch(Game game) {
        searchByCycleListeners.forEach(filter -> filter.beforeSearch(game));
    }


    public void triggerAfterSearch(SearchMoveResult result) {
        searchByCycleListeners.forEach(filter -> filter.afterSearch(result));
    }


    public void triggerBeforeSearchByDepth(SearchByDepthContext context) {
        searchByDepthListeners.forEach(filter -> filter.beforeSearchByDepth(context));
    }


    public void triggerAfterSearchByDepth(SearchMoveResult result) {
        searchByDepthListeners.forEach(filter -> filter.afterSearchByDepth(result));
    }

    public void triggerStopSearching() {
        stopSearchingListeners.forEach(StopSearchingListener::stopSearching);
    }

    public void triggerReset() {
        resetListeners.forEach(ResetListener::reset);
    }

    public void add(SmartListener listener) {
        if (listener instanceof SearchByCycleListener searchByCycleListener) {
            searchByCycleListeners.add(searchByCycleListener);
        }

        if (listener instanceof SearchByDepthListener searchByDepthListener) {
            searchByDepthListeners.add(searchByDepthListener);
        }

        if (listener instanceof StopSearchingListener stopSearchingListener) {
            stopSearchingListeners.add(stopSearchingListener);
        }

        if (listener instanceof ResetListener resetListener) {
            resetListeners.add(resetListener);
        }
    }

    public void addAll(List<SmartListener> listeners) {
        listeners.forEach(this::add);
    }
}
