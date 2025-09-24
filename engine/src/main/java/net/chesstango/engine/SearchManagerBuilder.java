package net.chesstango.engine;

import lombok.extern.slf4j.Slf4j;
import net.chesstango.engine.timemgmt.FivePercentage;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.search.Search;
import net.chesstango.search.SearchBuilder;

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

    private Evaluator evaluator;

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

    public SearchManagerBuilder withEvaluator(Evaluator evaluator) {
        this.evaluator = evaluator;
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
        if (asyncInvoker && searchExecutor == null) {
            throw new IllegalArgumentException("SearchExecutor must be provided when asyncInvoker is true");
        }
        if (timeOutExecutor == null) {
            throw new IllegalArgumentException("ScheduledExecutorService must be provided");
        }

        SearchChain searchChain = buildSearchChain();

        SearchInvoker searchInvoker = asyncInvoker
                ? new SearchInvokerAsync(searchChain, searchExecutor)
                : new SearchInvokerSync(searchChain);

        return new SearchManager(infiniteDepth, searchChain, new FivePercentage(), searchInvoker, timeOutExecutor);
    }

    SearchChain buildSearchChain() {
        List<SearchChain> searchChains = new ArrayList<>();

        if (polyglotFile != null) {
            SearchByOpenBook searchManagerByOpenBook = SearchByOpenBook.open(polyglotFile);
            if (searchManagerByOpenBook != null) {
                searchChains.add(searchManagerByOpenBook);
            }
        }

        if (syzygyDirectory != null) {
            SearchByTablebase searchByTablebase = SearchByTablebase.open(syzygyDirectory);
            if (searchByTablebase != null) {
                searchChains.add(searchByTablebase);
            }
        }

        if (search != null) {
            searchChains.add(new SearchByAlgorithm(search));
        } else {
            SearchBuilder<?> searchBuilder = Search.newSearchBuilder()
                    .withGameEvaluator(evaluator == null ? Evaluator.getInstance() : evaluator);

            search = searchBuilder.build();

            searchChains.add(new SearchByAlgorithm(search));
        }

        return linkChain(searchChains);
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
