package net.chesstango.search.smart.alphabeta.statistics.evaluation;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.board.Game;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.evaluation.EvaluatorCache;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.SearchByCycleListener;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Mauricio Coria
 */
public class EvaluatorStatisticsCollector implements Evaluator, SearchByCycleListener, Acceptor {

    @Setter
    @Getter
    @Accessors(chain = true)
    private Evaluator imp;

    @Setter
    @Getter
    @Accessors(chain = true)
    private EvaluatorCache gameEvaluatorCache;

    @Setter
    @Accessors(chain = true)
    private boolean trackEvaluations;

    private long evaluationsCounter;
    private Set<EvaluationEntry> evaluations;
    private Game game;


    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int evaluate() {
        evaluationsCounter++;
        int evaluation = imp.evaluate();
        if (trackEvaluations) {
            long hash = game.getPosition().getZobristHash();
            evaluations.add(new EvaluationEntry(hash, evaluation));
        }
        return evaluation;
    }

    @Override
    public void setGame(Game game) {
        this.game = game;
        imp.setGame(game);
    }

    @Override
    public void beforeSearch() {
        evaluationsCounter = 0;
        if (trackEvaluations) {
            evaluations = new LinkedHashSet<>();
        }
        if (gameEvaluatorCache != null) {
            gameEvaluatorCache.resetCacheHitsCounter();
        }
    }

    public EvaluationStatistics getEvaluationStatistics() {
        long cacheHitsCounter = gameEvaluatorCache != null ? gameEvaluatorCache.getCacheHitsCounter() : 0;
        return new EvaluationStatistics(evaluationsCounter, cacheHitsCounter, evaluations);
    }

}
