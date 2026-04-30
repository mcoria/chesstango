package net.chesstango.evaluation.evaluators;

import net.chesstango.gardel.fen.FENStringBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Mauricio Coria
 */
public class EvaluatorByFEN extends AbstractEvaluator {
    private int defaultValue;

    private final Map<String, Integer> evaluations = new HashMap<>();

    @Override
    protected int evaluateNonFinalStatus() {
        FENStringBuilder fenBuilder = new FENStringBuilder();

        game.getPosition().export(fenBuilder);

        String fen = fenBuilder.getPositionRepresentation();

        return evaluations.getOrDefault(fen, defaultValue);
    }


    public EvaluatorByFEN setDefaultValue(int defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    public EvaluatorByFEN addEvaluation(String fen, int evaluation) {
        evaluations.put(fen, evaluation);
        return this;
    }

}
