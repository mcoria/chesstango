package net.chesstango.engine;

import net.chesstango.engine.timemgmt.FivePercentage;
import net.chesstango.search.Search;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author Mauricio Coria
 */
class SearchManagerBuilder {
    private Search search;

    private int infiniteDepth;

    private String polyglotFile;

    private String syzygyDirectory;

    private ExecutorService searchExecutor;

    private ScheduledExecutorService timeOutExecutor;

    public SearchManagerBuilder withSearch(Search search) {
        this.search = search;
        return this;
    }

    public SearchManagerBuilder withPolyglotFile(String polyglotFile) {
        this.polyglotFile = polyglotFile;
        return this;
    }

    public SearchManagerBuilder withSyzygyDirectory(String syzygyDirectory) {
        this.syzygyDirectory = syzygyDirectory;
        return this;
    }

    public SearchManagerBuilder withInfiniteDepth(int infiniteDepth) {
        this.infiniteDepth = infiniteDepth;
        return this;
    }

    public SearchManagerBuilder withExecutorService(ExecutorService searchExecutor) {
        this.searchExecutor = searchExecutor;
        return this;
    }

    public SearchManagerBuilder withScheduledExecutorService(ScheduledExecutorService timeOutExecutor) {
        this.timeOutExecutor = timeOutExecutor;
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

        return new SearchManager(infiniteDepth, head, new FivePercentage(), searchExecutor, timeOutExecutor);
    }
}
