package net.chesstango.engine;

import lombok.extern.slf4j.Slf4j;
import net.chesstango.engine.timemgmt.FivePercentage;
import net.chesstango.search.Search;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author Mauricio Coria
 */
@Slf4j
class SearchManagerBuilder {
    private Search search;

    private int infiniteDepth;

    private String polyglotFile;

    private String syzygyDirectory;

    private ExecutorService searchExecutor;

    private ScheduledExecutorService timeOutExecutor;

    private boolean asyncInvoker;

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

    public SearchManagerBuilder withAsyncInvoker() {
        this.asyncInvoker = true;
        return this;
    }

    public SearchManager build() {
        List<SearchChain> searchChains = new ArrayList<>();

        SearchByTablebase searchByTablebase = null;
        if (polyglotFile != null) {
            SearchByOpenBook searchManagerByOpenBook = SearchByOpenBook.open(polyglotFile);
            if (searchManagerByOpenBook != null) {
                searchChains.add(searchByTablebase);
            }
        }

        if (syzygyDirectory != null) {
            searchByTablebase = SearchByTablebase.open(syzygyDirectory);
            if (searchByTablebase != null) {
                searchChains.add(searchByTablebase);
            }
        }

        if (search != null) {
            searchChains.add(new SearchByAlgorithm(search));
        } else {
            searchChains.add(new SearchByAlgorithm(Search.getInstance()));
        }

        SearchChain searchChainHead = linkChain(searchChains);

        SearchInvoker searchInvoker = null;

        if (asyncInvoker) {
            if (searchExecutor == null) {
                throw new IllegalArgumentException("SearchExecutor must be provided when asyncInvoker is true");
            }
            searchInvoker = new SearchInvokerAsync(searchChainHead, searchExecutor);
        } else {
            searchInvoker = new SearchInvokerSync(searchChainHead);
        }

        if (timeOutExecutor == null) {
            throw new IllegalArgumentException("ScheduledExecutorService must be provided");
        }

        return new SearchManager(infiniteDepth, searchChainHead, new FivePercentage(), searchInvoker, timeOutExecutor);
    }

    SearchChain linkChain(List<SearchChain> searchChains) {
        SearchChain previousChain = searchChains.getFirst();
        for (int i = 1; i < searchChains.size(); i++) {
            if (previousChain instanceof SearchByOpenBook searchByOpenBook) {
                searchByOpenBook.setNext(searchChains.get(i));
                previousChain = searchChains.get(i);
            } else if (previousChain instanceof SearchByTablebase searchByTablebase) {
                searchByTablebase.setNext(searchChains.get(i));
                previousChain = searchChains.get(i);
            }
        }
        return searchChains.getFirst();
    }


}
