package net.chesstango.engine;

import net.chesstango.evaluation.Evaluator;
import net.chesstango.piazzolla.polyglot.PolyglotBook;
import net.chesstango.piazzolla.syzygy.Syzygy;
import net.chesstango.search.Search;
import net.chesstango.search.smart.alphabeta.egtb.EndGameTableBase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
/**
 * @author Mauricio Corial
 */
interface TangoFactory {
    SearchInvoker createSearchInvokerAsync(SearchByChain searchByChain, ExecutorService executorService);

    SearchInvoker createSearchInvokerSync(SearchByChain searchByChain);

    SearchManager createSearchManager(int infiniteDepth,
                                      Runnable stopFn,
                                      TimeMgmt timeMgmt,
                                      SearchInvoker searchInvoker,
                                      ScheduledExecutorService timeOutExecutor);

    Search createSearch();

    Search createSearch(Evaluator evaluator);

    EndGameTableBase createSyzygyTableBaseAdapter(Syzygy syzygy);

    ThreadFactory createThreadFactory(String threadNamePrefix);

    SearchByAggregator createSearchByAggregator(Config config);

    SearchByTree createSearchByTree(Config config);

    SearchByOpenBook createSearchByOpenBook(PolyglotBook book);

    SearchByTablebase createSearchByTablebase(Syzygy syzygy);

    SearchByProxy createSearchByProxy();

    TimeMgmt createTimeMgmt();

    PolyglotBook createPolyglotBook(String polyglotFile);

    Syzygy createSyzygy(String syzygyPath);
}
