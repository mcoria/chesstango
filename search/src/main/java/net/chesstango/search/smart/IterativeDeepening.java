package net.chesstango.search.smart;

import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.*;

import java.time.Duration;
import java.time.Instant;
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
    private final SmartAlgorithm smartAlgorithm;

    @Setter
    private SmartListenerMediator smartListenerMediator;
    private ProgressListener progressListener;
    private int maxDepth = Integer.MAX_VALUE;
    private Predicate<SearchMoveResult> searchPredicate = searchMoveResult -> true;

    public IterativeDeepening(SmartAlgorithm smartAlgorithm) {
        this.smartAlgorithm = smartAlgorithm;
    }

    @Override
    public SearchMoveResult search(final Game game) {
        keepProcessing = true;
        countDownLatch = new CountDownLatch(1);


        SearchByCycleContext searchByCycleContext = new SearchByCycleContext(game);

        smartListenerMediator.triggerBeforeSearch(searchByCycleContext);

        int currentSearchDepth = 1;
        SearchMoveResult searchResult = null;
        Instant startInstant = Instant.now();
        do {
            Instant startDepthInstant = Instant.now();

            SearchByDepthContext context = new SearchByDepthContext(currentSearchDepth);

            smartListenerMediator.triggerBeforeSearchByDepth(context);

            MoveEvaluation bestMoveEvaluation = smartAlgorithm.search();

            searchResult = new SearchMoveResult(currentSearchDepth, bestMoveEvaluation.evaluation(), bestMoveEvaluation.move(), null);

            smartListenerMediator.triggerAfterSearchByDepth(searchResult);

            Instant endDepthInstant = Instant.now();
            searchResult.setTimeSearching(Duration.between(startInstant, endDepthInstant).toMillis());
            searchResult.setTimeSearchingLastDepth(Duration.between(startDepthInstant, endDepthInstant).toMillis());

            if (progressListener != null) {
                progressListener.accept(searchResult);
            }

            if (GameEvaluator.WHITE_WON == searchResult.getEvaluation() || GameEvaluator.BLACK_WON == searchResult.getEvaluation()) {
                break;
            }

            countDownLatch.countDown();
            currentSearchDepth++;

        } while (keepProcessing && currentSearchDepth <= maxDepth && searchPredicate.test(searchResult));

        smartListenerMediator.triggerAfterSearch();

        return searchResult;
    }


    /**
     * No podemos detener si al menos no se buscó con DEPTH = 1
     */
    @Override
    public void stopSearching() {
        keepProcessing = false;
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        smartListenerMediator.triggerStopSearching();
    }

    @Override
    public void reset() {
        smartListenerMediator.triggerReset();
    }

    @Override
    public void setSearchParameter(SearchParameter parameter, Object value) {
        if (SEARCH_PREDICATE.equals(parameter) && value instanceof Predicate<?> searchPredicateArg) {
            this.searchPredicate = (Predicate<SearchMoveResult>) searchPredicateArg;
        } else if (MAX_DEPTH.equals(parameter) && value instanceof Integer maxDepthParam) {
            this.maxDepth = maxDepthParam;
        }
    }

    public void setProgressListener(ProgressListener progressListener) {
        this.progressListener = progressListener;
    }

}
