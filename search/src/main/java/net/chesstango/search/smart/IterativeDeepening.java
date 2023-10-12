package net.chesstango.search.smart;

import net.chesstango.board.Game;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.SearchInfo;
import net.chesstango.search.SearchMove;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.SearchParameter;

import java.util.LinkedList;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static net.chesstango.search.SearchParameter.SEARCH_PREDICATE;

/**
 * @author Mauricio Coria
 */
public class IterativeDeepening implements SearchMove {
    private volatile boolean keepProcessing;
    private volatile CountDownLatch countDownLatch;
    private final SearchSmart searchSmart;
    private Consumer<SearchInfo> searchStatusListener;

    private Predicate<SearchMoveResult> searchPredicate = searchMoveResult -> searchMoveResult.getDepth() == 1;

    public IterativeDeepening(SearchSmart searchSmartAlgorithm) {
        this.searchSmart = searchSmartAlgorithm;
    }

    @Override
    public SearchMoveResult search(final Game game) {
        keepProcessing = true;
        countDownLatch = new CountDownLatch(1);

        LinkedList<SearchMoveResult> bestMovesByDepth = new LinkedList<>();

        searchSmart.beforeSearch(game);

        int currentSearchDepth = 1;
        SearchMoveResult searchResult = null;

        do {

            SearchContext context = new SearchContext(currentSearchDepth);

            if (!bestMovesByDepth.isEmpty()) {
                setupContext(context, bestMovesByDepth.getLast());
            }

            searchSmart.beforeSearchByDepth(context);

            searchResult = searchSmart.search(context);

            searchSmart.afterSearchByDepth(searchResult);

            bestMovesByDepth.add(searchResult);

            if (searchStatusListener != null) {
                SearchInfo searchInfo = new SearchInfo(currentSearchDepth, currentSearchDepth, searchResult.getPrincipalVariation());
                searchStatusListener.accept(searchInfo);
            }

            if (GameEvaluator.WHITE_WON == searchResult.getEvaluation() || GameEvaluator.BLACK_WON == searchResult.getEvaluation()) {
                break;
            }

            countDownLatch.countDown();
            currentSearchDepth++;

        } while (keepProcessing && searchPredicate.test(searchResult));

        searchSmart.afterSearch(searchResult);

        return searchResult;
    }

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
            this.searchPredicate = (Predicate<SearchMoveResult>) searchPredicateArg;
        }
    }

    public void setSearchStatusListener(Consumer<SearchInfo> searchStatusListener) {
        this.searchStatusListener = searchStatusListener;
    }

}
