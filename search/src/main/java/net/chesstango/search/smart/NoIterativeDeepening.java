package net.chesstango.search.smart;

import lombok.Getter;
import net.chesstango.board.Game;
import net.chesstango.search.*;

import java.util.ArrayList;
import java.util.List;

import static net.chesstango.search.SearchParameter.MAX_DEPTH;

/**
 * @author Mauricio Coria
 */
public class NoIterativeDeepening implements SearchMove {

    @Getter
    private final SmartAlgorithm smartAlgorithm;

    @Getter
    private final SmartListenerMediator smartListenerMediator;

    private int maxDepth = Integer.MAX_VALUE;

    public NoIterativeDeepening(SmartAlgorithm smartAlgorithm, SmartListenerMediator smartListenerMediator) {
        this.smartAlgorithm = smartAlgorithm;
        this.smartListenerMediator = smartListenerMediator;
    }

    @Override
    public SearchMoveResult search(Game game) {
        List<SearchByDepthResult> searchByDepthResultList = new ArrayList<>();

        SearchByCycleContext searchByCycleContext = new SearchByCycleContext(game);

        smartListenerMediator.triggerBeforeSearch(searchByCycleContext);

        SearchByDepthContext context = new SearchByDepthContext(maxDepth);

        smartListenerMediator.triggerBeforeSearchByDepth(context);

        MoveEvaluation bestMoveEvaluation = smartAlgorithm.search();

        SearchByDepthResult searchByDepthResult = new SearchByDepthResult();
        searchByDepthResult.setDepth(maxDepth);
        searchByDepthResult.setBestMoveEvaluation(bestMoveEvaluation);

        searchByDepthResultList.add(searchByDepthResult);

        smartListenerMediator.triggerAfterSearchByDepth(searchByDepthResult);

        SearchMoveResult searchResult = new SearchMoveResult(maxDepth, bestMoveEvaluation, null);

        searchResult.setSearchByDepthResults(searchByDepthResultList);

        smartListenerMediator.triggerAfterSearch(searchResult);

        return searchResult;
    }

    @Override
    public void stopSearching() {
        this.smartListenerMediator.triggerStopSearching();
    }

    @Override
    public void reset() {
        this.smartListenerMediator.triggerReset();
    }

    @Override
    public void setSearchParameter(SearchParameter parameter, Object value) {
        if (MAX_DEPTH.equals(parameter) && value instanceof Integer maxDepthParam) {
            maxDepth = maxDepthParam;
        }
    }

    @Override
    public void setProgressListener(ProgressListener progressListener) {

    }

}
