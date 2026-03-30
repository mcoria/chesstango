package net.chesstango.engine;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.chesstango.board.representations.move.SimpleMoveEncoder;
import net.chesstango.piazzolla.syzygy.Syzygy;
import net.chesstango.search.Search;
import net.chesstango.search.SearchResult;
import net.chesstango.search.smart.alphabeta.egtb.EndGameTableBase;
import net.chesstango.search.smart.alphabeta.egtb.visitors.SetEndGameTableBaseVisitor;
import net.chesstango.search.visitors.SetMaxDepthVisitor;
import net.chesstango.search.visitors.SetSearchByDepthListenerVisitor;
import net.chesstango.search.visitors.SetSearchPredicateVisitor;

import java.time.Duration;
import java.time.Instant;

/**
 * @author Mauricio Coria
 */
@Slf4j
@Getter(AccessLevel.PACKAGE)
class SearchByTree implements SearchByChain {
    private final Search search;

    SearchByTree(TangoFactory tangoFactory, Config config) {
        if (config.getSearch() == null) {
            if (config.getEvaluator() != null) {
                search = tangoFactory.createSearch(config.getEvaluator());
            } else {
                search = tangoFactory.createSearch();
            }
        } else {
            search = config.getSearch();
        }
    }

    /**
     * TODO: deberia subsribirse al listener para manipular el latcher que habilita el stop
     *
     * @param context
     * @return
     */
    @Override
    public SearchResponse search(SearchContext context) {
        search.accept(new SetMaxDepthVisitor(context.getDepth()));

        search.accept(new SetSearchPredicateVisitor(context.getSearchResultByDepthPredicate()));

        search.accept(new SetSearchByDepthListenerVisitor(context.getSearchResultByDepthConsumer()));

        SearchResult searchResult = search.startSearch(context.getGame());

        log.debug("Tree search move found: {}", SimpleMoveEncoder.INSTANCE.encode(searchResult.getBestMove()));

        long timeSearching = Duration.between(context.getStartSearchInstant(), Instant.now()).toMillis();

        return new SearchByTreeResult(searchResult.getBestMove(), searchResult, timeSearching);
    }

    void stopSearching() {
        search.stopSearch();
    }


    void reset() {
        search.reset();
    }

    void setSyzygy(Syzygy syzygy) {
        EndGameTableBase egtb = new SyzygyAdapter(syzygy);
        search.accept(new SetEndGameTableBaseVisitor(egtb));
    }
}
