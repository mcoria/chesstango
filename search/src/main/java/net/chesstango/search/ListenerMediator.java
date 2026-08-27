package net.chesstango.search;

import lombok.Getter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
@Getter
public class ListenerMediator implements Acceptor {

    private final List<SearchListener> searchListeners = new ArrayList<>();

    private final List<SearchByDepthListener> searchByDepthListeners = new ArrayList<>();

    private final List<SearchByWindowsListener> searchByWindowsListeners = new ArrayList<>();

    private final List<StopSearchingListener> stopSearchingListeners = new ArrayList<>();

    private final List<ResetListener> resetListeners = new ArrayList<>();

    private final List<Acceptor> acceptors = new ArrayList<>();

    @Override
    public void accept(Visitor visitor) {
        acceptors.forEach(acceptor -> acceptor.accept(visitor));
    }

    public void triggerBeforeSearch() {
        searchListeners.forEach(SearchListener::beforeSearch);
    }

    public void triggerAfterSearch() {
        searchListeners.forEach(SearchListener::afterSearch);
    }


    public void triggerBeforeSearchByDepth() {
        searchByDepthListeners.forEach(SearchByDepthListener::beforeSearchByDepth);
    }

    public void triggerAfterSearchByDepth(boolean searchStopped) {
        searchByDepthListeners.forEach(searchByDepthListener -> searchByDepthListener.afterSearchByDepth(searchStopped));
    }

    public void triggerBeforeSearchByWindows(int alphaBound, int betaBound, int searchByWindowsCycle) {
        searchByWindowsListeners.forEach(filter -> filter.beforeSearchByWindows(alphaBound, betaBound, searchByWindowsCycle));
    }

    public void triggerAfterSearchByWindows(boolean searchStopped) {
        searchByWindowsListeners.forEach(filter -> filter.afterSearchByWindows(searchStopped));
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
        if (object instanceof Listener listener) {
            addSearchListener(listener);
        }
    }

    private void addAcceptor(Acceptor acceptor) {
        if (acceptors.contains(acceptor)) {
            throw new RuntimeException(String.format("Acceptor already added %s", acceptor));
        }

        acceptors.add(acceptor);
    }

    private void addSearchListener(Listener listener) {
        if (listener instanceof SearchListener searchListener) {
            if (searchListeners.contains(searchListener)) {
                throw new RuntimeException(String.format("SearchByCycleListener already added %s", searchListener));
            }
            searchListeners.add(searchListener);
        }

        if (listener instanceof SearchByDepthListener searchByDepthListener) {
            if (searchByDepthListeners.contains(searchByDepthListener)) {
                throw new RuntimeException(String.format("SearchByDepthListener already added %s", searchByDepthListener));
            }
            searchByDepthListeners.add(searchByDepthListener);
        }

        if (listener instanceof SearchByWindowsListener searchByWindowsListener) {
            if (searchByWindowsListeners.contains(searchByWindowsListener)) {
                throw new RuntimeException(String.format("SearchByWindowsListener already added %s", searchByWindowsListener));
            }
            searchByWindowsListeners.add(searchByWindowsListener);
        }

        if (listener instanceof StopSearchingListener stopSearchingListener) {
            if (stopSearchingListeners.contains(stopSearchingListener)) {
                throw new RuntimeException(String.format("StopSearchingListener already added %s", stopSearchingListener));
            }
            stopSearchingListeners.add(stopSearchingListener);
        }

        if (listener instanceof ResetListener resetListener) {
            if (resetListeners.contains(resetListener)) {
                throw new RuntimeException(String.format("ResetListener already added %s", resetListener));
            }
            resetListeners.add(resetListener);
        }
    }

    public void addAll(List<?> objects) {
        for (Object object : objects) {
            add(object);
        }
    }
}
