package net.chesstango.search.visitors;

import net.chesstango.evaluation.evaluators.EvaluatorByMaterial;
import net.chesstango.search.Search;
import net.chesstango.search.builders.AlphaBetaBuilder;
import net.chesstango.search.smart.features.debug.DebugNodeTrap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Mauricio Coria
 */
public class ChainPrinterVisitorTest {

    private ChainPrinterVisitor chainPrinterVisitor;
    private DebugNodeTrap debugNodeTrap;

    @BeforeEach
    public void setup() {
        chainPrinterVisitor = new ChainPrinterVisitor(false);
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
                .withDebugSearchTree(debugNodeTrap, false, true, true)
                .withStatistics();

        Search search = builder.build();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (PrintStream out = new PrintStream(baos, true, StandardCharsets.UTF_8)) { // true for autoFlush
            chainPrinterVisitor.print(search, out);
        }

        // Convert the captured bytes to a String
        String actualPrintChain = baos.toString(StandardCharsets.UTF_8);

        //System.out.println("Captured Output:\n" + actualPrintChain);

        String expectedPrintChain = readResource("printChain.txt");

        assertEquals(expectedPrintChain, actualPrintChain);

    }

    private String readResource(String resourceName) {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourceName);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
        ) {
            return reader.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
