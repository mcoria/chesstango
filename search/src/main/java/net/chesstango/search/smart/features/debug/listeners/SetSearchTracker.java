package net.chesstango.search.smart.features.debug.listeners;

import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.search.Acceptor;
import net.chesstango.search.SearchResultByDepth;
import net.chesstango.search.SearchResult;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.*;
import net.chesstango.search.smart.features.debug.DebugNodeTrap;
import net.chesstango.search.smart.features.debug.SearchTracker;
import net.chesstango.search.visitors.SetSearchTrackerVisitor;

/**
 * @author Mauricio Coria
 */
public class SetSearchTracker implements SearchByCycleListener, SearchByDepthListener, SearchByWindowsListener, Acceptor {
    private final DebugNodeTrap debugNodeTrap;

    private SearchTracker searchTracker;

    @Setter
    private Game game;

    @Setter
    private SearchListenerMediator searchListenerMediator;

    public SetSearchTracker(DebugNodeTrap debugNodeTrap) {
        this.debugNodeTrap = debugNodeTrap;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void beforeSearch() {
        searchTracker = new SearchTracker();
        searchTracker.setGame(game);

        searchListenerMediator.accept(new SetSearchTrackerVisitor(searchTracker));

        if (debugNodeTrap != null && debugNodeTrap instanceof SearchByCycleListener debugNodeTrapSearchByCycleListener) {
            debugNodeTrapSearchByCycleListener.beforeSearch();
        }
    }

    @Override
    public void afterSearch(SearchResult result) {
        if (debugNodeTrap != null && debugNodeTrap instanceof SearchByCycleListener debugNodeTrapSearchByCycleListener) {
            debugNodeTrapSearchByCycleListener.afterSearch(result);
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
    public void afterSearchByDepth(SearchResultByDepth result) {
        if (debugNodeTrap != null && debugNodeTrap instanceof SearchByDepthListener debugNodeTrapSearchByDepthListener) {
            debugNodeTrapSearchByDepthListener.afterSearchByDepth(result);
        }
    }

    @Override
    public void beforeSearchByWindows(int alphaBound, int betaBound, int searchByWindowsCycle) {
        searchTracker.reset();
    }

}
