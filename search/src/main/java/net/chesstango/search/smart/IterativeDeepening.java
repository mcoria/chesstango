package net.chesstango.search.smart;

import net.chesstango.board.Game;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.SearchInfo;
import net.chesstango.search.SearchMove;
import net.chesstango.search.SearchMoveResult;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

/**
 * @author Mauricio Coria
 */
public class IterativeDeepening implements SearchMove {
    private volatile boolean keepProcessing;
    private volatile CountDownLatch countDownLatch;
    private final SearchSmart searchSmart;
    private Consumer<SearchInfo> searchStatusListener;

    public IterativeDeepening(SearchSmart searchSmartAlgorithm) {
        this.searchSmart = searchSmartAlgorithm;
    }

    @Override
    public SearchMoveResult search(final Game game, final int depth) {
        keepProcessing = true;
        countDownLatch = new CountDownLatch(1);

        List<SearchMoveResult> bestMovesByDepth = new ArrayList<>();

        searchSmart.beforeSearch(game, depth);

        for (int currentSearchDepth = 1; currentSearchDepth <= depth && keepProcessing; currentSearchDepth++) {

            SearchContext context = new SearchContext(currentSearchDepth);

            searchSmart.beforeSearchByDepth(context);

            SearchMoveResult searchResult = searchSmart.search(context);

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
        }

        SearchMoveResult bestMove = bestMovesByDepth.get(bestMovesByDepth.size() - 1);

        searchSmart.afterSearch(bestMove);

        return bestMove;
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

    public void setSearchStatusListener(Consumer<SearchInfo> searchStatusListener) {
        this.searchStatusListener = searchStatusListener;
    }

}
