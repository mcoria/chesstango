package net.chesstango.search.smart;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.search.*;
import net.chesstango.search.visitors.*;

import java.util.function.Consumer;
import java.util.function.Predicate;


/**
 * @author Mauricio Coria
 */
public class IterativeDeepening implements Search {
    private volatile boolean keepProcessing;

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
    public SearchResult startSearch(final Game game) {
        if (game.getStatus().isFinalStatus()) {
            throw new RuntimeException("Game is already finished");
        }

        keepProcessing = true;

        accept(new SetGameVisitor(game));

        searchListenerMediator.triggerBeforeSearch();

        int currentSearchDepth = 1;
        SearchResult searchResult = new SearchResult();
        SearchResultByDepth searchResultByDepth = null;
        boolean continueDeepening;

        Color currentTurn = game.getPosition().getCurrentTurn();

        // Performs iterative deepening loop until stop conditions met
        do {
            searchListenerMediator.accept(new SetDepthVisitor(currentSearchDepth));

            searchListenerMediator.triggerBeforeSearchByDepth();

            searchAlgorithm.search();

            searchListenerMediator.triggerAfterSearchByDepth();

            searchResultByDepth = new SearchResultByDepth(currentSearchDepth);

            searchListenerMediator.accept(new CollectSearchResultByDepthVisitor(searchResultByDepth));

            searchListenerMediator.accept(new DistributeSearchResultByDepthVisitor(searchResultByDepth));

            searchResult.addSearchResultByDepth(searchResultByDepth);

            if (searchResultByDepthListener != null) {
                searchResultByDepthListener.accept(searchResultByDepth);
            }

            currentSearchDepth++;

            /**
             * Aca hay un issue; si PV.depth > currentSearchDepth quiere decir que es un mate encontrado más alla del horizonte
             * Deberiamos continuar buscando hasta que se encuentre un mate antes del horizonte
             */
            RootMoveEvaluation bestRootMoveEvaluation = searchResultByDepth.getBestRootMoveEvaluation();

            continueDeepening = bestRootMoveEvaluation.evaluation() < Evaluator.WON;

        } while (keepProcessing &&
                continueDeepening &&
                currentSearchDepth <= maxDepth &&
                searchPredicateParameter.test(searchResultByDepth)
        );

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
        keepProcessing = false;
        searchListenerMediator.triggerStopSearching();
    }

    @Override
    public void reset() {
        searchListenerMediator.triggerReset();
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
