package net.chesstango.search;

import net.chesstango.board.Game;
import net.chesstango.evaluation.DefaultGameEvaluator;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.builders.MinMaxPruningBuilder;

/**
 * @author Mauricio Coria
 */
public class DefaultSearchMove implements SearchMove {
    private final SearchMove imp;

    public DefaultSearchMove() {
        this(new DefaultGameEvaluator());
    }

    public DefaultSearchMove(final GameEvaluator gameEvaluator) {
        this.imp = new MinMaxPruningBuilder()
                .withGameEvaluator(gameEvaluator)

                //.withDetectCycle()

                .withQuiescence()

                .withTranspositionTable()
                .withQTranspositionTable()

                .withTranspositionMoveSorter()
                .withQTranspositionMoveSorter()

                .withGameRevert()

                .withIterativeDeepening()

                .withStatics()

                .build();
    }

    @Override
    public SearchMoveResult searchInfinite(Game game) {
        return imp.searchInfinite(game);
    }

    @Override
    public SearchMoveResult searchUpToDepth(Game game, int depth) {
        return imp.searchUpToDepth(game, depth);
    }

    @Override
    public SearchMoveResult searchUpToTime(Game game, int msTimeout) {
        return imp.searchUpToTime(game, msTimeout);
    }

    @Override
    public void stopSearching() {
        imp.stopSearching();
    }

    @Override
    public void reset() {
        imp.reset();
    }
}
