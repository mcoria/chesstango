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
class TangoFactory {

    SearchInvoker createSearchInvokerAsync(SearchByChain searchByChain, ExecutorService executorService) {
        return new SearchInvokerAsync(searchByChain, executorService);
    }


    SearchInvoker createSearchInvokerSync(SearchByChain searchByChain) {
        return new SearchInvokerSync(searchByChain);
    }


    SearchManager createSearchManager(int infiniteDepth,
                                             Runnable stopFn,
                                             TimeMgmt timeMgmt,
                                             SearchInvoker searchInvoker,
                                             ScheduledExecutorService timeOutExecutor) {
        return new SearchManager(infiniteDepth, stopFn, timeMgmt, searchInvoker, timeOutExecutor);
    }


    Search createSearch() {
        return Search
                .newSearchBuilder()
                .withGameEvaluator(Evaluator.createInstance())
                .build();
    }


    Search createSearch(Evaluator evaluator) {
        return Search
                .newSearchBuilder()
                .withGameEvaluator(evaluator)
                .build();
    }


    EndGameTableBase createSyzygyTableBaseAdapter(Syzygy syzygy) {
        return new SyzygyAdapter(syzygy);
    }


    ThreadFactory createThreadFactory(String threadNamePrefix) {
        return new SearchManagerThreadFactory(threadNamePrefix);
    }

    SearchByAggregator createSearchByAggregator(Config config) {
        return new SearchByAggregator(this, config);
    }

    SearchByTree createSearchByTree(Config config) {
        return new SearchByTree(this, config);
    }

    SearchByOpenBook createSearchByOpenBook(PolyglotBook book) {
        return new SearchByOpenBook(book);
    }

    SearchByTablebase createSearchByTablebase(Syzygy syzygy) {
        return new SearchByTablebase(syzygy);
    }

    SearchByProxy createSearchByProxy() {
        return new SearchByProxy();
    }

    TimeMgmt createTimeMgmt() {
        return new TimeFivePercentage();
    }

    PolyglotBook createPolyglotBook(String polyglotFile) {
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

    Syzygy createSyzygy(String syzygyPath) {
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
