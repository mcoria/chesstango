package net.chesstango.search;

import net.chesstango.board.Game;
import net.chesstango.evaluation.DefaultGameEvaluator;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.smart.AbstractSmart;
import net.chesstango.search.smart.IterativeDeepening;
import net.chesstango.search.smart.MoveSorter;
import net.chesstango.search.smart.NoIterativeDeepening;
import net.chesstango.search.smart.alphabeta.*;
import net.chesstango.search.smart.minmax.MinMax;

import java.util.Arrays;
import java.util.function.Consumer;

/**
 * @author Mauricio Coria
 */
public class DefaultSearchMove implements SearchMove {
    private final SearchMove imp;

    private final GameEvaluator gameEvaluator;

    public DefaultSearchMove() {
        this(new DefaultGameEvaluator());
    }

    public DefaultSearchMove(final GameEvaluator gameEvaluator) {
        this.gameEvaluator = gameEvaluator;
        this.imp = withoutIterateDeepening(setupMinMaxPruning());
        //this.imp = withIterateDeepening(setupMinMaxPruning());
    }


    @Override
    public SearchMoveResult searchBestMove(Game game) {
        return imp.searchBestMove(game);
    }

    @Override
    public SearchMoveResult searchBestMove(Game game, int depth) {
        return imp.searchBestMove(game, depth);
    }

    @Override
    public void stopSearching() {
        imp.stopSearching();
    }

    protected AbstractSmart setupMinMaxPruning() {
        return new MinMaxPruningBuilder()
                .withGameEvaluator(gameEvaluator)
                .withStatics()
                //.withQuiescence()
                .build();
    }


    protected AbstractSmart setupMinMax() {

        MinMax minMax = new MinMax();

        minMax.setGameEvaluator(gameEvaluator);

        return minMax;
    }

    protected SearchMove withIterateDeepening(AbstractSmart algorithm) {
        return new IterativeDeepening(algorithm);
    }


    protected SearchMove withoutIterateDeepening(AbstractSmart algorithm) {
        return new NoIterativeDeepening(algorithm);
    }
}
