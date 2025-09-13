package net.chesstango.search.visitors;

import net.chesstango.evaluation.evaluators.EvaluatorByMaterial;
import net.chesstango.search.Search;
import net.chesstango.search.builders.AlphaBetaBuilder;
import net.chesstango.search.smart.features.debug.DebugNodeTrap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

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

        InputStream inputStream = new ByteArrayInputStream(baos.toByteArray());
        List<String> actualPrintChain = readInputStream(inputStream);

        List<String> expectedPrintChain = readResource("printChain.txt");

        assertEquals(expectedPrintChain.size(), actualPrintChain.size());

        for (int i = 0; i < expectedPrintChain.size(); i++) {
            assertEquals(expectedPrintChain.get(i), actualPrintChain.get(i), "Line " + (i + 1));
        }
    }

    private List<String> readResource(String resourceName) {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourceName);) {
            return readInputStream(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private List<String> readInputStream(InputStream inputStream) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line.trim());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return lines;
    }
}
