package net.chesstango.search.builders;

import net.chesstango.search.smart.SearchListenerMediator;

/**
 * @author Mauricio Corias
 */
public interface SearchObjectBuilder<T extends SearchObjectBuilder<T>> {

    T withSmartListenerMediator(SearchListenerMediator searchListenerMediator);

    void build();

    void link();
}
