package net.chesstango.tools.tuning.factories;

import lombok.Getter;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.evaluation.evaluators.EvaluatorImp06;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mauricio Coria
 */
@Getter
public class EvaluatorImp06Factory implements GameEvaluatorFactory {
    public static final int CONSTRAINT_MAX_VALUE = 1000;

    private static final Logger logger = LoggerFactory.getLogger(EvaluatorImp06Factory.class);

    private final int[] weighs;

    public EvaluatorImp06Factory(int[] weighs) {
        this.weighs = weighs;
    }

    @Override
    public Evaluator createGameEvaluator() {
        return new EvaluatorImp06(weighs);
    }

    @Override
    public String getKey() {
        return toString();
    }

    @Override
    public void dump(long points) {
    }

    @Override
    public String toString() {
        return String.format("[factor1=[%d] factor2=[%d] factor3=[%d] factor4=[%d] factor5=[%d] factor6=[%d]]", weighs[0], weighs[1], weighs[2], weighs[3], weighs[4], weighs[5]);
    }

}
