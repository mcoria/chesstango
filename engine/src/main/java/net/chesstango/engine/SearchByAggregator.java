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
class SearchByAggregator implements SearchByChain, TangoOptions {
    private final TangoFactory tangoFactory;
    private final SearchByProxy searchByOpenBookProxy;
    private final SearchByProxy searchByTablebaseProxy;
    private final SearchByTree searchByTree;

    SearchByAggregator(TangoFactory tangoFactory, Config config, SearchByTree searchByTree) {
        this.tangoFactory = tangoFactory;
        this.searchByOpenBookProxy = tangoFactory.createSearchByProxy();
        this.searchByTablebaseProxy = tangoFactory.createSearchByProxy();
        this.searchByTree = searchByTree;

        this.searchByOpenBookProxy.setNext(searchByTablebaseProxy);
        this.searchByTablebaseProxy.setNext(searchByTree);

        if (config.getPolyglotFile() != null) {
            setPolyglotFile(config.getPolyglotFile());
        }
        if (config.getSyzygyPath() != null) {
            setSyzygyPath(config.getSyzygyPath());
        }
    }


    @Override
    public SearchResponse search(SearchContext context) {
        return searchByOpenBookProxy.search(context);
    }

    @Override
    public void setPolyglotFile(String polyglotFile) {
        PolyglotBook polyglotBook = tangoFactory.createPolyglotBook(polyglotFile);
        if (polyglotBook != null) {
            SearchByOpenBook searchByOpenBook = tangoFactory.createSearchByOpenBook(polyglotBook);
            searchByOpenBookProxy.setImp(searchByOpenBook);
        }
    }


    @Override
    public void setSyzygyPath(String syzygyPath) {
        Syzygy syzygy = tangoFactory.createSyzygy(syzygyPath);

        if (syzygy != null) {
            SearchByTablebase searchByTablebase = tangoFactory.createSearchByTablebase(syzygy);
            searchByTablebaseProxy.setImp(searchByTablebase);

            searchByTree.setSyzygy(syzygy);
        }

    }
}
