package net.chesstango.search.visitors;

import net.chesstango.search.Visitor;
import net.chesstango.search.smart.SearchListenerMediator;
import net.chesstango.search.smart.features.debug.listeners.SetSearchTracker;

/**
 *
 * @author Mauricio Coria
 */
public class SetSearchListenerMediator implements Visitor {
    private final SearchListenerMediator searchListenerMediator;

    public SetSearchListenerMediator(SearchListenerMediator searchListenerMediator) {
        this.searchListenerMediator = searchListenerMediator;
    }

    @Override
    public void visit(SetSearchTracker setSearchTracker) {
        setSearchTracker.setSearchListenerMediator(searchListenerMediator);
    }
}
