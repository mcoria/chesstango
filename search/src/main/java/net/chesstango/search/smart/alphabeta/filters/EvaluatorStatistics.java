package net.chesstango.search.smart.alphabeta.filters;

import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.board.Game;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.SearchContext;
import net.chesstango.search.smart.SearchLifeCycle;
import net.chesstango.search.smart.statistics.EvaluationEntry;
import net.chesstango.search.smart.statistics.EvaluationStatistics;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Mauricio Coria
 */
public class EvaluatorStatistics implements GameEvaluator, SearchLifeCycle {
    private GameEvaluator imp;
    private long counter;
    private Set<EvaluationEntry> evaluations;

    @Setter
    @Accessors(chain = true)
    private boolean trackEvaluations;


    public EvaluatorStatistics(GameEvaluator instance) {
        this.imp = instance;
    }

    @Override
    public int evaluate(Game game) {
        counter++;
        int evaluation = imp.evaluate(game);
        if (trackEvaluations) {
            evaluations.add(new EvaluationEntry(game.getChessPosition().getZobristHash(), evaluation));
        }
        return evaluation;
    }

    @Override
    public void beforeSearch(Game game, int maxDepth) {
        counter = 0;
        if (trackEvaluations) {
            evaluations = new LinkedHashSet<>();
        }
    }

    @Override
    public void afterSearch(SearchMoveResult result) {
        if (result != null) {
            result.setEvaluationStatistics(new EvaluationStatistics(counter, evaluations));
        }
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
