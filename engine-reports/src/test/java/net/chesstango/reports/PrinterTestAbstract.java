package net.chesstango.reports;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PrinterTestAbstract {
    protected void assertSearchTree(Printer printer, String resourceName) {
        List<String> expectedPrintChain = readResource(resourceName);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (PrintStream out = new PrintStream(baos, true, StandardCharsets.UTF_8);) {
            //chainPrinterVisitor.print(search, System.out);
            printer.setOut(out);
            printer.print();
        }

        try (InputStream inputStream = new ByteArrayInputStream(baos.toByteArray());) {
            List<String> actualPrintChain = readInputStream(inputStream);

            assertEquals(expectedPrintChain.size(), actualPrintChain.size());

            for (int i = 0; i < actualPrintChain.size(); i++) {
                assertEquals(expectedPrintChain.get(i), actualPrintChain.get(i), "Line " + (i + 1));
            }
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
