package net.chesstango.tools.search.fitnessfunctions;

import net.chesstango.evaluation.evaluators.EvaluatorByMaterial;
import net.chesstango.evaluation.evaluators.EvaluatorImp05;
import net.chesstango.evaluation.evaluators.EvaluatorImp06;
import net.chesstango.tools.tuning.fitnessfunctions.FitnessByEpdBottomMoveCounter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Mauricio Coria
 */
@Disabled
public class FitnessByEpdBottomMoveCounterTest {
    private FitnessByEpdBottomMoveCounter fitnessByEpdBottomMoveCounter;

    @BeforeEach
    public void setup() {
        fitnessByEpdBottomMoveCounter = new FitnessByEpdBottomMoveCounter();
        fitnessByEpdBottomMoveCounter.start();
    }

    @AfterEach
    public void tearDown() {
        fitnessByEpdBottomMoveCounter.stop();
    }

    /**
     * Estoy trabajando acá, observar que los volores para EvaluatorByMaterial no coinciden con los comunmente conocidos
     */
    @Test
    @Disabled
    public void test01() {
        long fitness = fitnessByEpdBottomMoveCounter.fitness(() ->
                new EvaluatorByMaterial("{\"id\":\"01fd3aaa\",\"pawn\":5,\"knight\":4,\"bishop\":7,\"rook\":6,\"queen\":9}")
        );
        assertEquals(56037, fitness);
    }

    @Test
    @Disabled
    public void test02() {
        long fitness = fitnessByEpdBottomMoveCounter.fitness(EvaluatorByMaterial::new);
        assertEquals(53482, fitness);
    }

    @Test
    @Disabled
    public void test03() {
        long fitness = fitnessByEpdBottomMoveCounter.fitness(EvaluatorImp05::new);
        assertEquals(84368, fitness);
    }

    @Test
    @Disabled
    public void test04() {
        long fitness = fitnessByEpdBottomMoveCounter.fitness(EvaluatorImp06::new);
        assertEquals(84466, fitness);
    }
}
