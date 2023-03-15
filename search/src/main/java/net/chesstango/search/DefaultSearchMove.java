package net.chesstango.search;

import net.chesstango.board.Game;
import net.chesstango.evaluation.DefaultGameEvaluator;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.smart.AbstractSmart;
import net.chesstango.search.smart.AlgoWrapper;
import net.chesstango.search.smart.IterativeDeeping;
import net.chesstango.search.smart.MoveSorter;
import net.chesstango.search.smart.alphabeta.AlphaBetaImp;
import net.chesstango.search.smart.alphabeta.AlphaBetaStatistics;
import net.chesstango.search.smart.alphabeta.MinMaxPruning;
import net.chesstango.search.smart.alphabeta.QuiescenceNull;
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
        this.imp = simpleAbstractSmartWrapper(setupMinMaxPruning());
        //this.imp = simpleAbstractSmartWrapper(setupMinMax());

        this.setGameEvaluator(new DefaultGameEvaluator());
    }

    private AbstractSmart setupMinMaxPruning() {
        MoveSorter moveSorter = new MoveSorter();

        AlphaBetaStatistics alphaBetaStatistics1 = new AlphaBetaStatistics();
        QuiescenceNull quiescence = new QuiescenceNull();
        alphaBetaStatistics1.setNext(quiescence);

        AlphaBetaStatistics alphaBetaStatistics2 = new AlphaBetaStatistics();
        AlphaBetaImp alphaBetaImp = new AlphaBetaImp();
        alphaBetaImp.setQuiescence(alphaBetaStatistics1);
        alphaBetaImp.setMoveSorter(moveSorter);
        alphaBetaImp.setNext(alphaBetaStatistics2);
        alphaBetaStatistics2.setNext(alphaBetaImp);

        MinMaxPruning minMaxPruning = new MinMaxPruning();
        minMaxPruning.setAlphaBetaSearch(alphaBetaStatistics2);
        minMaxPruning.setMoveSorter(moveSorter);
        minMaxPruning.setFilters(Arrays.asList(alphaBetaImp, alphaBetaStatistics1, alphaBetaStatistics2, quiescence));

        this.fnSetEvaluator = (evaluator) -> quiescence.setGameEvaluator(evaluator);

        return minMaxPruning;
    }


    private AbstractSmart setupMinMax() {

        MinMax minMax = new MinMax();

        this.fnSetEvaluator = (evaluator) -> minMax.setGameEvaluator(evaluator);

        return minMax;
    }

    private SearchMove iterateDeepingWrapper(AbstractSmart algorithm) {
        return new IterativeDeeping(algorithm);
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
