package net.chesstango.search.smart;

import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.SearchInfo;
import net.chesstango.search.SearchMove;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.SearchParameter;

import java.time.Duration;
import java.time.Instant;
import java.util.LinkedList;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static net.chesstango.search.SearchParameter.MAX_DEPTH;
import static net.chesstango.search.SearchParameter.SEARCH_PREDICATE;

/**
 * @author Mauricio Coria
 */
public class IterativeDeepening implements SearchMove {
    private volatile boolean keepProcessing;
    private volatile CountDownLatch countDownLatch;
    private final SearchSmart searchSmart;

    @Setter
    private SmartListenerMediator smartListenerMediator;

    private Consumer<SearchInfo> searchStatusListener;
    private int maxDepth = Integer.MAX_VALUE;
    private Predicate<SearchInfo> searchPredicate = searchMoveResult -> true;

    public IterativeDeepening(SearchSmart searchSmartAlgorithm) {
        this.searchSmart = searchSmartAlgorithm;
    }

    @Override
    public SearchMoveResult search(final Game game) {
        keepProcessing = true;
        countDownLatch = new CountDownLatch(1);

        LinkedList<SearchMoveResult> searchMoveResults = new LinkedList<>();

        smartListenerMediator.triggerBeforeSearch(game);

        int currentSearchDepth = 1;
        SearchInfo searchInfo = null;
        SearchMoveResult searchResult = null;

        Instant startInstant = Instant.now();
        do {
            Instant startDepthInstant = Instant.now();

            SearchContext context = new SearchContext(currentSearchDepth);

            if (!searchMoveResults.isEmpty()) {
                setupContext(context, searchMoveResults.getLast());
            }

            searchSmart.beforeSearchByDepth(context);

            searchResult = searchSmart.search(context);

            searchSmart.afterSearchByDepth(searchResult);

            searchMoveResults.add(searchResult);

            Instant endDepthInstant = Instant.now();

            searchInfo = new SearchInfo(searchResult, Duration.between(startInstant, endDepthInstant).toMillis(), Duration.between(startDepthInstant, endDepthInstant).toMillis());
            if (searchStatusListener != null) {
                searchStatusListener.accept(searchInfo);
            }

            if (GameEvaluator.WHITE_WON == searchResult.getEvaluation() || GameEvaluator.BLACK_WON == searchResult.getEvaluation()) {
                break;
            }

            countDownLatch.countDown();
            currentSearchDepth++;

        } while (keepProcessing && currentSearchDepth <= maxDepth && searchPredicate.test(searchInfo));

        smartListenerMediator.triggerAfterSearch(searchResult);

        return searchResult;
    }

    /**
     * Moverlo a un filtro que sirva de memoria
     */
    private void setupContext(SearchContext context, SearchMoveResult searchMoveResult) {
        context.setLastBestMove(searchMoveResult.getBestMove());
        context.setLastBestEvaluation(searchMoveResult.getEvaluation());
        context.setLastMoveEvaluations(searchMoveResult.getMoveEvaluations());
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
        searchSmart.stopSearching();
    }

    @Override
    public void reset() {
        searchSmart.reset();
    }

    @Override
    public void setParameter(SearchParameter parameter, Object value) {
        if (SEARCH_PREDICATE.equals(parameter) && value instanceof Predicate<?> searchPredicateArg) {
            this.searchPredicate = (Predicate<SearchInfo>) searchPredicateArg;
        } else if (MAX_DEPTH.equals(parameter) && value instanceof Integer maxDepthParam) {
            this.maxDepth = maxDepthParam;
        }
    }

    public void setSearchStatusListener(Consumer<SearchInfo> searchStatusListener) {
        this.searchStatusListener = searchStatusListener;
    }

}
