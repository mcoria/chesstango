package net.chesstango.tools.tuning.factories;

import lombok.Getter;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.evaluation.evaluators.EvaluatorImp05;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mauricio Coria
 */
@Getter
public class EvaluatorImp05Factory implements GameEvaluatorFactory {
    public static final int CONSTRAINT_MAX_VALUE = 1000;

    private static final Logger logger = LoggerFactory.getLogger(EvaluatorImp05Factory.class);

    private final int[] weighs;

    public EvaluatorImp05Factory(int[] weighs) {
        this.weighs = weighs;
    }

    @Override
    public Evaluator createGameEvaluator() {
        return new EvaluatorImp05(weighs);
    }

    @Override
    public String getKey() {
        return toString();
    }

    @Override
    public String getRepresentation() {
        return toString();
    }

    @Override
    public String toString() {
        return String.format("[factor1=[%d] factor2=[%d] factor3=[%d]]", weighs[0], weighs[1], weighs[2]);
    }

}
