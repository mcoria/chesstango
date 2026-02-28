package net.chesstango.reports;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Mauricio Coria
 */
public class PrinterTestAbstract {

    private static final boolean DEBUG = false;

    protected void assertSearchTree(Printer printer, String resourceName) {
        List<String> expectedPrintChain = readResource(resourceName);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (PrintStream out = new PrintStream(baos, true, StandardCharsets.UTF_8);) {
            printer.setOut(out);
            printer.print();
        }

        if(DEBUG) {
            printer.setOut(System.out);
            printer.print();
        }

        // Compares output to expected; throws on failure
        try (InputStream inputStream = new ByteArrayInputStream(baos.toByteArray());) {
            List<String> actualOutput = readInputStream(inputStream);

            for (int i = 0; i < actualOutput.size(); i++) {
                assertEquals(expectedPrintChain.get(i), actualOutput.get(i), "Line difference" + (i + 1));
            }

            assertEquals(expectedPrintChain.size(), actualOutput.size());
        } catch (IOException e) {
            throw new RuntimeException(e);
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
