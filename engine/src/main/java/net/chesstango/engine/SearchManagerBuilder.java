package net.chesstango.engine;

import net.chesstango.search.Search;

/**
 * @author Mauricio Coria
 */
class SearchManagerBuilder {
    private Search search;

    private boolean searchByBookEnabled;
    private SearchListener listenerClient;
    private int infiniteDepth;

    public SearchManagerBuilder withSearchByBookEnabled(boolean searchByBookEnabled) {
        this.searchByBookEnabled = searchByBookEnabled;
        return this;
    }

    public SearchManagerBuilder withSearchMove(Search search) {
        this.search = search;
        return this;
    }

    public SearchManagerBuilder withInfiniteDepth(int infiniteDepth) {
        this.infiniteDepth = infiniteDepth;
        return this;
    }

    public SearchManager build() {
        SearchByAlgorithm searchManagerByAlgorithm = new SearchByAlgorithm(search);
        //searchManagerByAlgorithm.setSearchResultByDepthListener(listenerClient::searchInfo);

        SearchByOpenBook searchManagerByOpenBook = null;
        if (searchByBookEnabled) {
            searchManagerByOpenBook = new SearchByOpenBook();
            searchManagerByOpenBook.setNext(searchManagerByAlgorithm);
        }

        SearchManager searchManager = new SearchManager(searchManagerByAlgorithm);
        searchManager.setInfiniteDepth(infiniteDepth);

        return searchManager;
    }
}
