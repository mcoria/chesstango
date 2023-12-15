package net.chesstango.search.smart;

import lombok.Getter;
import net.chesstango.search.SearchMoveResult;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class SmartListenerMediator {

    @Getter
    private List<SearchByCycleListener> searchByCycleListeners = new LinkedList<>();

    @Getter
    private List<SearchByDepthListener> searchByDepthListeners = new LinkedList<>();

    @Getter
    private List<StopSearchingListener> stopSearchingListeners = new LinkedList<>();

    @Getter
    private List<ResetListener> resetListeners = new LinkedList<>();


    public void triggerBeforeSearch(SearchByCycleContext context) {
        searchByCycleListeners.forEach(filter -> filter.beforeSearch(context));
    }


    public void triggerAfterSearch() {
        searchByCycleListeners.forEach(SearchByCycleListener::afterSearch);
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

        if (searchByCycleListeners.contains(listener) ||
                searchByDepthListeners.contains(listener) ||
                stopSearchingListeners.contains(listener) ||
                resetListeners.contains(listener)) {
            throw new RuntimeException(String.format("Listener already added %s", listener));
        }

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
