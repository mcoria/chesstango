package net.chesstango.engine;

import net.chesstango.search.DefaultSearch;
import net.chesstango.search.Search;

/**
 * @author Mauricio Coria
 */
class SearchManagerBuilder {
    private Search search;

    private int infiniteDepth;

    private String polyglotFile;

    private String syzygyDirectory;

    public SearchManagerBuilder withSearch(Search search) {
        this.search = search;
        return this;
    }

    public SearchManagerBuilder withPolyglotFile(String polyglotFile) {
        this.polyglotFile = polyglotFile;
        return this;
    }

    public void withSyzygyDirectory(String SyzygyDirectory) {
        this.syzygyDirectory = SyzygyDirectory;
    }

    public SearchManagerBuilder withInfiniteDepth(int infiniteDepth) {
        this.infiniteDepth = infiniteDepth;
        return this;
    }

    public SearchManager build() {
        SearchChain head = new SearchByAlgorithm(search);

        if (polyglotFile != null) {
            SearchByOpenBook searchManagerByOpenBook = SearchByOpenBook.open(polyglotFile);
            if (searchManagerByOpenBook != null) {
                searchManagerByOpenBook.setNext(head);
                head = searchManagerByOpenBook;
            }
        }

        if (syzygyDirectory != null) {
            SearchByTablebase searchManagerByOpenBook = SearchByTablebase.open(syzygyDirectory);
            if (searchManagerByOpenBook != null) {
                searchManagerByOpenBook.setNext(head);
                head = searchManagerByOpenBook;
            }
        }

        SearchManager searchManager = new SearchManager(head);
        searchManager.setInfiniteDepth(infiniteDepth);

        return searchManager;
    }
}
