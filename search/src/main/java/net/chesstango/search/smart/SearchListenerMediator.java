package net.chesstango.search.smart;

import lombok.Getter;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Visitor;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
@Getter
public class SearchListenerMediator implements Acceptor {

    private final List<SearchByCycleListener> searchByCycleListeners = new LinkedList<>();

    private final List<SearchByDepthListener> searchByDepthListeners = new LinkedList<>();

    private final List<SearchByWindowsListener> searchByWindowsListeners = new LinkedList<>();

    private final List<StopSearchingListener> stopSearchingListeners = new LinkedList<>();

    private final List<ResetListener> resetListeners = new LinkedList<>();

    private final List<Acceptor> acceptors = new LinkedList<>();

    @Override
    public void accept(Visitor visitor) {
        acceptors.forEach(acceptor -> acceptor.accept(visitor));
    }

    public void triggerBeforeSearch() {
        searchByCycleListeners.forEach(SearchByCycleListener::beforeSearch);
    }

    public void triggerAfterSearch() {
        searchByCycleListeners.forEach(SearchByCycleListener::afterSearch);
    }


    public void triggerBeforeSearchByDepth() {
        searchByDepthListeners.forEach(SearchByDepthListener::beforeSearchByDepth);
    }

    public void triggerAfterSearchByDepth() {
        searchByDepthListeners.forEach(SearchByDepthListener::afterSearchByDepth);
    }

    public void triggerBeforeSearchByWindows(int alphaBound, int betaBound, int searchByWindowsCycle) {
        searchByWindowsListeners.forEach(filter -> filter.beforeSearchByWindows(alphaBound, betaBound, searchByWindowsCycle));
    }

    public void triggerAfterSearchByWindows(boolean searchByWindowsFinished) {
        searchByWindowsListeners.forEach(filter -> filter.afterSearchByWindows(searchByWindowsFinished));
    }

    public void triggerStopSearching() {
        stopSearchingListeners.forEach(StopSearchingListener::stopSearching);
    }

    public void triggerReset() {
        resetListeners.forEach(ResetListener::reset);
    }

    public void add(Object object) {
        if (object instanceof Acceptor acceptor) {
            addAcceptor(acceptor);
        }
        if (object instanceof SearchListener searchListener) {
            addSearchListener(searchListener);
        }
    }

    private void addAcceptor(Acceptor acceptor) {
        if (acceptors.contains(acceptor)) {
            throw new RuntimeException(String.format("Acceptor already added %s", acceptor));
        }

        acceptors.add(acceptor);
    }

    private void addSearchListener(SearchListener searchListener) {
        if (searchListener instanceof SearchByCycleListener searchByCycleListener) {
            if (searchByCycleListeners.contains(searchByCycleListener)) {
                throw new RuntimeException(String.format("SearchByCycleListener already added %s", searchByCycleListener));
            }
            searchByCycleListeners.add(searchByCycleListener);
        }

        if (searchListener instanceof SearchByDepthListener searchByDepthListener) {
            if (searchByDepthListeners.contains(searchByDepthListener)) {
                throw new RuntimeException(String.format("SearchByDepthListener already added %s", searchByDepthListener));
            }
            searchByDepthListeners.add(searchByDepthListener);
        }

        if (searchListener instanceof SearchByWindowsListener searchByWindowsListener) {
            if (searchByWindowsListeners.contains(searchByWindowsListener)) {
                throw new RuntimeException(String.format("SearchByWindowsListener already added %s", searchByWindowsListener));
            }
            searchByWindowsListeners.add(searchByWindowsListener);
        }

        if (searchListener instanceof StopSearchingListener stopSearchingListener) {
            if (stopSearchingListeners.contains(stopSearchingListener)) {
                throw new RuntimeException(String.format("StopSearchingListener already added %s", stopSearchingListener));
            }
            stopSearchingListeners.add(stopSearchingListener);
        }

        if (searchListener instanceof ResetListener resetListener) {
            if (resetListeners.contains(resetListener)) {
                throw new RuntimeException(String.format("ResetListener already added %s", resetListener));
            }
            resetListeners.add(resetListener);
        }
    }
}
