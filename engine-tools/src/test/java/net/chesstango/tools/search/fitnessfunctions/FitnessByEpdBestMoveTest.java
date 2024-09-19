package net.chesstango.tools.search.fitnessfunctions;

import net.chesstango.evaluation.evaluators.EvaluatorByMaterial;
import net.chesstango.tools.tuning.fitnessfunctions.FitnessByEpdBestMove;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Mauricio Coria
 */
public class FitnessByEpdBestMoveTest {
    private FitnessByEpdBestMove fitnessByEpdBestMove;

    @BeforeEach
    public void setup() {
        fitnessByEpdBestMove = new FitnessByEpdBestMove();
        fitnessByEpdBestMove.start();
    }

    @AfterEach
    public void tearDown() {
        fitnessByEpdBestMove.stop();
    }

    /**
     * Estoy trabajando acá, observar que los volores para EvaluatorByMaterial no coinciden con los comunmentes conocidos
     */
    @Test
    @Disabled
    public void test01() {
        long fitness = fitnessByEpdBestMove.fitness(EvaluatorByMaterial::new);
        assertEquals(246, fitness);
    }

    @Test
    @Disabled
    public void test02() {
        long fitness = fitnessByEpdBestMove.fitness(() ->
                new EvaluatorByMaterial(
                        EvaluatorByMaterial.readValues("{\"id\":\"01fd3aaa\",\"pawn\":5,\"knight\":4,\"bishop\":7,\"rook\":6,\"queen\":9}"))
        );
        assertEquals(222, fitness);
    }
}
