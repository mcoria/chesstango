package net.chesstango.engine;

import lombok.extern.slf4j.Slf4j;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.piazzolla.syzygy.Syzygy;
import net.chesstango.search.Search;
import net.chesstango.search.smart.features.egtb.EndGameTableBase;
import net.chesstango.search.smart.features.egtb.visitors.SetEndGameTableBaseVisitor;

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

    private final SearchManagerFactory searchManagerFactory;

    public SearchManagerBuilder() {
        this(new DefaultSearchManagerFactory());
    }

    SearchManagerBuilder(SearchManagerFactory searchManagerFactory) {
        this.searchManagerFactory = searchManagerFactory;
    }


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
        log.info("Building SearchManager");

        if (asyncInvoker && searchExecutor == null) {
            throw new IllegalArgumentException("SearchExecutor must be provided when asyncInvoker is true");
        }
        if (timeOutExecutor == null) {
            throw new IllegalArgumentException("ScheduledExecutorService must be provided");
        }
        if (infiniteDepth <= 0) {
            throw new IllegalArgumentException("Infinite depth must be greater than 0");
        }
        if (search != null && evaluator != null) {
            log.warn("Both search and evaluator are set. Evaluator will be ignored");
        }

        SearchChain searchChain = buildSearchChain();

        SearchInvoker searchInvoker = asyncInvoker
                ? searchManagerFactory.createSearchInvokerAsync(searchChain, searchExecutor)
                : searchManagerFactory.createSearchInvokerSync(searchChain);

        return searchManagerFactory.createSearchManager(infiniteDepth, searchChain, new TimeFivePercentage(), searchInvoker, timeOutExecutor);
    }

    SearchChain buildSearchChain() {
        List<SearchChain> searchChains = new ArrayList<>();
        Syzygy syzygy = null;

        if (polyglotFile != null) {
            SearchByOpenBook searchManagerByOpenBook = searchManagerFactory.createSearchByOpenBook(polyglotFile);
            if (searchManagerByOpenBook != null) {
                searchChains.add(searchManagerByOpenBook);
            }
        }

        if (syzygyDirectory != null) {
            SearchByTablebase searchByTablebase = searchManagerFactory.createSearchByTablebase(syzygyDirectory);
            if (searchByTablebase != null) {
                searchChains.add(searchByTablebase);
                syzygy = searchByTablebase.getSyzygy();
            }
        }

        if (search == null) {
            if (evaluator != null) {
                search = searchManagerFactory.createSearch(evaluator);
            } else {
                search = searchManagerFactory.createSearch();
            }
        }

        if (syzygy != null) {
            search.accept(new SetEndGameTableBaseVisitor(searchManagerFactory.createSyzygyTableBaseAdapter(syzygy)));
        }

        SearchByTree searchByTree = searchManagerFactory.createSearchByAlgorithm(search);

        searchChains.add(searchByTree);

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

    interface SearchManagerFactory {
        SearchByOpenBook createSearchByOpenBook(String polyglotFile);

        SearchByTablebase createSearchByTablebase(String syzygyDirectory);

        SearchByTree createSearchByAlgorithm(Search search);

        SearchInvoker createSearchInvokerAsync(SearchChain searchChain, ExecutorService searchExecutor);

        SearchInvoker createSearchInvokerSync(SearchChain searchChain);

        SearchManager createSearchManager(int infiniteDepth,
                                          SearchChain searchChain,
                                          TimeMgmt timeMgmt,
                                          SearchInvoker searchInvoker,
                                          ScheduledExecutorService timeOutExecutor);

        Search createSearch();

        Search createSearch(Evaluator evaluator);

        EndGameTableBase createSyzygyTableBaseAdapter(Syzygy syzygy);
    }


    static class DefaultSearchManagerFactory implements SearchManagerFactory {
        @Override
        public SearchByOpenBook createSearchByOpenBook(String polyglotFile) {
            return SearchByOpenBook.open(polyglotFile);
        }

        @Override
        public SearchByTablebase createSearchByTablebase(String syzygyDirectory) {
            return SearchByTablebase.open(syzygyDirectory);
        }

        @Override
        public SearchByTree createSearchByAlgorithm(Search search) {
            return new SearchByTree(search);
        }

        @Override
        public SearchInvoker createSearchInvokerAsync(SearchChain searchChain, ExecutorService searchExecutor) {
            return new SearchInvokerAsync(searchChain, searchExecutor);
        }

        @Override
        public SearchInvoker createSearchInvokerSync(SearchChain searchChain) {
            return new SearchInvokerSync(searchChain);
        }

        @Override
        public SearchManager createSearchManager(int infiniteDepth,
                                                 SearchChain searchChain,
                                                 TimeMgmt timeMgmt,
                                                 SearchInvoker searchInvoker,
                                                 ScheduledExecutorService timeOutExecutor) {
            return new SearchManager(infiniteDepth, searchChain, timeMgmt, searchInvoker, timeOutExecutor);
        }

        @Override
        public Search createSearch() {
            return Search
                    .newSearchBuilder()
                    .withGameEvaluator(Evaluator.getInstance())
                    .build();
        }

        @Override
        public Search createSearch(Evaluator evaluator) {
            return Search
                    .newSearchBuilder()
                    .withGameEvaluator(evaluator)
                    .build();
        }

        @Override
        public EndGameTableBase createSyzygyTableBaseAdapter(Syzygy syzygy) {
            return new SyzygyAdapter(syzygy);
        }
    }
}
