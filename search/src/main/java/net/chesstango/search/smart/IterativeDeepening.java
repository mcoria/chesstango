package net.chesstango.search.smart;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.search.*;
import net.chesstango.search.visitors.CollectSearchResultVisitor;
import net.chesstango.search.visitors.DistributeSearchResultVisitor;
import net.chesstango.search.visitors.SetDepthVisitor;
import net.chesstango.search.visitors.SetGameVisitor;

import java.util.function.Consumer;
import java.util.function.Predicate;


/**
 * @author Mauricio Coria
 */
public class IterativeDeepening implements Search {

    @Getter
    private final SearchAlgorithm searchAlgorithm;

    @Getter
    private final SearchListenerMediator searchListenerMediator;

    @Setter
    private int maxDepth = Integer.MAX_VALUE / 2;

    @Setter
    private Consumer<SearchResultByDepth> searchResultByDepthListener;

    @Setter
    private Predicate<SearchResultByDepth> searchPredicateParameter = searchMoveResult -> true;

    public IterativeDeepening(SearchAlgorithm searchAlgorithm, SearchListenerMediator searchListenerMediator) {
        this.searchAlgorithm = searchAlgorithm;
        this.searchListenerMediator = searchListenerMediator;
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

        searchListenerMediator.triggerBeforeSearch();

        SearchResult searchResult = new SearchResult();

        try {

            int currentSearchDepth = 1;

            boolean continueDeepening;

            // Performs iterative deepening loop until stop conditions met
            do {
                searchListenerMediator.accept(new SetDepthVisitor(currentSearchDepth));

                SearchResultByDepth searchResultByDepth = searchAlgorithm.search();

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

        } catch (StopSearchingException stopSearchingException) {
            // La profundidad actual no exploró ningun movimiento
        }

        searchListenerMediator.triggerAfterSearch();

        searchListenerMediator.accept(new CollectSearchResultVisitor(searchResult));

        searchListenerMediator.accept(new DistributeSearchResultVisitor(searchResult));

        return searchResult;
    }


    /**
     * No podemos detener si al menos no se buscó con DEPTH = 1
     *
     */
    @Override
    public void stopSearch() {
        searchListenerMediator.triggerStopSearching();
    }

    @Override
    public void reset() {
        searchListenerMediator.triggerReset();
    }
}
