package net.chesstango.search;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.search.visitors.CollectSearchResultVisitor;
import net.chesstango.search.visitors.DistributeSearchResultVisitor;
import net.chesstango.search.visitors.SetGameVisitor;

import java.util.function.Consumer;
import java.util.function.Predicate;


/**
 * @author Mauricio Coria
 */
public class IterativeDeepening implements Search {

    @Getter
    private final SearchByDepth searchByDepth;

    @Getter
    private final ListenerMediator listenerMediator;

    @Setter
    private int maxDepth = Integer.MAX_VALUE;

    @Setter
    private Consumer<SearchResultByDepth> searchResultByDepthListener;

    @Setter
    private Predicate<SearchResultByDepth> searchPredicateParameter = searchMoveResult -> true;

    public IterativeDeepening(SearchByDepth searchByDepth, ListenerMediator listenerMediator) {
        this.searchByDepth = searchByDepth;
        this.listenerMediator = listenerMediator;
    }


    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public SearchResult startSearch(final Game game) {
        if (game.getStatus().isFinalStatus()) {
            throw new RuntimeException("Game is already finished");
        }

        accept(new SetGameVisitor(game));

        listenerMediator.triggerBeforeSearch();

        SearchResult searchResult = new SearchResult();

        int currentSearchDepth = 1;

        boolean continueDeepening;

        do {
            SearchResultByDepth searchResultByDepth = searchByDepth.search(currentSearchDepth);

            /**
             * La busqueda en profundidad actual fué detenida prematuramente
             * y no logró explorar ningun movimiento root
             */
            if (searchResultByDepth == null) {
                break;
            }

            searchResult.addSearchResultByDepth(searchResultByDepth);

            if (searchResultByDepthListener != null) {
                searchResultByDepthListener.accept(searchResultByDepth);
            }

            /**
             * Aca hay un issue; si PV.depth > currentSearchDepth quiere decir que es un mate encontrado más alla del horizonte
             * Deberiamos continuar buscando hasta que se encuentre un mate antes del horizonte
             */
            RootMoveEvaluation bestRootMoveEvaluation = searchResultByDepth.getBestRootMoveEvaluation();

            continueDeepening = !searchResultByDepth.isSearchStopped() &&
                    bestRootMoveEvaluation.evaluation() < Evaluator.WON &&
                    searchPredicateParameter.test(searchResultByDepth);

        } while (continueDeepening && ++currentSearchDepth <= maxDepth);


        listenerMediator.triggerAfterSearch();

        listenerMediator.accept(new CollectSearchResultVisitor(searchResult));

        listenerMediator.accept(new DistributeSearchResultVisitor(searchResult));

        return searchResult;
    }


    /**
     * No podemos detener si al menos no se buscó con DEPTH = 1
     *
     */
    @Override
    public void stopSearch() {
        listenerMediator.triggerStopSearching();
    }

    @Override
    public void reset() {
        listenerMediator.triggerReset();
    }
}
