package net.chesstango.search;

import net.chesstango.board.Game;
import net.chesstango.evaluation.DefaultGameEvaluator;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.smart.MoveSorter;
import net.chesstango.search.smart.alphabeta.AlphaBetaImp;
import net.chesstango.search.smart.alphabeta.AlphaBetaStatistics;
import net.chesstango.search.smart.alphabeta.MinMaxPruning;
import net.chesstango.search.smart.alphabeta.QuiescenceNull;

import java.util.function.Consumer;

/**
 * @author Mauricio Coria
 */
public class DefaultSearchMove implements SearchMove {

    private final SearchMove imp;

    private final Consumer<GameEvaluator> fnSetEvaluator;

    public DefaultSearchMove() {
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

        this.imp = minMaxPruning;
        this.fnSetEvaluator = (evaluator) -> quiescence.setGameEvaluator(evaluator);

        this.setGameEvaluator(new DefaultGameEvaluator());
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
