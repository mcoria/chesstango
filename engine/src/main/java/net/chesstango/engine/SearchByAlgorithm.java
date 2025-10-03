package net.chesstango.engine;

import lombok.extern.slf4j.Slf4j;
import net.chesstango.board.representations.move.SimpleMoveEncoder;
import net.chesstango.search.Search;
import net.chesstango.search.SearchResult;
import net.chesstango.search.visitors.SetMaxDepthVisitor;
import net.chesstango.search.visitors.SetSearchByDepthVisitor;
import net.chesstango.search.visitors.SetSearchPredicateVisitor;

/**
 * @author Mauricio Coria
 */
@Slf4j
class SearchByAlgorithm implements SearchChain {
    private final SimpleMoveEncoder simpleMoveEncoder = new SimpleMoveEncoder();

    private final Search search;

    SearchByAlgorithm(Search search) {
        this.search = search;
    }

    @Override
    public void reset() {
        search.reset();
    }

    @Override
    public void stopSearching() {
        search.stopSearch();
    }

    @Override
    public void close() {
    }


    /**
     * TODO: deberia subsribirse al listener para manipular el latcher que habilita el stop
     *
     * @param context
     * @return
     */
    @Override
    public SearchResult search(SearchContext context) {
        search.accept(new SetMaxDepthVisitor(context.getDepth()));
        search.accept(new SetSearchPredicateVisitor(context.getSearchPredicate()));

        search.accept(new SetSearchByDepthVisitor(context.getSearchResultByDepthListener()));

        SearchResult result = search.startSearch(context.getGame());
        log.debug("Move found: {}", simpleMoveEncoder.encode(result.getBestMove()));
        return result;
    }
}
