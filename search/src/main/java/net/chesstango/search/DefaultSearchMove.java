package net.chesstango.search;

import net.chesstango.board.Game;
import net.chesstango.evaluation.DefaultGameEvaluator;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.smart.IterativeDeeping;
import net.chesstango.search.smart.MoveSorter;
import net.chesstango.search.smart.alphabeta.*;

import java.util.function.Consumer;

/**
 * @author Mauricio Coria
 */
public class DefaultSearchMove implements SearchMove {

    private final SearchMove imp;

    private final Consumer<GameEvaluator> fnSetEvaluator;

    public DefaultSearchMove() {
        MoveSorter moveSorter = new MoveSorter();

        AlphaBetaMoveCapturer alphaBetaMoveCapturer1 = new AlphaBetaMoveCapturer();
        QuiescenceNull quiescence = new QuiescenceNull();
        alphaBetaMoveCapturer1.setNext(quiescence);

        AlphaBetaMoveCapturer alphaBetaMoveCapturer2 = new AlphaBetaMoveCapturer();
        AlphaBetaImp alphaBetaImp = new AlphaBetaImp();
        alphaBetaImp.setQuiescence(alphaBetaMoveCapturer1);
        alphaBetaImp.setMoveSorter(moveSorter);
        alphaBetaImp.setNext(alphaBetaMoveCapturer2);
        alphaBetaMoveCapturer2.setNext(alphaBetaImp);

        MinMaxPruning minMaxPruning = new MinMaxPruning();
        minMaxPruning.setAlphaBetaSearch(alphaBetaMoveCapturer2);
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
