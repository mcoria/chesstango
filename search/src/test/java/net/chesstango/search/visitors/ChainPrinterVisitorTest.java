package net.chesstango.search.visitors;

import net.chesstango.board.Game;
import net.chesstango.evaluation.evaluators.EvaluatorByMaterial;
import net.chesstango.search.Search;
import net.chesstango.search.builders.AlphaBetaBuilder;
import net.chesstango.search.smart.features.debug.DebugNodeTrap;
import net.chesstango.search.smart.features.debug.model.DebugNode;
import net.chesstango.search.smart.features.egtb.EndGameTableBase;
import net.chesstango.search.smart.features.egtb.visitors.SetEndGameTableBaseVisitor;
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

    /**
     * Controls whether the output of the chain structure will be printed during testing.
     * If set to {@code true}, detailed chain-related information is printed;
     * otherwise, chain-related output is suppressed.
     * Used for debugging purposes within the test class.
     */
    private static final boolean PRINT_CHAIN = true;

    private ChainPrinterVisitor chainPrinterVisitor;
    private DebugNodeTrap debugNodeTrap;

    @BeforeEach
    public void setup() {
        chainPrinterVisitor = new ChainPrinterVisitor(false);
        debugNodeTrap = new DebugNodeTrap() {
            @Override
            public boolean test(DebugNode debugNode) {
                return false;
            }

            @Override
            public void debugAction(DebugNode debugNode, PrintStream debugOut) {

            }
        };
    }

    @Test
    public void alphaBetaBuilderChainDefault() throws IOException {
        AlphaBetaBuilder builder = AlphaBetaBuilder
                .createDefaultBuilderInstance()
                .withGameEvaluator(new EvaluatorByMaterial());

        Search search = builder.build();

        assertSearchTree(search, "alphaBetaBuilderChainDefault.txt");
    }

    @Test
    public void alphaBetaBuilderChainWithEGTB() throws IOException {
        AlphaBetaBuilder builder = AlphaBetaBuilder
                .createDefaultBuilderInstance()
                .withGameEvaluator(new EvaluatorByMaterial());

        Search search = builder.build();

        search.accept(new SetEndGameTableBaseVisitor(new MyEndGameTableBaseExtension()));

        assertSearchTree(search, "alphaBetaBuilderChainWithEGTB.txt");
    }

    @Test
    public void alphaBetaBuilderChainWithStatistics() throws IOException {
        AlphaBetaBuilder builder = AlphaBetaBuilder
                .createDefaultBuilderInstance()
                .withStatistics()
                .withGameEvaluator(new EvaluatorByMaterial());

        Search search = builder.build();

        assertSearchTree(search, "alphaBetaBuilderChainStatistics.txt");
    }

    @Test
    public void alphaBetaBuilderChainNoOption() throws IOException {
        AlphaBetaBuilder builder = new AlphaBetaBuilder()
                .withGameEvaluator(new EvaluatorByMaterial());

        Search search = builder.build();

        assertSearchTree(search, "alphaBetaBuilderChainNoOption.txt");
    }

    @Test
    public void alphaBetaBuilderChainDebugSearchTree() throws IOException {
        AlphaBetaBuilder builder = AlphaBetaBuilder
                .createDefaultBuilderInstance()
                .withGameEvaluator(new EvaluatorByMaterial())
                .withDebugSearchTree(false, true, true);

        Search search = builder.build();

        assertSearchTree(search, "alphaBetaBuilderChainDebug.txt");
    }

    private void assertSearchTree(Search search, String resourceName) throws IOException {
        List<String> expectedPrintChain = readResource(resourceName);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (PrintStream out = new PrintStream(baos, true, StandardCharsets.UTF_8);) {
            if (PRINT_CHAIN) {
                chainPrinterVisitor.print(search, System.out);
            }
            chainPrinterVisitor.print(search, out);
        }

        try (InputStream inputStream = new ByteArrayInputStream(baos.toByteArray());) {
            List<String> actualPrintChain = readInputStream(inputStream);

            assertEquals(expectedPrintChain.size(), actualPrintChain.size());

            for (int i = 0; i < actualPrintChain.size(); i++) {
                assertEquals(expectedPrintChain.get(i), actualPrintChain.get(i), "Line " + (i + 1));
            }
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

    private static class MyEndGameTableBaseExtension implements EndGameTableBase {

        @Override
        public boolean isProbeAvailable() {
            return false;
        }

        @Override
        public int evaluate() {
            return 0;
        }

        @Override
        public void setGame(Game game) {

        }
    }
}
