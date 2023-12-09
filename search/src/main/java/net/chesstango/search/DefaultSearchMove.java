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
        this(null);
    }

    public DefaultSearchMove(final SearchListener searchListener) {
        this(new DefaultEvaluator(), searchListener);
    }

    public DefaultSearchMove(final GameEvaluator gameEvaluator, final SearchListener searchListener) {
        this.imp = new AlphaBetaBuilder()
                .withGameEvaluator(gameEvaluator)
                .withGameEvaluatorCache()

                .withQuiescence()

                .withTranspositionTable()
                .withQTranspositionTable()
                //.withTranspositionTableReuse()

                .withTranspositionMoveSorter()
                .withQTranspositionMoveSorter()

                .withAspirationWindows()
                .withIterativeDeepening()

                .withStopProcessingCatch()

                //.withStatics()

                .withSearchListener(searchListener)

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
    public void setParameter(SearchParameter parameter, Object value) {
        imp.setParameter(parameter, value);
    }

    public SearchMove getImplementation() {
        return imp;
    }
}
