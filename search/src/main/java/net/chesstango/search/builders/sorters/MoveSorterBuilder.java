package net.chesstango.search.builders.sorters;

import net.chesstango.search.smart.SearchListenerMediator;
import net.chesstango.search.smart.sorters.MoveSorter;

/**
 * @author Mauricio Coria
 */
public interface MoveSorterBuilder {
    MoveSorter build();

    MoveSorterBuilder withIterativeDeepening();

    MoveSorterBuilder withDebugSearchTree();

    MoveSorterBuilder withSmartListenerMediator(SearchListenerMediator searchListenerMediator);
}
