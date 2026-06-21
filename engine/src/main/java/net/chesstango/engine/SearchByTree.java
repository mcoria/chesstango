package net.chesstango.engine;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.chesstango.board.representations.move.SimpleMoveEncoder;
import net.chesstango.piazzolla.syzygy.Syzygy;
import net.chesstango.search.Search;
import net.chesstango.search.SearchBuilder;
import net.chesstango.search.SearchResult;
import net.chesstango.search.smart.alphabeta.egtb.EndGameTableBase;
import net.chesstango.search.smart.alphabeta.egtb.visitors.LinkEndGameTableBaseVisitor;
import net.chesstango.search.smart.alphabeta.transposition.visitors.SetTTableHashSizeVisitor;
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
    private final TangoFactory tangoFactory;
    private final Search search;

    SearchByTree(TangoFactory tangoFactory, Config config) {
        this.tangoFactory = tangoFactory;

        if (config.getSearch() != null && config.getEvaluator() != null) {
            log.warn("Search and evaluator are set. Evaluator will be ignored");
        }

        if (config.getSearch() == null) {
            SearchBuilder<?> searchBuilder = tangoFactory.createSearchBuilder();

            searchBuilder.withGameEvaluator(config.getEvaluator());

            if (config.getHashSizeMB() != null) {
                searchBuilder.withTranspositionHashSize(config.getHashSizeMB() * 1024);
            }

            if (config.getStaleAge() != null) {
                searchBuilder.withTranspositionStaleAge(config.getStaleAge());
            }

            search = searchBuilder.build();
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
        EndGameTableBase egtb = tangoFactory.createSyzygyTableBaseAdapter(syzygy);
        search.accept(new LinkEndGameTableBaseVisitor(egtb));
    }

    /**
     *
     * @param hashSizeMB in megabytes
     */
    void setHashSize(int hashSizeMB) {
        // Convertirlo a KB
        search.accept(new SetTTableHashSizeVisitor(hashSizeMB * 1024));
    }
}
