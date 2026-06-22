package net.chesstango.engine;

import lombok.extern.slf4j.Slf4j;
import net.chesstango.piazzolla.polyglot.PolyglotBook;
import net.chesstango.piazzolla.syzygy.Syzygy;
import net.chesstango.search.SearchBuilder;
import net.chesstango.search.smart.alphabeta.egtb.EndGameTableBase;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Mauricio Corial
 */
@Slf4j
class TangoFactoryImp implements TangoFactory {
    private final SearchManagerThreadFactory searchThreadFactory;
    private final SearchManagerThreadFactory timeoutThreadFactory;

    TangoFactoryImp() {
        this.searchThreadFactory = new SearchManagerThreadFactory("search");
        this.timeoutThreadFactory = new SearchManagerThreadFactory("timeout");
    }

    @Override
    public SearchInvoker createSearchInvokerAsync(SearchByChain searchByChain, ExecutorService executorService) {
        return new SearchInvokerAsync(searchByChain, executorService);
    }

    @Override
    public SearchInvoker createSearchInvokerSync(SearchByChain searchByChain) {
        return new SearchInvokerSync(searchByChain);
    }


    @Override
    public SearchManager createSearchManager(int infiniteDepth,
                                             SearchByTree searchByTree,
                                             TangoOptions tangoOptions,
                                             TimeMgmt timeMgmt,
                                             SearchInvoker searchInvoker,
                                             ScheduledExecutorService timeOutExecutor) {
        return new SearchManager(infiniteDepth, searchByTree, tangoOptions, timeMgmt, searchInvoker, timeOutExecutor);
    }

    @Override
    public SearchBuilder<?> createSearchBuilder() {
        return SearchBuilder
                .newSearchBuilder();
    }

    @Override
    public EndGameTableBase createSyzygyTableBaseAdapter(Syzygy syzygy) {
        return new SyzygyAdapter(syzygy);
    }

    @Override
    public SearchByAggregator createSearchByAggregator(Config config, SearchByTree searchByTree) {
        return new SearchByAggregator(this, config, searchByTree);
    }

    @Override
    public SearchByTree createSearchByTree(Config config) {
        return new SearchByTree(this, config);
    }

    @Override
    public SearchByOpenBook createSearchByOpenBook(PolyglotBook book) {
        return new SearchByOpenBook(book);
    }

    @Override
    public SearchByTablebase createSearchByTablebase(Syzygy syzygy) {
        return new SearchByTablebase(syzygy);
    }

    @Override
    public SearchByProxy createSearchByProxy() {
        return new SearchByProxy();
    }

    @Override
    public TimeMgmt createTimeMgmt() {
        return new TimeFivePercentage();
    }

    @Override
    public PolyglotBook createPolyglotBook(Path polyglotFile) {
        if (polyglotFile != null) {
            try {
                if (Files.exists(polyglotFile)) {
                    return PolyglotBook.open(polyglotFile);
                } else {
                    log.warn("Book file '{}' not found", polyglotFile);
                }
            } catch (IllegalArgumentException | IOException e) {
                log.error("Error opening book file", e);
            }
        }
        return null;
    }

    @Override
    public Syzygy createSyzygy(Path syzygyPath) {
        if (syzygyPath != null) {
            return Syzygy.open(syzygyPath.toString());
        }
        return null;
    }

    @Override
    public ScheduledExecutorService createScheduledExecutorService() {
        return Executors.newSingleThreadScheduledExecutor(timeoutThreadFactory);
    }

    @Override
    public ExecutorService createExecutorService() {
        return Executors.newSingleThreadExecutor(searchThreadFactory);
    }

    private static class SearchManagerThreadFactory implements ThreadFactory {
        private final AtomicInteger threadCounter = new AtomicInteger(1);
        private String threadNamePrefix = "";

        public SearchManagerThreadFactory(String threadNamePrefix) {
            this.threadNamePrefix = threadNamePrefix;
        }

        public Thread newThread(Runnable runnable) {
            return new Thread(runnable, String.format("%s-%d", threadNamePrefix, threadCounter.getAndIncrement()));
        }
    }
}
