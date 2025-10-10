package net.chesstango.search.smart;

import lombok.Getter;
import net.chesstango.search.Acceptor;
import net.chesstango.search.SearchResult;
import net.chesstango.search.SearchResultByDepth;
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
        searchByCycleListeners.forEach(filter -> filter.beforeSearch());
    }

    public void triggerAfterSearch(SearchResult result) {
        searchByCycleListeners.forEach(searchByCycleListener -> searchByCycleListener.afterSearch(result));
    }


    public void triggerBeforeSearchByDepth() {
        searchByDepthListeners.forEach(SearchByDepthListener::beforeSearchByDepth);
    }

    public void triggerAfterSearchByDepth(SearchResultByDepth result) {
        searchByDepthListeners.forEach(filter -> filter.afterSearchByDepth(result));
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

    public void add(SearchListener listener) {
        if (searchByCycleListeners.contains(listener) ||
                searchByDepthListeners.contains(listener) ||
                searchByWindowsListeners.contains(listener) ||
                stopSearchingListeners.contains(listener) ||
                resetListeners.contains(listener)
        ) {
            throw new RuntimeException(String.format("Listener already added %s", listener));
        }

        if (listener instanceof SearchByCycleListener searchByCycleListener) {
            searchByCycleListeners.add(searchByCycleListener);
        }

        if (listener instanceof SearchByDepthListener searchByDepthListener) {
            searchByDepthListeners.add(searchByDepthListener);
        }

        if (listener instanceof SearchByWindowsListener searchByWindowsListener) {
            searchByWindowsListeners.add(searchByWindowsListener);
        }

        if (listener instanceof StopSearchingListener stopSearchingListener) {
            stopSearchingListeners.add(stopSearchingListener);
        }

        if (listener instanceof ResetListener resetListener) {
            resetListeners.add(resetListener);
        }

        if (listener instanceof Acceptor acceptor) {
            addAcceptor(acceptor);
        }
    }

    public void addAll(List<SearchListener> listeners) {
        listeners.forEach(this::add);
    }

    public void addAcceptor(Acceptor acceptor) {
        if (acceptors.contains(acceptor)
        ) {
            throw new RuntimeException(String.format("Acceptor already added %s", acceptor));
        }

        acceptors.add(acceptor);
    }

    public void addAllAcceptor(List<Acceptor> listeners) {
        listeners.forEach(this::addAcceptor);
    }
}
