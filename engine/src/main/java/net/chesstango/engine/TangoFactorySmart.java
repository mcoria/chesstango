package net.chesstango.engine;

import lombok.extern.slf4j.Slf4j;
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
@Slf4j
class TangoFactorySmart implements TangoFactory, AutoCloseable {

    private final TangoFactory imp;

    private PolyglotBook polyglotBook;

    private Syzygy syzygy;

    TangoFactorySmart(TangoFactory tangoFactory) {
        this.imp = tangoFactory;
    }


    @Override
    public void close() {
        closeInstance(polyglotBook);
        closeInstance(syzygy);
    }

    private void closeInstance(AutoCloseable theInstance) {
        try {
            if (theInstance != null) {
                theInstance.close();
            }
        } catch (Exception e) {
            log.error("Error closing instance", e);
        }
    }

    @Override
    public SearchInvoker createSearchInvokerAsync(SearchByChain searchByChain, ExecutorService executorService) {
        return imp.createSearchInvokerAsync(searchByChain, executorService);
    }

    @Override
    public SearchInvoker createSearchInvokerSync(SearchByChain searchByChain) {
        return imp.createSearchInvokerSync(searchByChain);
    }

    @Override
    public SearchManager createSearchManager(int infiniteDepth,
                                             SearchByTree searchByTree,
                                             TimeMgmt timeMgmt,
                                             SearchInvoker searchInvoker,
                                             ScheduledExecutorService timeOutExecutor) {
        return imp.createSearchManager(infiniteDepth, searchByTree, timeMgmt, searchInvoker, timeOutExecutor);
    }

    @Override
    public Search createSearch() {
        return imp.createSearch();
    }

    @Override
    public Search createSearch(Evaluator evaluator) {
        return imp.createSearch(evaluator);
    }

    @Override
    public EndGameTableBase createSyzygyTableBaseAdapter(Syzygy syzygy) {
        return imp.createSyzygyTableBaseAdapter(syzygy);
    }

    @Override
    public ThreadFactory createThreadFactory(String threadNamePrefix) {
        return imp.createThreadFactory(threadNamePrefix);
    }

    @Override
    public SearchByAggregator createSearchByAggregator(Config config, SearchByTree searchByTree) {
        return imp.createSearchByAggregator(config, searchByTree);
    }

    @Override
    public SearchByTree createSearchByTree(Config config) {
        return imp.createSearchByTree(config);
    }

    @Override
    public SearchByOpenBook createSearchByOpenBook(PolyglotBook book) {
        return imp.createSearchByOpenBook(book);
    }

    @Override
    public SearchByTablebase createSearchByTablebase(Syzygy syzygy) {
        return imp.createSearchByTablebase(syzygy);
    }

    @Override
    public SearchByProxy createSearchByProxy() {
        return imp.createSearchByProxy();
    }

    @Override
    public TimeMgmt createTimeMgmt() {
        return imp.createTimeMgmt();
    }

    @Override
    public PolyglotBook createPolyglotBook(String polyglotFile) {
        polyglotBook = imp.createPolyglotBook(polyglotFile);
        return imp.createPolyglotBook(polyglotFile);
    }

    @Override
    public Syzygy createSyzygy(String syzygyPath) {
        syzygy = imp.createSyzygy(syzygyPath);
        return syzygy;
    }
}
