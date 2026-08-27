package net.chesstango.search.builders.sorters;

import net.chesstango.search.ListenerMediator;
import net.chesstango.search.sorters.MoveSorter;

/**
 * @author Mauricio Coria
 */
public interface MoveSorterBuilder {
    MoveSorter build();

    MoveSorterBuilder withIterativeDeepening();

    MoveSorterBuilder withDebugSearchTree();

    MoveSorterBuilder withSmartListenerMediator(ListenerMediator listenerMediator);
}
