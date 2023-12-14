package net.chesstango.search.smart;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.search.*;

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
        SearchByCycleContext searchByCycleContext = new SearchByCycleContext(game);

        smartListenerMediator.triggerBeforeSearch(searchByCycleContext);

        SearchByDepthContext context = new SearchByDepthContext(maxDepth);

        smartListenerMediator.triggerBeforeSearchByDepth(context);

        MoveEvaluation bestMoveEvaluation = smartAlgorithm.search();

        SearchMoveResult searchResult = new SearchMoveResult(maxDepth, bestMoveEvaluation.evaluation(), bestMoveEvaluation.move(), null);

        smartListenerMediator.triggerAfterSearchByDepth(searchResult);

        smartListenerMediator.triggerAfterSearch();

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
