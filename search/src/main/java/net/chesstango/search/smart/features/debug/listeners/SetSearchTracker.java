package net.chesstango.search.smart.features.debug.listeners;

import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.SearchByDepthListener;
import net.chesstango.search.smart.SearchByWindowsListener;
import net.chesstango.search.smart.SearchListenerMediator;
import net.chesstango.search.smart.features.debug.SearchTracker;
import net.chesstango.search.visitors.SetSearchTrackerVisitor;

/**
 * @author Mauricio Coria
 */
public class SetSearchTracker implements SearchByCycleListener, SearchByDepthListener, SearchByWindowsListener, Acceptor {
    private SearchTracker searchTracker;

    @Setter
    private Game game;

    @Setter
    private SearchListenerMediator searchListenerMediator;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void beforeSearch() {
        searchTracker = new SearchTracker();

        searchTracker.setGame(game);

        searchListenerMediator.accept(new SetSearchTrackerVisitor(searchTracker));
    }

    @Override
    public void beforeSearchByDepth() {
        searchTracker.reset();
    }

    @Override
    public void beforeSearchByWindows(int alphaBound, int betaBound, int searchByWindowsCycle) {
        searchTracker.reset();
    }
}
