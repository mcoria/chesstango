package net.chesstango.search.builders;

import net.chesstango.search.ListenerMediator;

/**
 * @author Mauricio Corias
 */
public interface SearchObjectBuilder<T extends SearchObjectBuilder<T>> {

    T withSmartListenerMediator(ListenerMediator listenerMediator);

    void build();

    void link();
}
