package net.chesstango.search;

import net.chesstango.board.Game;
import net.chesstango.evaluation.DefaultEvaluator;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.builders.AlphaBetaBuilder;

/**
 * @author Mauricio Coria
 */
public class DefaultSearchMove implements SearchMove {

    private final SearchMove imp;


    public DefaultSearchMove() {
        this(new DefaultEvaluator());
    }

    public DefaultSearchMove(final GameEvaluator gameEvaluator) {
        this.imp = new AlphaBetaBuilder()
                .withGameEvaluator(gameEvaluator)
                .withGameEvaluatorCache()

                .withQuiescence()

                .withTranspositionTable()
                .withQTranspositionTable()
                //.withTranspositionTableReuse()

                .withTranspositionMoveSorter()
                .withQTranspositionMoveSorter()
                .withComposedMoveSorter()

                .withAspirationWindows()
                .withIterativeDeepening()

                .withStopProcessingCatch()

                //.withStatics()

                .build();
    }

    @Override
    public SearchMoveResult search(Game game) {
        return imp.search(game);
    }

    @Override
    public void stopSearching() {
        imp.stopSearching();
    }

    @Override
    public void reset() {
        imp.reset();
    }

    @Override
    public void setSearchParameter(SearchParameter parameter, Object value) {
        imp.setSearchParameter(parameter, value);
    }

    @Override
    public void setProgressListener(ProgressListener progressListener) {
        imp.setProgressListener(progressListener);
    }

    public SearchMove getImplementation() {
        return imp;
    }
}
