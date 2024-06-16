package net.chesstango.tools.tuning.fitnessfunctions;

import net.chesstango.evaluation.Evaluator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * @author Mauricio Coria
 */
public class FitnessByLeastSquareTest {

    @Test
    @Disabled
    public void testError01() {
        FitnessByLeastSquare.FeaturesValues featuresValues = new FitnessByLeastSquare.FeaturesValues();
        featuresValues.features = new int[]{1, 2, 3};
        featuresValues.expectedResult = FitnessByLeastSquare.GameResult.DRAW;

        int error = featuresValues.error(new int[]{4, 5, 6});

        Assertions.assertEquals((1 * 4 + 2 * 5 + 3 * 6), error);
    }

    @Test
    @Disabled
    public void testError02() {
        FitnessByLeastSquare.FeaturesValues featuresValues = new FitnessByLeastSquare.FeaturesValues();
        featuresValues.features = new int[]{1, 2, 3};
        featuresValues.expectedResult = FitnessByLeastSquare.GameResult.WHITE_WINS;

        int error = featuresValues.error(new int[]{4, 5, 6});

        Assertions.assertEquals( Evaluator.WHITE_WON - (1 * 4 + 2 * 5 + 3 * 6), error);
    }

    @Test
    @Disabled
    public void testError03() {
        FitnessByLeastSquare.FeaturesValues featuresValues = new FitnessByLeastSquare.FeaturesValues();
        featuresValues.features = new int[]{1, 2, 3};
        featuresValues.expectedResult = FitnessByLeastSquare.GameResult.BLACK_WINS;

        int error = featuresValues.error(new int[]{4, 5, 6});

        Assertions.assertEquals( Evaluator.BLACK_WON - (1 * 4 + 2 * 5 + 3 * 6), error);
    }
}
