package net.chesstango.search;

import net.chesstango.board.Game;
import net.chesstango.evaluation.DefaultGameEvaluator;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.smart.AbstractSmart;
import net.chesstango.search.smart.AlgoWrapper;
import net.chesstango.search.smart.IterativeDeepening;
import net.chesstango.search.smart.MoveSorter;
import net.chesstango.search.smart.alphabeta.*;
import net.chesstango.search.smart.minmax.MinMax;

import java.util.Arrays;
import java.util.function.Consumer;

/**
 * @author Mauricio Coria
 */
public class DefaultSearchMove implements SearchMove {

    private final SearchMove imp;

    private Consumer<GameEvaluator> fnSetEvaluator;

    public DefaultSearchMove() {
        //this.imp = simpleAbstractSmartWrapper(setupMinMaxPruning());
        this.imp = iterateDeepingWrapper(setupMinMaxPruning());

        this.setGameEvaluator(new DefaultGameEvaluator());
    }

    private AbstractSmart setupMinMaxPruning() {

        // FILTERS START
        MoveSorter moveSorter = new MoveSorter();

        Quiescence quiescence = new Quiescence();
        quiescence.setMoveSorter(moveSorter);

        AlphaBetaImp alphaBetaImp = new AlphaBetaImp();
        alphaBetaImp.setQuiescence(quiescence);
        alphaBetaImp.setMoveSorter(moveSorter);

        DetectCycle detectCycle = new DetectCycle();
        // FILTERS END

        alphaBetaImp.setNext(detectCycle);
        detectCycle.setNext(alphaBetaImp);

        MinMaxPruning minMaxPruning = new MinMaxPruning();
        minMaxPruning.setAlphaBetaSearch(detectCycle);
        minMaxPruning.setMoveSorter(moveSorter);
        minMaxPruning.setFilters(Arrays.asList(alphaBetaImp, quiescence));

        this.fnSetEvaluator = (evaluator) -> quiescence.setGameEvaluator(evaluator);

        return minMaxPruning;
    }

    private AbstractSmart setupMinMaxPruningWithStatics() {

        // FILTERS START
        MoveSorter moveSorter = new MoveSorter();

        QuiescenceNull quiescence = new QuiescenceNull();

        AlphaBetaImp alphaBetaImp = new AlphaBetaImp();
        alphaBetaImp.setQuiescence(quiescence);
        alphaBetaImp.setMoveSorter(moveSorter);

        AlphaBetaStatistics alphaBetaStatistics = new AlphaBetaStatistics();

        //DetectCycle detectCycle = new DetectCycle();
        // FILTERS END

        alphaBetaImp.setNext(alphaBetaStatistics);
        //detectCycle.setNext(alphaBetaImp);
        alphaBetaStatistics.setNext(alphaBetaImp);

        MinMaxPruning minMaxPruning = new MinMaxPruning();
        minMaxPruning.setAlphaBetaSearch(alphaBetaStatistics);
        minMaxPruning.setMoveSorter(moveSorter);
        minMaxPruning.setFilters(Arrays.asList(alphaBetaImp, alphaBetaStatistics, quiescence));

        this.fnSetEvaluator = (evaluator) -> quiescence.setGameEvaluator(evaluator);

        return minMaxPruning;
    }


    private AbstractSmart setupMinMax() {

        MinMax minMax = new MinMax();

        this.fnSetEvaluator = (evaluator) -> minMax.setGameEvaluator(evaluator);

        return minMax;
    }

    private SearchMove iterateDeepingWrapper(AbstractSmart algorithm) {
        return new IterativeDeepening(algorithm);
    }


    private SearchMove simpleAbstractSmartWrapper(AbstractSmart algorithm) {
        return new AlgoWrapper(algorithm);
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

    public void setGameEvaluator(GameEvaluator evaluator) {
        fnSetEvaluator.accept(evaluator);
    }
}
