package net.chesstango.search.builders;

import lombok.Getter;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.evaluation.EvaluatorCache;
import net.chesstango.search.smart.SearchListenerMediator;
import net.chesstango.search.smart.features.statistics.evaluation.EvaluatorStatisticsCollector;

/**
 * @author Mauricio Corias
 */
public class EvaluationBuilder {

    private Evaluator evaluator;

    @Getter
    private EvaluatorCache gameEvaluatorCache;

    private EvaluatorStatisticsCollector gameEvaluatorStatisticsCollector;

    private SearchListenerMediator searchListenerMediator;

    private boolean withTrackEvaluations;
    private boolean withGameEvaluatorCache;
    private boolean withStatistics;


    public EvaluationBuilder withGameEvaluator(Evaluator evaluator) {
        this.evaluator = evaluator;
        return this;
    }

    public EvaluationBuilder withGameEvaluatorCache() {
        withGameEvaluatorCache = true;
        return this;
    }

    public EvaluationBuilder withTrackEvaluations() {
        if (!withStatistics) {
            throw new RuntimeException("You must enable Statistics first");
        }
        withTrackEvaluations = true;
        return this;
    }

    public EvaluationBuilder withStatistics() {
        withStatistics = true;
        return this;
    }

    public EvaluationBuilder withSmartListenerMediator(SearchListenerMediator searchListenerMediator) {
        this.searchListenerMediator = searchListenerMediator;
        return this;
    }


    public Evaluator build() {
        buildObjects();
        setupListenerMediator();
        return evaluator;
    }

    private void buildObjects() {
        if (withGameEvaluatorCache) {
            gameEvaluatorCache = new EvaluatorCache(evaluator);

            evaluator = gameEvaluatorCache;
        }

        if (withStatistics) {
            gameEvaluatorStatisticsCollector = new EvaluatorStatisticsCollector()
                    .setImp(evaluator)
                    .setGameEvaluatorCache(gameEvaluatorCache)
                    .setTrackEvaluations(withTrackEvaluations);

            evaluator = gameEvaluatorStatisticsCollector;
        }
    }

    private void setupListenerMediator() {
        if (gameEvaluatorStatisticsCollector != null) {
            searchListenerMediator.add(gameEvaluatorStatisticsCollector);
        }
    }

}
