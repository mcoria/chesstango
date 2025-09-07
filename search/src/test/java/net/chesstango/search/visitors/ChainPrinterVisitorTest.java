package net.chesstango.search.visitors;

import net.chesstango.evaluation.evaluators.EvaluatorByMaterial;
import net.chesstango.search.Search;
import net.chesstango.search.builders.AlphaBetaBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Mauricio Coria
 */
public class ChainPrinterVisitorTest {

    private ChainPrinterVisitor chainPrinterVisitor;

    @BeforeEach
    public void setup() {
        chainPrinterVisitor = new ChainPrinterVisitor();
    }

    @Test
    public void printChain() {
        AlphaBetaBuilder builder = new AlphaBetaBuilder()
                .withGameEvaluator(new EvaluatorByMaterial())
                .withGameEvaluatorCache()

                //.withExtensionCheckResolver()
                .withQuiescence()

                //.withTriangularPV()
                .withTranspositionTable()

                .withTranspositionMoveSorter()
                .withKillerMoveSorter()
                .withRecaptureSorter()
                .withMvvLvaSorter()

                .withAspirationWindows()
                .withIterativeDeepening()

                //.withStopProcessingCatch()
                //.withZobristTracker()
                //.withTrackEvaluations() // Consume demasiada memoria
                //.withDebugSearchTree(debugNodeTrap, false, true, true)
                .withStatistics()
                //.withPrintChain()
                ;

        Search search = builder.build();


        chainPrinterVisitor.print(search);
    }

}
