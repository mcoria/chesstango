package net.chesstango.engine.manager;

import net.chesstango.engine.SearchListener;
import net.chesstango.search.SearchMove;

import java.util.Objects;

/**
 * @author Mauricio Coria
 */
public final class SearchManagerChainBuilder {
    private SearchMove searchMove;
    private SearchListener listenerClient;
    private boolean searchByBookEnabled;

    public SearchManagerChainBuilder withSearchByBookEnabled(boolean searchByBookEnabled) {
        this.searchByBookEnabled = searchByBookEnabled;
        return this;
    }

    public SearchManagerChainBuilder withSearchMove(SearchMove searchMove) {
        this.searchMove = searchMove;
        return this;
    }

    public SearchManagerChainBuilder withSearchListener(SearchListener listenerClient) {
        this.listenerClient = listenerClient;
        return this;
    }

    public SearchManagerChain build() {
        SearchManagerByAlgorithm searchManagerByAlgorithm = new SearchManagerByAlgorithm(searchMove);
        SearchManagerByBook searchManagerByBook = null;
        if (searchByBookEnabled) {
            searchManagerByBook = new SearchManagerByBook();
            searchManagerByBook.setNext(searchManagerByAlgorithm);
        }

        return Objects.nonNull(searchManagerByBook) ? searchManagerByBook : searchManagerByAlgorithm;
    }
}
