package net.chesstango.engine;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author Mauricio Coria
 */
@Slf4j
class SearchManagerBuilder {
    private final TangoFactory tangoFactory;

    private Config config;

    private int infiniteDepth;

    private ExecutorService searchExecutor;

    private ScheduledExecutorService timeOutExecutor;

    SearchManagerBuilder(TangoFactory tangoFactory) {
        this.tangoFactory = tangoFactory;
    }

    public SearchManagerBuilder withConfig(Config config) {
        this.config = config;
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
        log.info("Building SearchManager");

        if (!config.getSyncSearch() && searchExecutor == null) {
            throw new IllegalArgumentException("SearchExecutor must be provided when asyncInvoker is true");
        }
        if (timeOutExecutor == null) {
            throw new IllegalArgumentException("ScheduledExecutorService must be provided");
        }
        if (infiniteDepth <= 0) {
            throw new IllegalArgumentException("Infinite depth must be greater than 0");
        }
        if (config.getSearch() != null && config.getEvaluator() != null) {
            log.warn("Both search and evaluator are set. Evaluator will be ignored");
        }

        SearchByAggregator searchByAggregator = tangoFactory.createSearchByAggregator(config);

        SearchInvoker searchInvoker = config.getSyncSearch()
                ? tangoFactory.createSearchInvokerSync(searchByAggregator)
                : tangoFactory.createSearchInvokerAsync(searchByAggregator, searchExecutor);

        TimeMgmt timeMgmt = tangoFactory.createTimeMgmt();

        return tangoFactory.createSearchManager(infiniteDepth, searchByAggregator::stopSearching, timeMgmt, searchInvoker, timeOutExecutor);
    }


}
