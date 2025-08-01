package net.chesstango.engine.manager;

import net.chesstango.engine.SearchListener;
import net.chesstango.search.Search;

import java.util.Objects;

/**
 * @author Mauricio Coria
 */
public final class SearchManagerChainBuilder {
    private Search search;
    private boolean searchByBookEnabled;

    public SearchManagerChainBuilder withSearchByBookEnabled(boolean searchByBookEnabled) {
        this.searchByBookEnabled = searchByBookEnabled;
        return this;
    }

    public SearchManagerChainBuilder withSearchMove(Search search) {
        this.search = search;
        return this;
    }

    public SearchManagerChainBuilder withSearchListener(SearchListener listenerClient) {
        return this;
    }

    public SearchManagerChain build() {
        SearchManagerByAlgorithm searchManagerByAlgorithm = new SearchManagerByAlgorithm(search);
        SearchManagerByOpenBook searchManagerByOpenBook = null;
        if (searchByBookEnabled) {
            searchManagerByOpenBook = new SearchManagerByOpenBook();
            searchManagerByOpenBook.setNext(searchManagerByAlgorithm);
        }

        return Objects.nonNull(searchManagerByOpenBook) ? searchManagerByOpenBook : searchManagerByAlgorithm;
    }
}
