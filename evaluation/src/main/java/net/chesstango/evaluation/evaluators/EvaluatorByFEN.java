package net.chesstango.evaluation.evaluators;

import net.chesstango.gardel.fen.FENBuilder;

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
        FENBuilder fenBuilder = new FENBuilder();

        game.getPosition().export(fenBuilder);

        String fen = fenBuilder.getPositionRepresentation().toString();

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
