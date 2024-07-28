package net.chesstango.search.smart;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.representations.epd.EPD;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.search.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.function.Predicate;

import static net.chesstango.search.SearchParameter.*;
import static net.chesstango.search.SearchParameter.EPD_PARAMS;

/**
 * @author Mauricio Coria
 */
public class IterativeDeepening implements SearchMove {
    private volatile boolean keepProcessing;
    private volatile CountDownLatch countDownLatch;

    @Getter
    private final SmartAlgorithm smartAlgorithm;

    @Getter
    private final SmartListenerMediator smartListenerMediator;

    @Setter
    private ProgressListener progressListener;

    private final Map<SearchParameter, Object> searchParameters = new HashMap<>();

    private int maxDepth = Integer.MAX_VALUE;

    private Predicate<SearchByDepthResult> searchPredicate = searchMoveResult -> true;

    public IterativeDeepening(SmartAlgorithm smartAlgorithm, SmartListenerMediator smartListenerMediator) {
        this.smartAlgorithm = smartAlgorithm;
        this.smartListenerMediator = smartListenerMediator;
    }

    @Override
    public SearchMoveResult search(final Game game) {
        keepProcessing = true;
        countDownLatch = new CountDownLatch(1);

        List<SearchByDepthResult> searchByDepthResults = new ArrayList<>();

        SearchByCycleContext searchByCycleContext = new SearchByCycleContext(game);
        searchByCycleContext.setSearchParameters(searchParameters);

        smartListenerMediator.triggerBeforeSearch(searchByCycleContext);

        int currentSearchDepth = 1;
        SearchByDepthResult searchByDepthResult = null;
        MoveEvaluation bestMoveEvaluation = null;
        do {
            SearchByDepthContext context = new SearchByDepthContext(currentSearchDepth);

            smartListenerMediator.triggerBeforeSearchByDepth(context);

            bestMoveEvaluation = smartAlgorithm.search();

            searchByDepthResult = new SearchByDepthResult();
            searchByDepthResult.setDepth(currentSearchDepth);
            searchByDepthResult.setBestMoveEvaluation(bestMoveEvaluation);

            searchByDepthResults.add(searchByDepthResult);

            smartListenerMediator.triggerAfterSearchByDepth(searchByDepthResult);

            if (progressListener != null) {
                progressListener.accept(searchByDepthResult);
            }

            countDownLatch.countDown();
            currentSearchDepth++;

        } while (keepProcessing &&
                currentSearchDepth <= maxDepth &&
                searchPredicate.test(searchByDepthResult) &&
                Evaluator.WHITE_WON != bestMoveEvaluation.evaluation() &&
                Evaluator.BLACK_WON != bestMoveEvaluation.evaluation()
        );

        SearchMoveResult searchResult = new SearchMoveResult(currentSearchDepth - 1, bestMoveEvaluation, null);
        searchResult.setSearchByDepthResults(searchByDepthResults);

        smartListenerMediator.triggerAfterSearch(searchResult);

        return searchResult;
    }


    /**
     * No podemos detener si al menos no se buscó con DEPTH = 1
     */
    @Override
    public void stopSearching() {
        keepProcessing = false;
        try {
            // Espera que al menos se complete un ciclo
            // Aca se puede dar la interrupcion
            countDownLatch.await();

            smartListenerMediator.triggerStopSearching();
        } catch (InterruptedException e) {
            // Si ocurre la excepcion quiere decir que terminó normalmente y el thread fué interrumpido, por lo tanto no es necesario triggerStopSearching()
        }
    }

    @Override
    public void reset() {
        smartListenerMediator.triggerReset();
    }

    @Override
    public void setSearchParameter(SearchParameter parameter, Object value) {
        if (SEARCH_PREDICATE.equals(parameter) && value instanceof Predicate<?> searchPredicateArg) {
            this.searchPredicate = (Predicate<SearchByDepthResult>) searchPredicateArg;
        } else if (MAX_DEPTH.equals(parameter) && value instanceof Integer maxDepthParam) {
            this.maxDepth = maxDepthParam;
        }

        if (EPD_PARAMS.equals(parameter) && value instanceof EPD epd) {
            searchParameters.put(EPD_PARAMS, epd);
        }
    }


}
