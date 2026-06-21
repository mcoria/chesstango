package net.chesstango.engine;

import net.chesstango.piazzolla.polyglot.PolyglotBook;
import net.chesstango.piazzolla.syzygy.Syzygy;
import net.chesstango.search.SearchBuilder;
import net.chesstango.search.smart.alphabeta.egtb.EndGameTableBase;

import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author Mauricio Corial
 */
interface TangoFactory {
    SearchInvoker createSearchInvokerAsync(SearchByChain searchByChain, ExecutorService executorService);

    SearchInvoker createSearchInvokerSync(SearchByChain searchByChain);

    SearchManager createSearchManager(int infiniteDepth,
                                      SearchByTree searchByTree,
                                      TangoOptions tangoOptions,
                                      TimeMgmt timeMgmt,
                                      SearchInvoker searchInvoker,
                                      ScheduledExecutorService timeOutExecutor);

    SearchBuilder<?> createSearchBuilder();

    EndGameTableBase createSyzygyTableBaseAdapter(Syzygy syzygy);

    SearchByAggregator createSearchByAggregator(Config config, SearchByTree searchByTree);

    SearchByTree createSearchByTree(Config config);

    SearchByOpenBook createSearchByOpenBook(PolyglotBook book);

    SearchByTablebase createSearchByTablebase(Syzygy syzygy);

    SearchByProxy createSearchByProxy();

    TimeMgmt createTimeMgmt();

    PolyglotBook createPolyglotBook(Path polyglotFile);

    Syzygy createSyzygy(String syzygyPath);

    ScheduledExecutorService createScheduledExecutorService();

    ExecutorService createExecutorService();
}
