package net.chesstango.engine;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ScheduledExecutorService;

/**
 * @author Mauricio Coria
 */
@Slf4j
class SearchManagerBuilder {
    private final TangoFactory tangoFactory;

    private Config config;

    private int infiniteDepth;


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

    public SearchManager build() {
        log.info("Building SearchManager");

        if (infiniteDepth <= 0) {
            throw new IllegalArgumentException("Infinite depth must be greater than 0");
        }

        if (config.getSearch() != null && config.getEvaluator() != null) {
            log.warn("Both search and evaluator are set. Evaluator will be ignored");
        }

        SearchByTree searchByTree = tangoFactory.createSearchByTree(config);

        SearchByAggregator searchByAggregator = tangoFactory.createSearchByAggregator(config, searchByTree);

        SearchInvoker searchInvoker = config.getSyncSearch()
                ? tangoFactory.createSearchInvokerSync(searchByAggregator)
                : tangoFactory.createSearchInvokerAsync(searchByAggregator, tangoFactory.createExecutorService());

        TimeMgmt timeMgmt = tangoFactory.createTimeMgmt();

        ScheduledExecutorService timeOutExecutor = tangoFactory.createScheduledExecutorService();

        return tangoFactory.createSearchManager(infiniteDepth, searchByTree, searchByAggregator, timeMgmt, searchInvoker, timeOutExecutor);
    }


}
