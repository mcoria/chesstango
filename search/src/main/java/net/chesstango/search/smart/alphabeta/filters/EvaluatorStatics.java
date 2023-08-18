package net.chesstango.search.smart.alphabeta.filters;

import net.chesstango.board.Game;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.SearchContext;
import net.chesstango.search.smart.SearchLifeCycle;
import net.chesstango.search.smart.statics.EvaluationEntry;
import net.chesstango.search.smart.statics.EvaluationStatics;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Mauricio Coria
 */
public class EvaluatorStatics implements GameEvaluator, SearchLifeCycle {
    private GameEvaluator imp;
    private long counter;
    private Set<EvaluationEntry> evaluations;


    public EvaluatorStatics(GameEvaluator instance) {
        this.imp = instance;
    }

    @Override
    public int evaluate(Game game) {
        counter++;
        int evaluation = imp.evaluate(game);
        evaluations.add(new EvaluationEntry(game.getChessPosition().getZobristHash(), evaluation));
        return evaluation;
    }

    @Override
    public void beforeSearch(Game game, int maxDepth) {
        counter = 0;
        evaluations = new LinkedHashSet<>();
    }

    @Override
    public void afterSearch(SearchMoveResult result) {
        if (result != null) {
            result.setEvaluationStatics(new EvaluationStatics(counter, evaluations));
        }
        counter = 0;
        evaluations = null;
    }

    @Override
    public void beforeSearchByDepth(SearchContext context) {
    }

    @Override
    public void afterSearchByDepth(SearchMoveResult result) {
    }

    @Override
    public void stopSearching() {
    }

    @Override
    public void reset() {
    }

}
