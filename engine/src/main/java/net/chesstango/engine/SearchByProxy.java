package net.chesstango.engine;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Mauricio Coria
 */
@Setter(AccessLevel.PACKAGE)
@Getter(AccessLevel.PACKAGE)
class SearchByProxy implements SearchByChain {
    private SearchByChain next;

    private SearchByChain imp;

    @Override
    public SearchResponse search(SearchContext context) {
        SearchResponse searchResponse = null;
        if (imp != null) {
            searchResponse = imp.search(context);
        }
        return searchResponse != null ? searchResponse : next.search(context);
    }
}
