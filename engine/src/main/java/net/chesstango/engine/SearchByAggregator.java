package net.chesstango.engine;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.chesstango.piazzolla.polyglot.PolyglotBook;
import net.chesstango.piazzolla.syzygy.Syzygy;

/**
 * @author Mauricio Coria
 */
@Slf4j
@Getter(AccessLevel.PACKAGE)
class SearchByAggregator implements SearchByChain {
    private final TangoFactory tangoFactory;
    private final SearchByProxy searchByOpenBookProxy;
    private final SearchByProxy searchByTablebaseProxy;
    private final SearchByTree searchByTree;

    SearchByAggregator(TangoFactory tangoFactory, Config config) {
        this.tangoFactory = tangoFactory;
        this.searchByOpenBookProxy = tangoFactory.createSearchByProxy();
        this.searchByTablebaseProxy = tangoFactory.createSearchByProxy();
        this.searchByTree = tangoFactory.createSearchByTree(config);

        this.searchByOpenBookProxy.setNext(searchByTablebaseProxy);
        this.searchByTablebaseProxy.setNext(searchByTree);

        if (config.getPolyglotFile() != null) {
            withPolyglotFile(config.getPolyglotFile());
        }
        if (config.getSyzygyPath() != null) {
            withSyzygyPath(config.getSyzygyPath());
        }
    }


    @Override
    public SearchResponse search(SearchContext context) {
        return searchByOpenBookProxy.search(context);
    }

    void stopSearching() {
        searchByTree.stopSearching();
    }

    SearchByAggregator withPolyglotFile(String polyglotFile) {
        PolyglotBook polyglotBook = tangoFactory.createPolyglotBook(polyglotFile);
        if (polyglotBook != null) {
            SearchByOpenBook searchByOpenBook = tangoFactory.createSearchByOpenBook(polyglotBook);
            searchByOpenBookProxy.setImp(searchByOpenBook);
        }
        return this;
    }


    SearchByAggregator withSyzygyPath(String syzygyPath) {
        Syzygy syzygy = tangoFactory.createSyzygy(syzygyPath);

        if (syzygy != null) {
            SearchByTablebase searchByTablebase = tangoFactory.createSearchByTablebase(syzygy);
            searchByTablebaseProxy.setImp(searchByTablebase);

            searchByTree.setSyzygy(syzygy);
        }

        return this;
    }

}
