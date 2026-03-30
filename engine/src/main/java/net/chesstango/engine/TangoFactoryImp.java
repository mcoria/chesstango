package net.chesstango.engine;

import lombok.extern.slf4j.Slf4j;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.piazzolla.polyglot.PolyglotBook;
import net.chesstango.piazzolla.syzygy.Syzygy;
import net.chesstango.search.Search;
import net.chesstango.search.smart.alphabeta.egtb.EndGameTableBase;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Mauricio Corial
 */
@Slf4j
class TangoFactoryImp implements TangoFactory {

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
                                             SearchByAggregator searchByAggregator,
                                             TimeMgmt timeMgmt,
                                             SearchInvoker searchInvoker,
                                             ScheduledExecutorService timeOutExecutor) {
        return new SearchManager(infiniteDepth, searchByTree, searchByAggregator, timeMgmt, searchInvoker, timeOutExecutor);
    }


    @Override
    public Search createSearch() {
        return Search
                .newSearchBuilder()
                .withGameEvaluator(Evaluator.createInstance())
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


    @Override
    public ThreadFactory createThreadFactory(String threadNamePrefix) {
        return new SearchManagerThreadFactory(threadNamePrefix);
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
    public PolyglotBook createPolyglotBook(String polyglotFile) {
        if (polyglotFile != null) {
            try {
                Path polyglotFilePath = Path.of(polyglotFile);
                if (Files.exists(polyglotFilePath)) {
                    return PolyglotBook.open(polyglotFilePath);
                } else {
                    log.warn("Book file '{}' not found", polyglotFile);
                }
            } catch (IOException e) {
                log.error("Error opening book file", e);
            }
        }
        return null;
    }

    @Override
    public Syzygy createSyzygy(String syzygyPath) {
        if (syzygyPath != null) {
            return Syzygy.open(syzygyPath);
        }
        return null;
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
