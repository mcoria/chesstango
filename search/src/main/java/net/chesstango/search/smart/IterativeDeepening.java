package net.chesstango.search.smart;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.function.Predicate;

import static net.chesstango.search.SearchParameter.MAX_DEPTH;
import static net.chesstango.search.SearchParameter.SEARCH_PREDICATE;

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

        List<SearchByDepthResult> searchByDepthResultList = new ArrayList<>();

        SearchByCycleContext searchByCycleContext = new SearchByCycleContext(game);

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

            searchByDepthResultList.add(searchByDepthResult);

            smartListenerMediator.triggerAfterSearchByDepth(searchByDepthResult);

            if (progressListener != null) {
                progressListener.accept(searchByDepthResult);
            }

            countDownLatch.countDown();
            currentSearchDepth++;

        } while (keepProcessing &&
                currentSearchDepth <= maxDepth &&
                searchPredicate.test(searchByDepthResult) &&
                GameEvaluator.WHITE_WON != bestMoveEvaluation.evaluation() &&
                GameEvaluator.BLACK_WON != bestMoveEvaluation.evaluation()
        );

        SearchMoveResult searchResult = new SearchMoveResult(currentSearchDepth - 1, bestMoveEvaluation, null);
        searchResult.setSearchByDepthResultList(searchByDepthResultList);

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
    }


}
