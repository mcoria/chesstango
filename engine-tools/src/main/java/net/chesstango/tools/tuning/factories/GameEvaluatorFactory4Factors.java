package net.chesstango.tools.tuning.factories;

import lombok.Getter;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.evaluation.evaluators.EvaluatorSEandImp03;

/**
 * @author Mauricio Coria
 */
@Getter
public class GameEvaluatorFactory4Factors implements GameEvaluatorFactory {
    public static final int CONSTRAINT_MAX_VALUE = 1000;

    private final int factor1;
    private final int factor2;
    private final int factor3;
    private final int factor4;

    public GameEvaluatorFactory4Factors(int scalar1, int scalar2, int scalar3) {
        if (scalar1 > CONSTRAINT_MAX_VALUE || scalar2 > CONSTRAINT_MAX_VALUE || scalar3 > CONSTRAINT_MAX_VALUE) {
            throw new RuntimeException(String.format("Invalid input scalars %d %d %d", scalar1, scalar2, scalar3));
        }
        this.factor1 = scalar1;
        this.factor2 = scalar2 * (CONSTRAINT_MAX_VALUE - factor1) / CONSTRAINT_MAX_VALUE;
        this.factor3 = scalar3 * (CONSTRAINT_MAX_VALUE - factor1 - factor2) / CONSTRAINT_MAX_VALUE;
        this.factor4 = CONSTRAINT_MAX_VALUE - factor1 - factor2 - factor3;
    }

    @Override
    public Evaluator createGameEvaluator() {
        return new EvaluatorSEandImp03(factor1, factor2, factor3, factor4);
    }

    @Override
    public String getKey() {
        return toString();
    }

    @Override
    public String toString() {
        return String.format("[factor1=[%d] factor2=[%d] factor3=[%d] factor4=[%d]]", factor1, factor2, factor3, factor4);
    }

}
